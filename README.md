# Knapsack Optimizer Service

## Running the application

**IMPORTANT**:
In order for the application to even start it needs to be aware of the native binaries required to 
run the solution code(which are provided in the /lib-folder).
It's easiest to simply add "-Djava.library.path=lib/" to the JAVA_OPTS environment
variable, or if that does not work, try adding the absolute path to the /lib-folder.

The server can also be started in dev-mode by passing the options through SBT using the "-J"-prefix, 
like so: `$ sbt run -J-Djava.library.path=lib/`

### Running in container

SBT version 1.1.2 is required to build the application, and a docker file is provided.

    [sg@Sveinbjorn knapsack-optimizer-service]$ sbt dist
    [...]
    [success] All package validations passed
    [info] Your package is ready in /home/sg/code/knapsack-optimizer-service/target/universal/knapsack-optimizer-service-1.0-SNAPSHOT.zip
    [...]
    [sg@Sveinbjorn knapsack-optimizer-service]$ docker build .
    [...]
    Removing intermediate container 6482d3d6835e
     ---> 0e442bdf6442
    Successfully built 0e442bdf6442
    [sg@Sveinbjorn knapsack-optimizer-service]$ docker run -d -p 9000:9000 0e442bdf6442
    d7b2fc7d57cb68270e40017838e9f0c4a4f657d12c56630a46a200c1a33e6560

## Architecture

The code is partitioned into 3 parts: Service, Tasks and Database.

### Service
The service part is the simplest part, it contains:
 
 * The controllers for the HTTP endpoints. 
 * Models for the json requests and responses, and (de)serialization of them.
 * Validation for incoming requests, and the mapping of errors to HTTP response code(s) and message(s).
 
The validation is mostly based on reading the Google OR-Tools source code; 
such as how values and weights need to have the same size, no numeric could be larger than int64 etc.,
and it's implemented using [Vavr's](http://www.vavr.io/vavr-docs/#_validation) Validation applicative functor, 
so it should accumulate all errors(i.e. it won't "fail fast").

All requests are then processed by the `service.RequestProcessor`, which is the only part of the service package 
to have any coupling with any other part of the code.

### Tasks

As suggested, I'm using [Google's OR-Tools](https://developers.google.com/optimization/), although I probably shouldn't have
since, because of a class loader issue, running native code with the Play Framework is incredibly difficult to get working,
so much so that there's a [Play Native Loader](https://github.com/playframework/play-native-loader) library maintained by it's creators(which I'm using).

All task logic is found in `tasks.TaskProcessor`, which is the only part of the tasks package to have any coupling with any other part of the code.

Creating a new task:

* Compare the problem with other problems persisted into the database.
* If a solution is found, return the task associated with it.
* If not, persist the task and spawn a new thread to compute the solution, and respond to the request appropriately on the current thread.
* Naive cancellation functionality is implemented by storing a reference to running tasks in a 
`java.util.concurrent.ConcurrentHashMap<Long, CompletionStage<Void>>`, adding them 
when they get started, and removing them once they're completed(or when they are cancelled by the user).

#### Configuration/Thread pool

Since the tasks are potentially computationally intensive, rather than block the calling thread, I decided to create a separate 
thread pool to run them, defined in `tasks.context.TaskExecutionContext` and configured in [application.conf](/conf/application.conf),
I have it currently set to 4 threads.

### Database

The database is used by the other two other parts so I tried to keep the shared code as generic as possible. 
This was achieved by having every(shared) query to the database:

* Return a function accepts a connection and returns...
* ...a function that accepts `java.sql.ResultSet` and returns...
* ...any value `<T>`.

This not only means loose coupling, but also lets the database implement simple utility functions
such as [the "maybe" function](/app/database/util/ResultSetReadable.java#L13) 
that automatically turns potentially empty result sets into `java.util.Optional<T>`.

#### Configuration/Thread pool

In order to not block any of the service threads, all calls to the database go through a 
different thread pool defined in [database.context.DatabaseExecutionContext](/app/database/context/DatabaseExecutionContext.java) and configured in 
[application.conf](/conf/application.conf).
According to best practices this should be set to ((physical_core_count * 2) + effective_spindle_count)
on the machine running the database -currently I have it set to 4 for the SqLite database I configured.

There is also one trigger in the database: [UPDATE_STATUS_TIME](/conf/database/UPDATE_STATUS_TIME.sql),
 it simply records the times when a task transitions from one status to another, 
it seems like that doing that sort of thing consistently in code is very difficult to do, so it's best left to the database.

