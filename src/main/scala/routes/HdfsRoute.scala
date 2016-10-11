package routes

import services.hdfs.HdfsService._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import mappings.JsonMappings
import akka.http.scaladsl.server.Directives._
import spray.json._

trait HdfsRoute extends JsonMappings with SecurityDirectives {
  val hdfsApi = pathPrefix("hdfs") {
    pathPrefix("list") {
      pathEndOrSingleSlash {
        get {
          parameter("path") { path =>
            complete(listDirectory(path).map(_.toJson))
          }
        }
      }
    } ~
    pathPrefix("home") {
      pathEndOrSingleSlash {
        get {
          entity(as[String]) { path =>
            complete(homeDirectory().map(_.toJson))
          }
        }
      }
    } ~
    pathPrefix("open") {
      pathEndOrSingleSlash {
        get {
          parameter("path") { path =>
            complete(openFile(path).map(_.toJson))
          }
        }
      }
    } ~
    pathPrefix("create") {
      pathEndOrSingleSlash {
        get {
          parameter("path") { path =>
            complete(createFile(path).map(_.toJson))
          }
        }
      }
    } ~
    pathPrefix("status") {
      pathEndOrSingleSlash {
        get {
          parameter("path") { path =>
            complete(fileStatus(path).map(_.toJson))
          }
        }
      }
    }
  }
}
