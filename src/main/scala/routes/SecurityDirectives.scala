package routes

import akka.http.scaladsl.server._
import akka.http.scaladsl.server.directives.{BasicDirectives, FutureDirectives, HeaderDirectives, RouteDirectives}
import models.UserEntity
import services.users.AuthService

/**
  * Created by victorgarcia on 10/10/16.
  */
trait SecurityDirectives {
  import BasicDirectives._
  import HeaderDirectives._
  import RouteDirectives._
  import FutureDirectives._

  def authenticate: Directive1[UserEntity] = {
    headerValueByName("Token").flatMap { token =>
      onSuccess(AuthService.authenticate(token)).flatMap {
        case Some(user) => provide(user)
        case None       => reject
      }
    }
  }
}
