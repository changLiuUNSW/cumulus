package io.cumulus.controllers

import scala.concurrent.ExecutionContext
import io.cumulus.controllers.payloads.{LoginPayload, SignUpPayload}
import io.cumulus.core.Settings
import io.cumulus.core.controllers.utils.api.ApiUtils
import io.cumulus.core.controllers.utils.authentication.Authentication
import io.cumulus.core.controllers.utils.authentication.Authentication._
import io.cumulus.core.controllers.utils.bodyParser.BodyParserJson
import io.cumulus.models.user.{User, UserSession}
import io.cumulus.services.UserService
import io.cumulus.views.CumulusEmailValidationPage
import play.api.libs.json.Json
import play.api.mvc._

class UserController (
  cc: ControllerComponents,
  userService: UserService
)(implicit
  ec: ExecutionContext,
  settings: Settings
) extends AbstractController(cc) with Authentication[UserSession] with ApiUtils with BodyParserJson {

  def signUp: Action[SignUpPayload] =
    Action.async(parseJson[SignUpPayload]) { implicit request =>
      ApiResponse {
        val signInPayload = request.body
        val user = User.create(signInPayload.email, signInPayload.login, signInPayload.password)

        userService.createUser(user)
      }
    }

  def validateEmail(userLogin: String, emailCode: String): Action[AnyContent] =
    Action.async { implicit request =>
      userService.validateUserEmail(userLogin, emailCode).map { result =>
        Ok(CumulusEmailValidationPage(result))
      }
    }

  def login: Action[LoginPayload] =
    Action.async(parseJson[LoginPayload]) { implicit request =>
      val loginPayload = request.body

      userService.loginUser(loginPayload.login, loginPayload.password).map {
        case Right(authenticatedUser) =>
          loginUser(authenticatedUser, loginPayload.password)
        case Left(error) =>
          toApiError(error)
      }
    }

  def me: Action[AnyContent] =
    AuthenticatedAction { implicit request =>
      ApiResponse {
        Right(request.user)
      }
    }

  def logout: Action[AnyContent] =
    Action { implicit request =>
      Redirect(routes.HomeController.index()).withoutAuthentication
    }

  private def loginUser(user: User, password: String): Result = {
    val session = UserSession(user, password)
    val token = createJwtSession(session)
    Ok(Json.obj(
      "token" -> token.refresh().serialize,
      "user" -> Json.toJson(user)
    )).withAuthentication(token)
  }

}
