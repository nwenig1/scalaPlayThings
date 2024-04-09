package models

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext
import models.Tables._
import scala.concurrent.Future
import org.mindrot.jbcrypt.BCrypt

class Task9DatabaseModel(db: Database)(implicit ec: ExecutionContext) {
  def validateUser(username: String, password: String) : Future[Option[Int]] = {
    ???
  }
}
