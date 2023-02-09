package domain.model.user

import cats.ApplicativeError
import cats.data.NonEmptyChain

case class Name(value: String)
object Name {
  // Validation for value object Name.
  def validate[F[_, _]](value: String)(
    implicit AE: ApplicativeError[({ type L[A] = F[NonEmptyChain[String], A] })#L, NonEmptyChain[String]]
  ): F[NonEmptyChain[String], Name] = {
    if (value.nonEmpty) AE.pure(Name(value))
    else AE.raiseError(NonEmptyChain.one("AccountName should not be blank."))
  }
}