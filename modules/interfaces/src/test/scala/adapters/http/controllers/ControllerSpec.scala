package adapters.http.controllers

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.model.headers.OAuth2BearerToken
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.flatspec.AnyFlatSpec

import java.nio.charset.StandardCharsets
import wvlet.airframe.{Design, Session}
import adapters.DIModules
import adapters.http.json.{DeleteUserResponseJson, GetUserResponseJson, SignInResponseJson, SignUpResponseJson, UpdateUserResponseJson}
import adapters.http.jsonSupport.{DeleteUserJsonSupport, GetUserJsonSupport, SignInJsonSupport, SignUpJsonSupport, UpdateUserJsonSupport}

class ControllerSpec extends AnyFlatSpec with ScalatestRouteTest
  with SignInJsonSupport
  with SignUpJsonSupport
  with GetUserJsonSupport
  with UpdateUserJsonSupport
  with DeleteUserJsonSupport {

  val design: Design         = DIModules.designs
  val session: Session       = design.newSession
  val controller: Controller = session.build[Controller]

  def signUpAndSignIn(userId: String, name: String, email: String, password: String): (String, String) = {

    val signUpData: Array[Byte] =
      raw"""{"userId":"$userId", "name":"$name", "email":"$email", "password":"$password"}"""
        .getBytes(StandardCharsets.UTF_8)
    Post("/api/auth/sign_up", HttpEntity(ContentTypes.`application/json`, signUpData)) ~> controller.routes ~> check {
      assert(response.status === StatusCodes.OK)

      val signUpResponseJson = responseAs[SignUpResponseJson]
      assert(signUpResponseJson.error_message.isEmpty)
      assert(signUpResponseJson.id.get === userId)
    }

    val signInData: Array[Byte] =
      raw"""{"email":"$email","password":"$password"}""".getBytes(StandardCharsets.UTF_8)
    val (token, signed_userId) = Post("/api/auth/sign_in", HttpEntity(ContentTypes.`application/json`, signInData)) ~> controller.routes ~> check {
      assert(response.status === StatusCodes.OK)
      val responseJson = responseAs[SignInResponseJson]

      val maybeToken = responseJson.token
      assert(maybeToken.isDefined === true)

      val signed_userId = responseJson.userId

      (maybeToken.get, signed_userId)
    }
    (token, signed_userId)
  }

  def updateUser(userId: String, newName: String) = {
    // Update User
    val updateUserData: Array[Byte] =
      raw"""{"newName":"$newName"}""".getBytes(StandardCharsets.UTF_8)
    Post(s"/api/user/$userId", HttpEntity(ContentTypes.`application/json`, updateUserData))
      .addCredentials(OAuth2BearerToken(token)) ~> controller.routes ~> check {
      assert(response.status === StatusCodes.OK)

      val updateUserResponseJson = responseAs[UpdateUserResponseJson]
      assert(updateUserResponseJson.userId === userId)
    }
  }

  val userId: String = "testUser01"
  val name: String = "Test User"
  val email: String = "test01@example.com"
  val password: String = "password1234"

  val newName: String = "NewName User"

  // SignUp and SignIn
  val (token, signedUserId) = signUpAndSignIn(userId, name, email, password)

  // Update User
  "UpdateUser" should "be success." in {
    updateUser(signedUserId, newName)
  }

  // Get User
  "GetUser" should "be success." in {
    Get(s"/api/user/$userId")
      .addCredentials(OAuth2BearerToken(token)) ~> controller.routes ~> check {
      assert(response.status === StatusCodes.OK)

      val getUserResponseJson = responseAs[GetUserResponseJson]
      assert(getUserResponseJson.userId === userId)
      assert(getUserResponseJson.email === email)
      assert(getUserResponseJson.name === newName)
    }
  }

  // Delete User
  "DeleteUser" should "be success." in {
    Delete(s"/api/user/$userId")
      .addCredentials(OAuth2BearerToken(token)) ~> controller.routes ~> check {
      assert(response.status === StatusCodes.OK)

      val deleteUserResponseJson = responseAs[DeleteUserResponseJson]
      assert(deleteUserResponseJson.userId === userId)
    }
  }

  // SignIn Failure1: email and password must not be blank.
  "SignIn" should "be failure because email and password is bad." in {
    val signInData_fail1: Array[Byte] =
      raw"""{"email":"","password":""}""".getBytes(StandardCharsets.UTF_8)
    Post("/api/auth/sign_in", HttpEntity(ContentTypes.`application/json`, signInData_fail1)) ~> controller.routes ~> check {
      assert(response.status === StatusCodes.InternalServerError)
    }
  }

}