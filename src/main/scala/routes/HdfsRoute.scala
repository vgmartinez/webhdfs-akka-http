package routes

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import spray.json._
import mappings.JsonMappings
import services.hdfs.HdfsService._

trait HdfsRoute extends JsonMappings with SecurityDirectives {
  val hdfsApi = pathPrefix("hdfs") {
    pathPrefix("list") {
      pathEndOrSingleSlash {
        authenticate { loggedUser =>
          get {
            parameter("path") { path =>
              complete(listDirectory(path, loggedUser).map(_.toJson))
            }
          }
        }
      }
    } ~
    pathPrefix("home") {
      pathEndOrSingleSlash {
        authenticate { loggedUser =>
          get {
            entity(as[String]) { path =>
              complete(homeDirectory(loggedUser).map(_.toJson))
            }
          }
        }
      }
    } ~
    pathPrefix("open") {
      pathEndOrSingleSlash {
        authenticate { loggedUser =>
          get {
            parameter("path") { path =>
              complete(openFile(path, loggedUser).map(_.toJson))
            }
          }
        }
      }
    } ~
    pathPrefix("create") {
      pathEndOrSingleSlash {
        authenticate { loggedUser =>
          get {
            parameter("path") { path =>
              complete(createFile(path, loggedUser).map(_.toJson))
            }
          }
        }
      }
    } ~
    pathPrefix("status") {
      pathEndOrSingleSlash {
        authenticate { loggedUser =>
          get {
            parameter("path") { path =>
              complete(fileStatus(path, loggedUser).map(_.toJson))
            }
          }
        }
      }
    }
  }
}
