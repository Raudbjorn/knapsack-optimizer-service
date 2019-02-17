// @GENERATOR:play-routes-compiler
// @SOURCE:C:/code/knapsack-optimizer-service/conf/routes
// @DATE:Sat Feb 16 21:57:12 GMT 2019

package controllers.knapsack;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.knapsack.ReverseAdminController AdminController = new controllers.knapsack.ReverseAdminController(RoutesPrefix.byNamePrefix());
  public static final controllers.knapsack.ReverseTaskController TaskController = new controllers.knapsack.ReverseTaskController(RoutesPrefix.byNamePrefix());
  public static final controllers.knapsack.ReverseSolutionController SolutionController = new controllers.knapsack.ReverseSolutionController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.knapsack.javascript.ReverseAdminController AdminController = new controllers.knapsack.javascript.ReverseAdminController(RoutesPrefix.byNamePrefix());
    public static final controllers.knapsack.javascript.ReverseTaskController TaskController = new controllers.knapsack.javascript.ReverseTaskController(RoutesPrefix.byNamePrefix());
    public static final controllers.knapsack.javascript.ReverseSolutionController SolutionController = new controllers.knapsack.javascript.ReverseSolutionController(RoutesPrefix.byNamePrefix());
  }

}
