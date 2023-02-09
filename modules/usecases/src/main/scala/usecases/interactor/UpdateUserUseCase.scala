package usecases.interactor

import cats.MonadError
import cats.implicits._
import domain.repositories.UserRepository
import usecases.errors.UseCaseApplicationError
import usecases.inputboundary.UseCase
import usecases.inputdata.UpdateUserInputData
import usecases.outputdata.UpdateUserOutputData

class UpdateUserUseCase[F[_]](implicit
                              userRepository: UserRepository[F]
                             ) extends UseCase[F, UpdateUserInputData, UpdateUserOutputData] {
  def execute(inputData: UpdateUserInputData)(implicit ME: MonadError[F, UseCaseApplicationError]): F[UpdateUserOutputData] =
    for {
      maybeUser <- userRepository.findById(inputData.userId)
      newUserId <- maybeUser match {
        case Some(user) =>
          userRepository.updateName(user.id, inputData.newName)
        case None =>
          ME.raiseError(UseCaseApplicationError("Update UserId failed..."))
      }
    } yield UpdateUserOutputData(newUserId)

}