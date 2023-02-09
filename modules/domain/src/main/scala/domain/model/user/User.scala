package domain.model.user

import cats.Monad
import domain.model.common.Email

case class User(id: UserId, name: Name, email: Email, password: EncryptedPassword)
object User {
  def generate[M[_]](userId: UserId, name: Name, email: Email, password: EncryptedPassword)(implicit M: Monad[M]): M[User] =
    M.pure( User(userId, name, email, password) )
}