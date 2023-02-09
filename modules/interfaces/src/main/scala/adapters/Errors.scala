package adapters

import cats.{MonadError, StackSafeMonad}
import cats.effect.IO

import usecases.errors.UseCaseApplicationError

object Errors {
  implicit val useCaseMonadErrorForIO = new MonadError[IO, UseCaseApplicationError] with StackSafeMonad[IO] {
    override def pure[A](x: A): IO[A] = IO.pure(x)
    override def flatMap[A, B](fa: IO[A])(f: A => IO[B]): IO[B] = fa.flatMap(f)
    override def raiseError[A](e: UseCaseApplicationError): IO[A] = IO.raiseError(new Exception(e.toString))
    override def handleErrorWith[A](fa: IO[A])(f: UseCaseApplicationError => IO[A]): IO[A] = ???
  }
}