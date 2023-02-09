package usecases.interactor

import cats.MonadError
import cats.implicits._
import domain.model.user.{EncryptedPassword, User}
import domain.repositories.UserRepository
import services.EncryptService
import usecases.errors.UseCaseApplicationError
import usecases.inputboundary.UseCase
import usecases.inputdata.SignUpInputData
import usecases.outputdata.SignUpOutputData

class SignUpUseCase[F[_]]( implicit
                           userRepository: UserRepository[F],
                           encryptService: EncryptService[F]
                         ) extends UseCase[F, SignUpInputData, SignUpOutputData] {
  def execute(inputData: SignUpInputData)(implicit ME: MonadError[F, UseCaseApplicationError]): F[SignUpOutputData] =
    for {
      maybeAccount <- userRepository.findByEmail(inputData.email)
      generated    <- maybeAccount match {
        case None =>
          for {
            encrypted_pass <- encryptService.encrypt(inputData.password.value)
            generated_user <- User.generate[F](inputData.userId, inputData.name, inputData.email, EncryptedPassword(encrypted_pass))
            _ <- userRepository.save(generated_user)
          } yield generated_user
        case Some(user) =>
          ME.raiseError[User](UseCaseApplicationError(s"user which use ${user.email.value} already exists"))
      }
    } yield SignUpOutputData(generated.id)
}