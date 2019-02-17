package repositories;

import play.db.Database;
import repositories.db.DatabaseExecutionContext;

import javax.inject.Inject;
import javax.inject.Singleton;



@Singleton
public class SolutionRepository {

    private Database db;
    private DatabaseExecutionContext executionContext;

    @Inject
    public SolutionRepository(Database db, DatabaseExecutionContext context) {
        this.db = db;
        this.executionContext = context;
    }
}
