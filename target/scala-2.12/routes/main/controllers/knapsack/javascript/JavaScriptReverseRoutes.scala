// @GENERATOR:play-routes-compiler
// @SOURCE:C:/code/knapsack-optimizer-service/conf/routes
// @DATE:Sat Feb 16 21:57:12 GMT 2019

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:5
package controllers.knapsack.javascript {

  // @LINE:11
  class ReverseAdminController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:11
    def getAllTasks: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.knapsack.AdminController.getAllTasks",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "knapsack/admin/tasks"})
        }
      """
    )
  
  }

  // @LINE:5
  class ReverseTaskController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:5
    def createTask: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.knapsack.TaskController.createTask",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "knapsack/tasks"})
        }
      """
    )
  
    // @LINE:7
    def getTasks: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.knapsack.TaskController.getTasks",
      """
        function(id0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "knapsack/tasks/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
  }

  // @LINE:9
  class ReverseSolutionController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:9
    def getSolution: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.knapsack.SolutionController.getSolution",
      """
        function(id0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "knapsack/solutions/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[Long]].javascriptUnbind + """)("id", id0))})
        }
      """
    )
  
  }


}
