package usecases.interactor

import cats.MonadError
import cats.implicits._

import domain.model.user.Auth
import domain.repositories.UserRepository
import services.{EncryptService, TokenService}
import usecases.errors.UseCaseApplicationError
import usecases.inputboundary.UseCase
import usecases.inputdata.SignInInputData
import usecases.outputdata.SignInOutputData

class SignInUseCase[F[_]](implicit
                          userRepository: UserRepository[F],
                          encryptService: EncryptService[F],
                          tokenService: TokenService[F]
                         ) extends UseCase[F, SignInInputData, SignInOutputData] {
  def execute(inputData: SignInInputData)(implicit ME: MonadError[F, UseCaseApplicationError]): F[SignInOutputData] =
    for {
      maybeUser     <- userRepository.findByEmail(inputData.email)
      user          <- maybeUser
        .map(user => ME.pure(user))
        .getOrElse(ME.raiseError(UseCaseApplicationError("User Process failing...")))
      matched          <- encryptService.matches(inputData.password.value, user.password.value)
      token            <-
        if(matched) {
          tokenService.generate(Auth(user.id))
        } else ME.raiseError(UseCaseApplicationError("User Process failing..."))
    } yield SignInOutputData(token, user.id)
}