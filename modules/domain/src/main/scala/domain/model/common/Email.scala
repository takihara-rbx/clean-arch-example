package domain.model.common

import cats.ApplicativeError
import cats.data.NonEmptyChain

case class Email(value: String)
object Email {
  // Validation for value object Email.
  def validate[F[_, _]](value: String)(
    implicit AE: ApplicativeError[({ type L[A] = F[NonEmptyChain[String], A] })#L, NonEmptyChain[String]]
  ): F[NonEmptyChain[String], Email] = {
    if (validateEmailFormat(value)) AE.pure(Email(value))
    else AE.raiseError(NonEmptyChain.one("Email format is invalid."))
  }

  private def validateEmailFormat(value: String): Boolean =
    value.matches("[^@]+@[^@]+")
}