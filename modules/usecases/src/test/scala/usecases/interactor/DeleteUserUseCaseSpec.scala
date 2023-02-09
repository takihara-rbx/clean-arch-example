package usecases.interactor

import cats.{Id, MonadError, StackSafeMonad}
import domain.model.common.Email
import domain.model.user.{Auth, Name, User, UserId}
import domain.repositories.UserRepository
import org.scalatest.flatspec.AnyFlatSpec
import usecases.errors.UseCaseApplicationError
import usecases.inputdata.DeleteUserInputData
import usecases.outputdata.DeleteUserOutputData

class DeleteUserUseCaseSpec extends AnyFlatSpec {
  val userId     = UserId("tu01")

  implicit def userRepository = new UserRepository[Id] {
    def findById(id: UserId): Id[Option[User]] = ???
    def findByEmail(email: Email): Id[Option[User]] = ???
    def save(user: User): Id[UserId] = ???
    def updateName(userId: UserId, newName: Name): Id[UserId] = userId
    def delete(id: UserId): Id[UserId] = id
  }

  implicit val idMonadError: MonadError[Id, UseCaseApplicationError] = new MonadError[Id, UseCaseApplicationError] with StackSafeMonad[Id] {
    override def pure[A](x: A): Id[A] = x
    override def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa)
    override def raiseError[A](e: UseCaseApplicationError): Id[A] = throw new Exception(e.toString)
    override def handleErrorWith[A](fa: Id[A])(f: UseCaseApplicationError => Id[A]): Id[A] = ???
  }

  val deleteUserUseCase = new DeleteUserUseCase[Id]

  "DeleteUserUseCase" should "be success" in {
    assert(
      deleteUserUseCase.execute(DeleteUserInputData(Auth(userId), userId)) === DeleteUserOutputData(userId)
    )
  }
}