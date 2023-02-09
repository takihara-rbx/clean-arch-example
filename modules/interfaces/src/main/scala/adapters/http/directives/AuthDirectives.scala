package adapters.http.directives

import akka.http.scaladsl.model.headers.{HttpChallenge, OAuth2BearerToken}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, Directive1}
import akka.http.scaladsl.server.directives.AuthenticationResult
import cats.effect.IO
import cats.effect.unsafe.IORuntime
import scala.concurrent.Future

import domain.model.user.Auth
import services.TokenService

trait AuthDirectives {
  def validateAuth(implicit tokenService: TokenService[IO], runtime: IORuntime): Directive1[Auth] =
    authenticateOrRejectWithChallenge[OAuth2BearerToken, Auth] {
      case Some(bearerToken) =>
        tokenService.verify(bearerToken.token, 1800)
          .map(AuthenticationResult.success)
          .unsafeToFuture()
      case None =>
        Future.successful(
          AuthenticationResult.failWithChallenge(HttpChallenge("bearer", "Application or System Error", Map("error" -> "invalid_token")))
        )
    }

  def authorized(auth: Auth, accountId: String): Directive0 = {
    authorize(auth.userId.value == accountId)
  }
}

object AuthDirectives extends AuthDirectives