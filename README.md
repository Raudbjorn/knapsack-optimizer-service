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
    
### Running tests

Tests are run with SBT

    [sg@Sveinbjorn knapsack-optimizer-service]$ sbt test
    [...]
    [info] Passed: Total 6, Failed 0, Errors 0, Passed 6
    [success] Total time: 5 s, completed Feb 25, 2019 9:57:29 PM
    
## API

### Endpoints

**POST** /knapsack/tasks

**GET** /knapsack/tasks/:id

**GET** /knapsack/solutions/:id

**GET** /knapsack/admin/tasks          

**POST** /knapsack/admin/shutdown       

**DELETE** /knapsack/tasks/:id (This is the "cancel task" endpoint)

More information can be found in the [routes-file](/conf/routes).

### Example

I tried to follow the example session in the document I received as exactly as I could.


    [sg@Sveinbjorn knapsack-optimizer-service]$ sbt dist
    [...]
    [sg@Sveinbjorn knapsack-optimizer-service]$ docker build .
    [...]
    Successfully built 8e5621fb371b
    [sg@Sveinbjorn knapsack-optimizer-service]$ docker run -d -p 9000:9000 8e5621fb371b
    168f6bb42cc0ec3c1814a1b6a3538cef07049b951a3b78d1c6ce466dec5d9bf4
    [sg@Sveinbjorn knapsack-optimizer-service]$ curl -XPOST -H 'Content-type: application/json' http://localhost:9000/knapsack/tasks \
    > -d '{"problem": {"capacity": 60, "weights": [10, 20, 33], "values": [10, 3, 30]}}'
    {"task":"1","status":"submitted","timestamps":{"submitted":1551188693,"started":null,"completed":null}}
    [sg@Sveinbjorn knapsack-optimizer-service]$ curl -XGET http://localhost:9000/knapsack/tasks/1
    {"task":"1","status":"completed","timestamps":{"submitted":1551188693,"started":1551188693,"completed":1551188693}}
    [sg@Sveinbjorn knapsack-optimizer-service]$ curl -XGET http://localhost:9000/knapsack/solutions/1
    {"task":"1","problem":{"capacity":60,"weights":[10,20,33],"values":[10,3,30]},"solution":{"items":[0,2],"time":0}}
    [sg@Sveinbjorn knapsack-optimizer-service]$ curl -XGET http://localhost:9000/knapsack/admin/tasks
    {"tasks":{"submitted":[],"started":[],"completed":[{"task":"1","status":"completed","timestamps":{"submitted":1551188693,"started":1551188693,"completed":1551188693}}]}}
    [sg@Sveinbjorn knapsack-optimizer-service]$ curl -XGET http://localhost:9000/knapsack/admin/shutdown
    Service shutting down..
    [sg@Sveinbjorn knapsack-optimizer-service]$ docker ps -n 1
    CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS                          PORTS               NAMES
    168f6bb42cc0        8e5621fb371b        "/usr/src/knapsack-o…"   10 minutes ago      Exited (0) About a minute ago                       wizardly_swanson

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

