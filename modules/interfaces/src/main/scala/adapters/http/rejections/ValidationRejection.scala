package adapters.http.rejections

import akka.http.javadsl.server.CustomRejection
import cats.data.NonEmptyChain

case class ValidationRejection(errors: NonEmptyChain[String]) extends CustomRejection {
  val message: String = errors.toChain.toList.mkString(",")
}
