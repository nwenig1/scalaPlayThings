package controllers


import javax.inject._
import shared.SharedMessages
import play.api.mvc._
//import models.Task8MemoryModel
import play.api.libs.json._
import models._

import scala.concurrent.Future
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.ExecutionContext
import play.api.db.slick.HasDatabaseConfigProvider

import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._


@Singleton
class Task9 @Inject() (protected val dbConfigProvider: DatabaseConfigProvider, cc:ControllerComponents)(implicit ec: ExecutionContext) 
extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {

  private val model = new Task9DatabaseModel(db)

  def load= Action{ implicit request=>
        Ok(views.html.task9())
    }
    implicit val dumbIdeaReads = Json.reads[dumbIdea]
    implicit val userDataReads = Json.reads[UserData]
    implicit val localMessageWrites = Json.writes[LocalMessage]
    implicit val globalMessageWrites = Json.writes[GlobalMessage]

    def withJsonBody[A](f: A => Future[Result])(implicit request: Request[AnyContent], reads: Reads[A]): Future[Result] = {
        request.body.asJson.map { body =>
        Json.fromJson[A](body) match {
        case JsSuccess(a, path) => f(a)
        case e @ JsError(_) => Future.successful(Redirect(routes.Task9.load))
      }
    }.getOrElse(Future.successful(Redirect(routes.Task9.load)))
  }
  def withSessionUsername(f: String => Future[Result])(implicit request: Request[AnyContent]) : Future[Result] = {
    request.session.get("username").map(f).getOrElse(Future.successful(Ok(Json.toJson(Seq.empty[String]))))
  }

    def login = Action.async { implicit request =>
        withJsonBody[UserData] { ud =>
          model.validateUser(ud.username, ud.password).map { userId =>
            userId match{
              case Some(userId) => Ok(Json.toJson(true)).withSession("username" -> ud.username, "userid" -> userId.toString, 
              "csrfToken" -> play.filters.csrf.CSRF.getToken.map(_.value).getOrElse(""))
              case None => Ok(Json.toJson(false))
            } 
          }  
        }
    }

    def create = Action.async { implicit request =>
      withJsonBody[UserData] {ud =>
            model.createUser(ud.username, ud.password).map { userId =>
              userId match {
                
              case Some(userId) => {
                Ok(Json.toJson(true)).withSession("username" -> ud.username, "userId" -> userId.toString,
              "csrfToken" -> play.filters.csrf.CSRF.getToken.map(_.value).getOrElse(""))
              }
              case None => {
                Ok(Json.toJson(false))
              }
            }
            }
        }
    }
    def getGlobalMessages = Action.async { implicit request=>
      withSessionUsername { username =>
          model.getGlobalMessages().map(globalmessages => Ok(Json.toJson(globalmessages)))
      }  
        }

    def sendGlobalMessage = Action.async { implicit request =>
    withSessionUsername { username=>
            withJsonBody[String] { message =>
            model.sendGlobalMessage(username, message); 
            Future.successful(Ok(Json.toJson(true))); 
        }
    }

   }
   def getLocalMessages = Action.async { implicit request =>
    withSessionUsername { username =>
      model.getLocalMessages(username).map(localMessages => Ok(Json.toJson(localMessages)))
    }
  
  }
    def sendLocalMessage =Action.async { implicit request =>
        withSessionUsername { username =>
            withJsonBody[dumbIdea] { lm=>
              model.sendLocalMessage(username, lm.reciever, lm.contents).flatMap { result=>
                if(result){
                Future.successful(Ok(Json.toJson(true)))
              } else {
                Future.successful(Ok(Json.toJson(false)))
              }
              }
            }
        }
    }
}



