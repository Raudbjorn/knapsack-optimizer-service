package database.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@FunctionalInterface
public interface ResultSetReadable<T> {

    T read(ResultSet resultSet) throws SQLException;


    static <T> ResultSetReadable<Optional<T>> maybe(ResultSetReadable<T> reader){
        return r -> r.next()
                ? Optional.ofNullable(reader.read(r))
                : Optional.empty();
    }
}
