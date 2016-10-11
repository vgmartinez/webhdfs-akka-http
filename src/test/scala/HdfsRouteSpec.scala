import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCode}
import models.{Comment, Post}
import org.scalatest.concurrent.ScalaFutures
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import services.hdfs.HdfsService

class HdfsRouteSpec extends BaseServiceSpec with ScalaFutures{
  "Comments api" should {
    "retrieve comments list" in {
      Get("/users/1/posts/1/comments") ~> hdfsApi ~> check {
        responseAs[JsArray] should be(List(testComments.head).toJson)
      }
    }
    "retrieve comment by id" in {
      Get("/users/1/posts/1/comments/1") ~> hdfsApi ~> check {
        responseAs[JsObject] should be(testComments.head.toJson)
      }
    }
    "create comment properly" in {
      val newContent = "newContent"
      val requestEntity = HttpEntity(MediaTypes.`application/json`,
        JsObject(
          "postId" -> JsNumber(testPosts.head.id.get),
          "userId" -> JsNumber(testUsers.head.id.get),
          "content" -> JsString(newContent)
        ).toString())
      Post("/comments", requestEntity) ~> hdfsApi ~> check {
        response.status should be(StatusCode.int2StatusCode(200))
        Get("/users/1/posts/1/comments") ~> hdfsApi ~> check {
          responseAs[Seq[Comment]] should have length 2
        }
      }
    }
    "update comment by id" in {
      val newContent = "UpdatedContent"
      val requestEntity = HttpEntity(MediaTypes.`application/json`,
        JsObject(
          "postId" -> JsNumber(testPosts.head.id.get),
          "userId" -> JsNumber(testUsers.head.id.get),
          "content" -> JsString(newContent)
        ).toString())
      Put("/users/1/posts/1/comments/1", requestEntity) ~> hdfsApi ~> check {
        response.status should be(StatusCode.int2StatusCode(200))
        whenReady(HdfsService.findById(1,1, 1)) { result =>
          result.content should be(newContent)
        }
      }
    }
    "delete comment by id" in {
      Delete("/comments/1") ~> hdfsApi ~> check {
        response.status should be(StatusCode.int2StatusCode(200))
        Get("/users/1/posts/1/comments") ~> hdfsApi ~> check {
          responseAs[Seq[Comment]] should have length 1
        }
      }
    }
  }
}
