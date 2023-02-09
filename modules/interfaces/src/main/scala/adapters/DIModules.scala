package adapters

import cats.effect.IO
import wvlet.airframe.{Design, newDesign}

import adapters.gateway.repositories.UserRepositoryImpl
import adapters.gateway.services.{EncryptServiceImpl, TokenServiceImpl}
import domain.repositories.UserRepository
import services.{EncryptService, TokenService}

trait DIModules {
  private[adapters] def designOfRepositories: Design =
    newDesign
      .bind[UserRepository[IO]].to[UserRepositoryImpl]

  private[adapters] def designOfServices: Design =
    newDesign
      .bind[EncryptService[IO]].to[EncryptServiceImpl]
      .bind[TokenService[IO]].to[TokenServiceImpl]

  def designs: Design =
    newDesign
      .add(designOfRepositories)
      .add(designOfServices)
}
object DIModules extends DIModules