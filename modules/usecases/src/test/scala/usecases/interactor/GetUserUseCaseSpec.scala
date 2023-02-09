package usecases.interactor

import cats.{Id, MonadError, StackSafeMonad}
import domain.model.common.Email
import domain.model.user.{Auth, EncryptedPassword, Name, User, UserId}
import domain.repositories.UserRepository
import org.scalatest.flatspec.AnyFlatSpec
import usecases.errors.UseCaseApplicationError
import usecases.inputdata.GetUserInputData
import usecases.outputdata.GetUserOutputData

class GetUserUseCaseSpec extends AnyFlatSpec {

  val userId   = UserId("tu01")
  val name     = Name("Test User")
  val email    = Email("test@example.com")
  val password = EncryptedPassword("somethingSomething")

  implicit def userRepository = new UserRepository[Id] {
    override def findById(id: UserId): Id[Option[User]] =
      if(id.value == "tu01") {
        Some(User(userId, name, email, password))
      } else {
        None
      }
    override def findByEmail(email: Email): Id[Option[User]] = ???
    override def save(user: User): Id[UserId] = ???
    override def updateName(userId: UserId, newName: Name): Id[UserId] = ???
    override def delete(id: UserId): Id[UserId] = ???
  }

  implicit val idMonadError: MonadError[Id, UseCaseApplicationError] = new MonadError[Id, UseCaseApplicationError] with StackSafeMonad[Id] {
    override def pure[A](x: A): Id[A] = x
    override def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa)
    override def raiseError[A](e: UseCaseApplicationError): Id[A] = throw new Exception(e.toString)
    override def handleErrorWith[A](fa: Id[A])(f: UseCaseApplicationError => Id[A]): Id[A] = ???
  }

  val getUserUseCase = new GetUserUseCase[Id]

  "GetUserUseCase" should "be success" in {
    assert(
      getUserUseCase.execute(GetUserInputData(Auth(userId), userId)) === GetUserOutputData(userId, name, email)
    )
  }

}