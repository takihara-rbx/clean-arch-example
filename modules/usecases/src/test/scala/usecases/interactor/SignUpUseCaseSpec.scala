package usecases.interactor

import cats.{Id, MonadError, StackSafeMonad}
import org.scalatest.flatspec.AnyFlatSpec

import domain.model.common.Email
import domain.model.user.{Name, PlainTextPassword, User, UserId}
import domain.repositories.UserRepository
import services.EncryptService
import usecases.errors.UseCaseApplicationError
import usecases.inputdata.SignUpInputData
import usecases.outputdata.SignUpOutputData

class SignUpUseCaseSpec extends AnyFlatSpec {

  implicit def userRepository = new UserRepository[Id] {
    def findByEmail(email: Email): Id[Option[User]] = None
    def save(user: User): Id[UserId] = user.id
    def findById(id: UserId): Id[Option[User]] = ???
    def updateName(userId: UserId, newName: Name): Id[UserId] = ???
    def delete(id: UserId): Id[UserId] = ???
  }

  implicit def encryptService = new EncryptService[Id] {
    def encrypt(plainTextPassword: String): Id[String] = plainTextPassword * 3
    def matches(v0: String, v1: String): Id[Boolean] = ???
  }

  implicit val idMonadError: MonadError[Id, UseCaseApplicationError] = new MonadError[Id, UseCaseApplicationError] with StackSafeMonad[Id] {
    override def pure[A](x: A): Id[A] = x
    override def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa)
    override def raiseError[A](e: UseCaseApplicationError): Id[A] = throw new Exception(e.toString)
    override def handleErrorWith[A](fa: Id[A])(f: UseCaseApplicationError => Id[A]): Id[A] = ???
  }

  val signUpUseCase: SignUpUseCase[Id] = new SignUpUseCase[Id]

  val userId   = UserId("newuser")
  val name     = Name("hoge")
  val email    = Email("newuser@example.com")
  val password = PlainTextPassword("password4321")

  "SignUpUseCase" should "be success" in {
    assert(
      signUpUseCase.execute(SignUpInputData(userId, name, email, password)) === SignUpOutputData(userId)
    )
  }

}