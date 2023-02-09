package services

import domain.model.user.Auth

trait TokenService[F[_]] {
  def generate(auth: Auth): F[String]
  def verify(token: String, acceptExpiresAt: Long): F[Auth]
}