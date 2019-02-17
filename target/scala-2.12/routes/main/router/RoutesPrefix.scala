// @GENERATOR:play-routes-compiler
// @SOURCE:C:/code/knapsack-optimizer-service/conf/routes
// @DATE:Sat Feb 16 21:57:12 GMT 2019


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
