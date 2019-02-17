package controllers.knapsack;

import models.knapsack.Problem;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import solvers.Knapsack;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Patterns.$Left;
import static io.vavr.Patterns.$Right;

public class TaskController extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public Result createTask(Http.Request request){
        Problem problem = Json.fromJson(request.body().asJson(), Problem.class);


        return Match(Knapsack.solve(problem)).of(
                Case($Right($()), result -> status(OK, result.toJson())),
                Case($Left($()), error -> status(error.status, error.errorMsg))
        );
    }

    public Result getTasks(long id){
        return null;
    }

}
