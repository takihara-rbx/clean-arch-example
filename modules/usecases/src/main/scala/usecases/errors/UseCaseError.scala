package usecases.errors

sealed trait UseCaseError
case class UseCaseApplicationError(msg: String) extends UseCaseError