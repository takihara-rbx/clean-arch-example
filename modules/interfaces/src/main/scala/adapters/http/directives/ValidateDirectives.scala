package adapters.http.directives

import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives._
import cats.ApplicativeError
import cats.data.{NonEmptyChain, Validated}
import cats.data.Validated._
import adapters.http.json.{DeleteUserRequestWithAuthJson, GetUserRequestWithAuthJson, SignInRequestJson, SignUpRequestJson, UpdateUserRequestWithAuthJson}
import adapters.http.rejections.ValidationRejection
import adapters.validator.Validator
import domain.model.user.{Auth, Name, PlainTextPassword, UserId}
import domain.model.common.Email
import usecases.inputdata.{DeleteUserInputData, GetUserInputData, SignInInputData, SignUpInputData, UpdateUserInputData}

trait ValidateDirectives {
  def validateJsonRequest[A, B](jsonRequest: A)(implicit V: Validator[A, B]): Directive1[B] =
    V.validate(jsonRequest)
      .fold(errors => {
        reject(ValidationRejection(errors))
      }, provide)
}
object ValidateDirectives extends ValidateDirectives {

  implicit object SignInValidator extends Validator[SignInRequestJson, SignInInputData] {
    def validate(value: SignInRequestJson)(
      implicit AE: ApplicativeError[({type L[A] = Validated[NonEmptyChain[String], A]})#L, NonEmptyChain[String]]
    ): Validated[NonEmptyChain[String], SignInInputData] =
      AE.map2(
        Email.validate[Validated](value.email),
        PlainTextPassword.validate[Validated](value.password)
      )(SignInInputData.apply)
  }

  implicit object SignUpValidator extends Validator[SignUpRequestJson, SignUpInputData] {
    override def validate(value: SignUpRequestJson)(
      implicit AE: ApplicativeError[({type L[A] = Validated[NonEmptyChain[String], A]})#L, NonEmptyChain[String]]
    ): Validated[NonEmptyChain[String], SignUpInputData] =
      AE.map4(
        AE.pure[UserId](UserId(value.userId)),
        Name.validate[Validated](value.name),
        Email.validate[Validated](value.email),
        PlainTextPassword.validate[Validated](value.password)
      )(SignUpInputData.apply)
  }

  implicit object GetUserValidator extends Validator[GetUserRequestWithAuthJson, GetUserInputData] {
    override def validate(value: GetUserRequestWithAuthJson)(
      implicit AE: ApplicativeError[({type L[A] = Validated[NonEmptyChain[String], A]})#L, NonEmptyChain[String]]
    ): Validated[NonEmptyChain[String], GetUserInputData] =
      AE.map2(
        AE.pure[Auth](value.auth),
        AE.pure[UserId](UserId(value.userId))
      )(GetUserInputData.apply)
  }

  implicit object UpdateUserValidator extends Validator[UpdateUserRequestWithAuthJson, UpdateUserInputData] {
    override def validate(value: UpdateUserRequestWithAuthJson)(
      implicit AE: ApplicativeError[({type L[A] = Validated[NonEmptyChain[String], A]})#L, NonEmptyChain[String]]
    ): Validated[NonEmptyChain[String], UpdateUserInputData] =
      AE.map3(
        AE.pure[Auth](value.auth),
        AE.pure[UserId](UserId(value.userId)),
        Name.validate[Validated](value.request.newName)
      )(UpdateUserInputData.apply)
  }

  implicit object DeleteUserValidator extends Validator[DeleteUserRequestWithAuthJson, DeleteUserInputData] {
    override def validate(value: DeleteUserRequestWithAuthJson)(
      implicit AE: ApplicativeError[({type L[A] = Validated[NonEmptyChain[String], A]})#L, NonEmptyChain[String]]
    ): Validated[NonEmptyChain[String], DeleteUserInputData] =
      AE.map2(
        AE.pure[Auth](value.auth),
        AE.pure[UserId](UserId(value.userId))
      )(DeleteUserInputData.apply)
  }
}