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
class Task9 @Inject() (protected val dbConfigProvider: DatabaseConfigProvider, cc:ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {
def load= Action{ implicit request=>
        Ok(views.html.task9())
    }
    implicit val userDataReads = Json.reads[UserData]
    implicit val localMessage = Json.reads[LocalMessage]

    def withJsonBody[A](f: A => Future[Result])(implicit request: Request[AnyContent], reads: Reads[A]): Future[Result] = {
        request.body.asJson.map { body =>
        Json.fromJson[A](body) match {
        case JsSuccess(a, path) => f(a)
        case e @ JsError(_) => Future.successful(Redirect(routes.Task9.load))
      }
    }.getOrElse(Future.successful(Redirect(routes.Task9.load)))
  }
  def withSessionUsername(f: String => Result)(implicit request: Request[AnyContent]) = {
    request.session.get("username").map(f).getOrElse(Ok(Json.toJson(Seq.empty[String])))
  }

    def login = ???
//        Action { implicit request =>
//        withJsonBody[UserData] { ud =>
//            if(Task5MemoryModel.validateInfo(ud.username, ud.password)){
//                Ok(Json.toJson(true))
//                .withSession("username" -> ud.username, "csrfToken" ->play.filters.csrf.CSRF.getToken.map(_.value).getOrElse(""))
//            }else{
//                Ok(Json.toJson(false))
//            }
//        }
//    }

    def create = ???
//        Action { implicit request =>
//        withJsonBody[UserData] {ud =>
//            if(Task5MemoryModel.createUser(ud.username, ud.password)){
//                Ok(Json.toJson(true))
//                .withSession("username" -> ud.username, "csrfToken" ->play.filters.csrf.CSRF.getToken.map(_.value).getOrElse(""))  
//            }else{
//                Ok(Json.toJson(false))
//            }
//        }
//    }
    def getGlobalMessages = ???
//        Action { implicit request=>
//        Ok(Json.toJson(Task5MemoryModel.getGlobalMessages()))   
//        }
//    def getLocalMessages = Action { implicit request =>
//        withSessionUsername { username =>
//            Ok(Json.toJson(Task5MemoryModel.getLocalMessages(username)))
//        }
//    }
    def sendGlobalMessage = ???
//        Action { implicit request =>
//        withSessionUsername { username=>
//            withJsonBody[String] { message=>
//            Task5MemoryModel.sendGlobalMessage(username, message); 
//            Ok(Json.toJson(true)); 
//        }
//    }
//
//    }
    def sendLocalMessage = ???
//    Action { implicit request =>
//        withSessionUsername { username =>
//            withJsonBody[LocalMessage] { lm=>
//
//                    if(Task5MemoryModel.sendLocalMessageTask8(username, lm.reciever, lm.contents)){
//                    Ok(Json.toJson(true))
//                    } else{
//                    Ok(Json.toJson(false))
//                }
//            }
//        }
//    }
}



