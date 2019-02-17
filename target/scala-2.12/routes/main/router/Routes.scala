// @GENERATOR:play-routes-compiler
// @SOURCE:C:/code/knapsack-optimizer-service/conf/routes
// @DATE:Sat Feb 16 21:57:12 GMT 2019

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset
import _root_.play.libs.F

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:5
  TaskController_1: controllers.knapsack.TaskController,
  // @LINE:9
  SolutionController_2: controllers.knapsack.SolutionController,
  // @LINE:11
  AdminController_0: controllers.knapsack.AdminController,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:5
    TaskController_1: controllers.knapsack.TaskController,
    // @LINE:9
    SolutionController_2: controllers.knapsack.SolutionController,
    // @LINE:11
    AdminController_0: controllers.knapsack.AdminController
  ) = this(errorHandler, TaskController_1, SolutionController_2, AdminController_0, "/")

  def withPrefix(addPrefix: String): Routes = {
    val prefix = play.api.routing.Router.concatPrefix(addPrefix, this.prefix)
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, TaskController_1, SolutionController_2, AdminController_0, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """knapsack/tasks""", """controllers.knapsack.TaskController.createTask(request:Request)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """knapsack/tasks/""" + "$" + """id<[^/]+>""", """controllers.knapsack.TaskController.getTasks(id:Long)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """knapsack/solutions/""" + "$" + """id<[^/]+>""", """controllers.knapsack.SolutionController.getSolution(id:Long)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """knapsack/admin/tasks""", """controllers.knapsack.AdminController.getAllTasks"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:5
  private[this] lazy val controllers_knapsack_TaskController_createTask0_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("knapsack/tasks")))
  )
  private[this] lazy val controllers_knapsack_TaskController_createTask0_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      TaskController_1.createTask(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.knapsack.TaskController",
      "createTask",
      Seq(classOf[play.mvc.Http.Request]),
      "POST",
      this.prefix + """knapsack/tasks""",
      """""",
      Seq()
    )
  )

  // @LINE:7
  private[this] lazy val controllers_knapsack_TaskController_getTasks1_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("knapsack/tasks/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_knapsack_TaskController_getTasks1_invoker = createInvoker(
    TaskController_1.getTasks(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.knapsack.TaskController",
      "getTasks",
      Seq(classOf[Long]),
      "GET",
      this.prefix + """knapsack/tasks/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:9
  private[this] lazy val controllers_knapsack_SolutionController_getSolution2_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("knapsack/solutions/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_knapsack_SolutionController_getSolution2_invoker = createInvoker(
    SolutionController_2.getSolution(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.knapsack.SolutionController",
      "getSolution",
      Seq(classOf[Long]),
      "GET",
      this.prefix + """knapsack/solutions/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:11
  private[this] lazy val controllers_knapsack_AdminController_getAllTasks3_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("knapsack/admin/tasks")))
  )
  private[this] lazy val controllers_knapsack_AdminController_getAllTasks3_invoker = createInvoker(
    AdminController_0.getAllTasks,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.knapsack.AdminController",
      "getAllTasks",
      Nil,
      "GET",
      this.prefix + """knapsack/admin/tasks""",
      """""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:5
    case controllers_knapsack_TaskController_createTask0_route(params@_) =>
      call { 
        controllers_knapsack_TaskController_createTask0_invoker.call(
          req => TaskController_1.createTask(req))
      }
  
    // @LINE:7
    case controllers_knapsack_TaskController_getTasks1_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_knapsack_TaskController_getTasks1_invoker.call(TaskController_1.getTasks(id))
      }
  
    // @LINE:9
    case controllers_knapsack_SolutionController_getSolution2_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_knapsack_SolutionController_getSolution2_invoker.call(SolutionController_2.getSolution(id))
      }
  
    // @LINE:11
    case controllers_knapsack_AdminController_getAllTasks3_route(params@_) =>
      call { 
        controllers_knapsack_AdminController_getAllTasks3_invoker.call(AdminController_0.getAllTasks)
      }
  }
}
