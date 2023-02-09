package adapters.gateway.repositories.support.doobie

import cats.effect.IO
import doobie.Transactor

trait DoobieSupport {
  implicit val ior = cats.effect.unsafe.IORuntime

  lazy val user: String = System.getenv("SCALA_ENV") match {
    case "production" => "production"
    case "staging"    => "staging"
    case _            => "kodai"
  }

  val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql:clean_arch_example_db",
    user,
    ""
  )
}
