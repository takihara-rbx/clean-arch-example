package usecases.interactor

import cats.MonadError
import cats.implicits._
import domain.repositories.UserRepository
import usecases.errors.UseCaseApplicationError
import usecases.inputboundary.UseCase
import usecases.inputdata.DeleteUserInputData
import usecases.outputdata.DeleteUserOutputData


class DeleteUserUseCase[F[_]](implicit
                              userRepository: UserRepository[F]
                             ) extends UseCase[F, DeleteUserInputData, DeleteUserOutputData] {
  def execute(inputData: DeleteUserInputData)(implicit ME: MonadError[F, UseCaseApplicationError]): F[DeleteUserOutputData] =
    for {
      userId <- userRepository.delete(inputData.userId)
    } yield DeleteUserOutputData(userId)
}