package domain.model.user

import cats.ApplicativeError
import cats.data.NonEmptyChain

case class PlainTextPassword(value: String)
object PlainTextPassword {
  // Validation for value object PlainTextPassword.
  def validate[F[_, _]](value: String)(
    implicit AE: ApplicativeError[({ type L[A] = F[NonEmptyChain[String], A] })#L, NonEmptyChain[String]]
  ): F[NonEmptyChain[String], PlainTextPassword] = {
    if (value.nonEmpty) AE.pure(PlainTextPassword(value))
    else AE.raiseError(NonEmptyChain.one("PlainTextPassword should not be blank."))
  }
}