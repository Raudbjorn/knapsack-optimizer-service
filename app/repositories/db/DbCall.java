package repositories.db;

import java.util.concurrent.CompletionStage;

public interface DbCall {

    public static CompletionStage<Boolean> transactionally(DbCall... calls){
        return null;
    }


}
