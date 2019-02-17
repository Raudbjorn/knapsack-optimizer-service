package repositories;


import play.db.Database;
import repositories.db.DatabaseExecutionContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProblemRepository {

    private Database db;
    private DatabaseExecutionContext executionContext;

    @Inject
    public ProblemRepository(Database db, DatabaseExecutionContext context) {
        this.db = db;
        this.executionContext = context;
    }
}
