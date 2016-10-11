package routes

import services.hdfs.HdfsService._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import mappings.JsonMappings
import akka.http.scaladsl.server.Directives._
import spray.json._

trait HdfsRoute extends JsonMappings with SecurityDirectives {
  val hdfsApi =
    (path("list") & get )  { parameter("path") { path =>
        complete (listDirectory(path).map(_.toJson))
      }
    }~
      (path("home") & get ) { entity(as[String]) { path =>
        complete (homeDirectory().map(_.toJson))
      }
    }~
      (path("open") & get )  { parameter("path") { path =>
        complete (openFile(path).map(_.toJson))
      }
     }~
      (path("create") & put )  { parameter("path") { path =>
        complete (createFile(path).map(_.toJson))
      }
      }~
      (path("status") & get )  { parameter("path") { path =>
        complete (fileStatus(path).map(_.toJson))
      }
    }
}
