# Knapsack Optimizer Service

##Architecture

The code is partitioned into 3 parts Service, Tasks and Database.

###Service
The service part is the simplest part, it contains:
 
 * The controllers for the HTTP endpoints. 
 * Models for the json requests and responses, and (de)serialization of them.
 * Validation for incoming requests, and the mapping of errors to HTTP response codes and error messages.
 
The validation is mostly based on reading the Google OR-Tools source code; 
such as how values and weights need to have the same size, 
and no numeric could be larger than int64 etc.

All requests are then processed by the service.RequestProcessor, which is the only part of the service package 
to have any coupling with any other part of the code.

###Tasks



###Database

The database is used by the other two other parts so I tried to keep the shared code as generic as possible. 
This was achieved by having every(shared) query to the database:

* Return a function accepts a connection and returns...
* ...a function that accepts `java.sql.ResultSet` and returns...
* ...any value `T`.

This not only means loose coupling, but also lets the database implement simple utility functions
such as the "maybe" function that automatically turns potentially empty result sets into `java.util.Optional<T>`:

    static <T> ResultSetReadable<Optional<T>> maybe(ResultSetReadable<T> reader){
        return r -> r.next()
                ? Optional.ofNullable(reader.read(r))
                : Optional.empty();
    }

####Configuration/Thread pool

In order to no block any of the service threads all calls to the database go through a 
different thread pool defined in [database.context.DatabaseExecutionContext](https://github.com/Raudbjorn/knapsack-optimizer-service/blob/master/app/database/context/DatabaseExecutionContext.java) and configured in 
[application.conf](https://github.com/Raudbjorn/knapsack-optimizer-service/blob/master/conf/application.conf).
According to best practices this should be set to ((physical_core_count * 2) + effective_spindle_count)
on the machine running the database -currently I have it set at 4 for the SqLite database I configured.

There is also one trigger in the database [UPDATE_STATUS_TIME](https://github.com/Raudbjorn/knapsack-optimizer-service/blob/master/conf/database/UPDATE_STATUS_TIME.sql)
; it simply records the times of when a task transitions from one status to another, 
it seems like that doing that sort of thing consistently in code is very difficult to do, so it's best left to the database.


