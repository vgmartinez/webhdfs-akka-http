package routes

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import mappings.JsonMappings
import models.{UserEntityUpdate}
import akka.http.scaladsl.server.Directives._
import services.users.UsersService._
import spray.json._


trait UsersRoute extends JsonMappings with SecurityDirectives {
  import StatusCodes._

  val usersApi = pathPrefix("users") {
    pathEndOrSingleSlash {
      get {
        complete(findAll.map(_.toJson))
      }
    } ~
    pathPrefix(IntNumber) { id =>
      pathEndOrSingleSlash {
        authenticate { loggedUser =>
          get {
            complete(findById(id).map(_.toJson))
          } ~
          post {
            entity(as[UserEntityUpdate]) { userUpdate =>
              complete(update(id, userUpdate).map(_.toJson))
            }
          } ~
          delete {
            onSuccess(deleteUser(loggedUser.id.get)) { ignored =>
              complete(NoContent)
            }
          }
        }
      }
    }
  }
}
