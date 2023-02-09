package adapters.http.controllers

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.effect.IO
import cats.effect.unsafe.IORuntime
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import wvlet.airframe.bind
import adapters.http.directives.{AuthDirectives, ValidateDirectives}
import adapters.http.json.{DeleteUserRequestWithAuthJson, GetUserRequestWithAuthJson, SignInRequestJson, SignUpRequestJson, UpdateUserRequestJson, UpdateUserRequestWithAuthJson}
import adapters.http.jsonSupport.{DeleteUserJsonSupport, GetUserJsonSupport, SignInJsonSupport, SignUpJsonSupport, UpdateUserJsonSupport}
import adapters.http.presenters.{DeleteUserPresenter, GetUserPresenter, SignInPresenter, SignUpPresenter, UpdateUserPresenter}
import services.TokenService
import usecases.interactor.{DeleteUserUseCase, GetUserUseCase, SignInUseCase, SignUpUseCase, UpdateUserUseCase}

trait Controller extends SignInJsonSupport
  with SignUpJsonSupport
  with GetUserJsonSupport
  with UpdateUserJsonSupport
  with DeleteUserJsonSupport {

  import ValidateDirectives._
  import adapters.Errors._

  implicit val runtime: IORuntime = cats.effect.unsafe.implicits.global

  private implicit val tokenService: TokenService[IO] = bind[TokenService[IO]]

  private val signInUseCase   = bind[SignInUseCase[IO]]
  private val signInPresenter = bind[SignInPresenter]

  private val signUpUseCase   = bind[SignUpUseCase[IO]]
  private val signUpPresenter = bind[SignUpPresenter]

  private val getUserUseCase   = bind[GetUserUseCase[IO]]
  private val getUserPresenter = bind[GetUserPresenter]

  private val updateUserUseCase   = bind[UpdateUserUseCase[IO]]
  private val updateUserPresenter = bind[UpdateUserPresenter]

  private val deleteUserUseCase = bind[DeleteUserUseCase[IO]]
  private val deleteUserPresenter = bind[DeleteUserPresenter]

  val routes: Route =
    cors(){
      Route.seal {
        concat( signIn, signUp, getUser, updateUser, deleteUser )
      }
    }

  private def signIn: Route =
    path("api" / "auth" / "sign_in") {
      post {
        entity(as[SignInRequestJson]) { json =>
          ValidateDirectives.validateJsonRequest(json).apply { inputData =>
            signInPresenter.convert(
              signInUseCase.execute(inputData).unsafeRunSync()
            )
          }
        }
      }
    }

  private def signUp: Route =
    path("api" / "auth" / "sign_up") {
      post {
        entity(as[SignUpRequestJson]) { json =>
          ValidateDirectives.validateJsonRequest(json).apply { inputData =>
            signUpPresenter.convert(
              signUpUseCase.execute(inputData).unsafeRunSync()
            )
          }
        }
      }
    }

  private def getUser: Route =
    path("api" / "user" / Segment ) { userId =>
      get {
        AuthDirectives.validateAuth.apply { auth =>
          AuthDirectives.authorized(auth, userId) {
            ValidateDirectives.validateJsonRequest(GetUserRequestWithAuthJson(auth, userId)).apply { inputData =>
              getUserPresenter.convert(
                getUserUseCase.execute(inputData).unsafeRunSync()
              )
            }
          }
        }
      }
    }

  private def updateUser: Route =
    path("api" / "user" / Segment) { userId =>
      post {
        AuthDirectives.validateAuth.apply { auth =>
          AuthDirectives.authorized(auth, userId) {
            entity(as[UpdateUserRequestJson]) { json =>
              ValidateDirectives.validateJsonRequest(UpdateUserRequestWithAuthJson(auth, userId, json)).apply { inputData =>
                updateUserPresenter.convert(
                  updateUserUseCase.execute(inputData).unsafeRunSync()
                )
              }
            }
          }
        }
      }
    }

  private def deleteUser: Route =
    path("api" / "user" / Segment) { userId =>
      delete {
        AuthDirectives.validateAuth.apply { auth =>
          AuthDirectives.authorized(auth, userId) {
            ValidateDirectives.validateJsonRequest(DeleteUserRequestWithAuthJson(auth, userId)).apply { inputData =>
              deleteUserPresenter.convert(
                deleteUserUseCase.execute(inputData).unsafeRunSync()
              )
            }
          }
        }
      }
    }

}