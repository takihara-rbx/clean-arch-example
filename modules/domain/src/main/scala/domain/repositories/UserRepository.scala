package domain.repositories

import domain.model.common.Email
import domain.model.user.{Name, User, UserId}

trait UserRepository[F[_]] {
  def findByEmail(email: Email): F[Option[User]]
  def findById(id: UserId): F[Option[User]]
  def save(user: User): F[UserId]
  def updateName(userId: UserId, newName: Name): F[UserId]
  def delete(id: UserId): F[UserId]
}