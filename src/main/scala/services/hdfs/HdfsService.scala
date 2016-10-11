package services.hdfs

import java.io._
import java.net.{HttpURLConnection, URL, URLConnection}
import java.text.MessageFormat

import com.typesafe.scalalogging._
import org.apache.commons.io.IOUtils
import services.Base

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object HdfsService extends Base {
  val logger = Logger("HdfsService")

  val httpfsUrl = hdfsProtocol + hdfsHost + ":" + hdfsPort
  val userName = hdfsUserName

  def listDirectory(path: String): Future[String] = {
    val url = new URL(new URL(httpfsUrl), MessageFormat.format("/webhdfs/v1/{0}?user.name={1}&op=LISTSTATUS", path, userName))
    print(path)
    val conn = url.openConnection()
    val response: Future[String] = result(conn)
    response
  }

  def homeDirectory(): Future[String] = {
    val url = new URL(new URL(httpfsUrl), "/webhdfs/v1/?user.name=hadoop&op=GETHOMEDIRECTORY")

    val conn = url.openConnection()
    val response: Future[String] = result(conn)
    response
  }

  def openFile(path: String): Future[String] = {
    val url = new URL(new URL(httpfsUrl), MessageFormat.format("/webhdfs/v1/{0}?user.name={1}&op=OPEN", path, userName))
    print(path)
    val conn = url.openConnection()
    conn.setRequestProperty("Content-Type", "application/octet-stream");
    val response: Future[String] = result(conn)
    response
  }

  def fileStatus(path: String): Future[String] = {
    val url = new URL(new URL(httpfsUrl), MessageFormat.format("/webhdfs/v1/{0}?user.name={1}&op=GETFILESTATUS", path, userName))
    print(path)
    val conn = url.openConnection()
    val response: Future[String] = result(conn)
    response
  }

  def createFile(path: String): String = {
    val url = new URL(new URL(httpfsUrl), MessageFormat.format("/webhdfs/v1/{0}?user.name={1}&op=CREATE", path, userName))
    var response: String = null
    var redirectUrl = ""
    val conn = url.openConnection().asInstanceOf[HttpURLConnection]

    conn.setRequestMethod("PUT")
    conn.setInstanceFollowRedirects(false)
    conn.connect()
    logger.info("Location:" + conn.getHeaderField("Location"))

    if (conn.getResponseCode() == 307)
      redirectUrl = conn.getHeaderField("Location")
    conn.disconnect()

    if (redirectUrl != null) {
      val conn = url.openConnection().asInstanceOf[HttpURLConnection]
      conn.setRequestMethod("PUT")
      conn.setDoOutput(true)
      conn.setDoInput(true)
      conn.setUseCaches(false)
      conn.setRequestProperty("Content-Type", "application/octet-stream")
      // conn.setRequestProperty("Transfer-Encoding", "chunked")
      conn.connect()
      response = resultSync(conn)
      conn.disconnect()
    }
    response
  }

  protected def copy(input: InputStream, result: OutputStream): Long = {
    val buffer: Array[Byte] = new Array[Byte](12288)
    var count: Long = 0L
    var n: Int = 0
    while (-1 != (n = input.read(buffer))) {
      {
        result.write(buffer, 0, n)
        count += n
        result.flush
      }
    }
    result.flush
    count
  }

  def result(conn: URLConnection): Future[String] = {
    val response: Future[String] = Future {
      val is: InputStream = conn.getInputStream
      IOUtils.toString(is, "UTF-8")
    }
    response.onFailure {
      case e => e.printStackTrace()
    }
    response
  }

  def resultSync(conn: URLConnection): String = {
    val is: InputStream = conn.getInputStream
    IOUtils.toString(is, "UTF-8")

  }
}
