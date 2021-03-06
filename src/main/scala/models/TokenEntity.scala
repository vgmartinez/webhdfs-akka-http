package models

import java.util.UUID

case class TokenEntity(id: Option[Long] = None, userId: Option[Long], token: String = UUID.randomUUID().toString.replaceAll("-", ""))

case class LoginPassword(login: String, password: String)