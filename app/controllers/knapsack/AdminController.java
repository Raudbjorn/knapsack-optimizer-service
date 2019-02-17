package controllers.knapsack;

import akka.actor.CoordinatedShutdown;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

public class AdminController extends Controller {

    private CoordinatedShutdown coordinatedShutdown;

    @Inject
    public AdminController(CoordinatedShutdown coordinatedShutdown){
        this.coordinatedShutdown = coordinatedShutdown;
    }

    public Result getAllTasks() {
        return null;
    }

    public Result shutdown(){
        coordinatedShutdown.run();
        return ok();
    }

}
