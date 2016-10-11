import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCode}
import models.UserEntity
import org.scalatest.concurrent.ScalaFutures
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import services.users.UsersService

class UsersRouteSpec extends BaseServiceSpec with ScalaFutures{
  "Users service" should {
    "retrieve users list" in {
      Get("/users") ~> usersApi ~> check {
        responseAs[JsArray] should be(testUsers.toJson)
      }
    }
    "retrieve user by id" in {
      Get("/users/1") ~> usersApi ~> check {
        responseAs[JsObject] should be(testUsers.head.toJson)
      }
    }
    "create user properly" in {
      val newUsername = "UpdatedUsername"
      val requestEntity = HttpEntity(MediaTypes.`application/json`,
        JsObject(
          "userName" -> JsString(newUsername),
          "password" -> JsString(testUsers.head.password),
          "age" -> JsNumber(testUsers.head.age),
          "gender" -> JsNumber(testUsers.head.gender)
        ).toString())
      Post("/users", requestEntity) ~> usersApi ~> check {
        response.status should be(StatusCode.int2StatusCode(200))
        Get("/users") ~> usersApi ~> check {
          responseAs[Seq[UserEntity]] should have length 4
        }
      }
    }
    "update user by id" in {
      val newUsername = "UpdatedUsername"
      val requestEntity = HttpEntity(MediaTypes.`application/json`,
        JsObject(
          "userName" -> JsString(newUsername),
          "password" -> JsString(testUsers.head.password),
          "age" -> JsNumber(testUsers.head.age),
          "gender" -> JsNumber(testUsers.head.gender)
        ).toString())
      Put("/users/1", requestEntity) ~> usersApi ~> check {
        response.status should be(StatusCode.int2StatusCode(200))
        whenReady(UsersService.findById(1)) { result =>
          result.username should be(newUsername)
        }
      }
    }
    "delete user by id" in {
      Delete("/users/1") ~> usersApi ~> check {
        response.status should be(StatusCode.int2StatusCode(200))
        Get("/users") ~> usersApi ~> check {
          responseAs[Seq[UserEntity]] should have length 3
        }
      }
    }
  }
}
