// @GENERATOR:play-routes-compiler
// @SOURCE:C:/code/knapsack-optimizer-service/conf/routes
// @DATE:Sat Feb 16 21:57:12 GMT 2019

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:5
package controllers.knapsack {

  // @LINE:11
  class ReverseAdminController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:11
    def getAllTasks(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "knapsack/admin/tasks")
    }
  
  }

  // @LINE:5
  class ReverseTaskController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:5
    def createTask(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "knapsack/tasks")
    }
  
    // @LINE:7
    def getTasks(id:Long): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "knapsack/tasks/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
  }

  // @LINE:9
  class ReverseSolutionController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:9
    def getSolution(id:Long): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "knapsack/solutions/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
  }


}
