package adapters.gateway.repositories

import cats.effect.IO
import doobie.implicits._
import domain.model.user.{Name, User, UserId}
import domain.model.common.Email
import domain.repositories.UserRepository
import adapters.gateway.repositories.support.doobie.DoobieSupport

class UserRepositoryImpl extends UserRepository[IO] with DoobieSupport {

  def findByEmail(email: Email): IO[Option[User]] =
    sql"select user_id, name, email, password from users where email = ${email.value}"
      .query[User]
      .option
      .transact(xa)

  def findById(userId: UserId): IO[Option[User]] =
    sql"select user_id, name, email, password from users where user_id = ${userId.value}"
      .query[User]
      .option
      .transact(xa)

  def save(user: User): IO[UserId] =
    sql"INSERT INTO users (user_id, name, email, password) VALUES (${user.id.value}, ${user.name.value}, ${user.email.value}, ${user.password.value})"
      .update
      .withUniqueGeneratedKeys[UserId]("user_id")
      .transact(xa)

  def updateName(userId: UserId, newName: Name): IO[UserId] =
    sql"UPDATE users SET name = ${newName.value} WHERE user_id = ${userId.value}"
      .update
      .withUniqueGeneratedKeys[UserId]("user_id")
      .transact(xa)

  def delete(userId: UserId): IO[UserId] =
    sql"DELETE FROM users WHERE user_id = ${userId.value}"
      .update
      .withUniqueGeneratedKeys[UserId]("user_id")
      .transact(xa)

}