package adapters.validator

import cats.ApplicativeError
import cats.data.{NonEmptyChain, Validated}

trait Validator[A, B] {
  def validate(value: A)(
    implicit AE: ApplicativeError[({type L[A] = Validated[NonEmptyChain[String], A]})#L, NonEmptyChain[String]]
  ): Validated[NonEmptyChain[String], B]
}
