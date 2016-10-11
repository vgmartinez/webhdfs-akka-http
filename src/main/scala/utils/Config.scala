package utils

import com.typesafe.config.ConfigFactory

trait Config {
  private val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")
  private val databaseConfig = config.getConfig("database")
  private val hdfsConfig = config.getConfig("hdfs")

  val httpInterface = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")

  val databaseUrl = databaseConfig.getString("url")
  val databaseUser = databaseConfig.getString("user")
  val databasePassword = databaseConfig.getString("password")
  val databaseNumThreads = databaseConfig.getString("numThreads")

  val hdfsHost = hdfsConfig.getString("default_host")
  val hdfsPort = hdfsConfig.getString("default_port")
  val hdfsUserName = hdfsConfig.getString("default_username")
  val hdfsPassword = hdfsConfig.getString("default_password")
  val hdfsProtocol = hdfsConfig.getString("default_protocol")
}
