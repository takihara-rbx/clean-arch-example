package services

import domain.model.user.EncryptedPassword

trait EncryptService[F[_]] {
  def matches(v0: String, v1: String): F[Boolean]
  def encrypt(plainTextPassword: String): F[String]
}