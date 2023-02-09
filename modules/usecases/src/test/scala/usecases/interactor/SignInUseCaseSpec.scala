package usecases.interactor

import cats.{Id, MonadError, StackSafeMonad}
import org.scalatest.flatspec.AnyFlatSpec

import domain.model.user.{User, UserId, Auth, EncryptedPassword, Name, PlainTextPassword}
import domain.model.common.Email
import domain.repositories.UserRepository
import services.{EncryptService, TokenService}
import usecases.errors.UseCaseApplicationError
import usecases.inputdata.SignInInputData
import usecases.outputdata.SignInOutputData

class SignInUseCaseSpec extends AnyFlatSpec {

  implicit def userRepository: UserRepository[Id] = new UserRepository[Id] {
    def findByEmail(email: Email): Id[Option[User]] = email match {
      case Email("test@example.com") =>
        Some(User(UserId("tu01"), Name("Test User"), Email("test@example.com"), EncryptedPassword("password1234")))
      case _ =>
        None
    }
    def findById(id: UserId): Id[Option[User]] = ???
    def save(user: User): Id[UserId] = ???
    def updateName(userId: UserId, newName: Name): Id[UserId] = ???
    def delete(id: UserId): Id[UserId] = ???
  }

  implicit def encryptService: EncryptService[Id] = new EncryptService[Id] {
    def matches(v0: String, v1: String): Id[Boolean] = v0 == v1
    def encrypt(plainTextPassword: String): Id[String] = ???
  }

  implicit def tokenService: TokenService[Id] = new TokenService[Id] {
    def generate(auth: Auth): Id[String] = auth.userId.value * 3
    def verify(token: String, acceptExpiresAt: Long): Id[Auth] = token match {
      case "tu01tu01tu01" => Auth(UserId("tu01"))
    }
  }

  implicit val idMonadError: MonadError[Id, UseCaseApplicationError] = new MonadError[Id, UseCaseApplicationError] with StackSafeMonad[Id] {
    override def pure[A](x: A): Id[A] = x
    override def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa)
    override def raiseError[A](e: UseCaseApplicationError): Id[A] = throw new Exception(e.toString)
    override def handleErrorWith[A](fa: Id[A])(f: UseCaseApplicationError => Id[A]): Id[A] = ???
  }

  val signInUseCase: SignInUseCase[Id] = new SignInUseCase[Id]

  "SignInUseCase" should "be success" in {
    assert(
      signInUseCase.execute(SignInInputData(Email("test@example.com"), PlainTextPassword("password1234"))) ===
        SignInOutputData("tu01tu01tu01", UserId("tu01"))
    )
  }

  it should "be exception because the user is not existed." in {
    assertThrows[Exception](
      signInUseCase.execute(SignInInputData(Email("dammy@example.com"), PlainTextPassword("dammypass")))
    )
  }

}