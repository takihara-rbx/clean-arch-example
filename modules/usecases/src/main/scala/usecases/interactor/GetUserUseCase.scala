package usecases.interactor

import cats.MonadError
import cats.implicits._
import domain.repositories.UserRepository
import usecases.errors.UseCaseApplicationError
import usecases.inputboundary.UseCase
import usecases.inputdata.GetUserInputData
import usecases.outputdata.GetUserOutputData

class GetUserUseCase[F[_]](implicit
                           userRepository: UserRepository[F]
                          ) extends UseCase[F, GetUserInputData, GetUserOutputData] {
  def execute(inputData: GetUserInputData)(implicit ME: MonadError[F, UseCaseApplicationError]): F[GetUserOutputData] =
    for {
      maybeUser <- userRepository.findById(inputData.userId)
      user <- maybeUser match {
        case Some(user) => ME.pure(user)
        case None       => ME.raiseError(UseCaseApplicationError("User Process failing..."))
      }
    } yield GetUserOutputData(user.id, user.name, user.email)
}