package database.util;

import play.db.ConnectionCallable;

@FunctionalInterface
public interface ResultSetProvider<T> {
    ConnectionCallable<T> withReader(ResultSetReadable<T> reader);
}
