package routes

import akka.http.scaladsl.server.Directives._
import utils.CorsSupport

trait Routes extends ApiErrorHandler with AuthRoute with UsersRoute with HdfsRoute with CorsSupport {
  val routes =
    pathPrefix("v1") {
      authRoute ~
      usersApi ~
      hdfsApi
    } ~ path("")(getFromResource("public/index.html"))
}
