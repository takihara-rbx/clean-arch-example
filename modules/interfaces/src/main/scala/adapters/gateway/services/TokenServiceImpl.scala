package adapters.gateway.services

import cats.effect.IO
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.time.{Instant, ZoneId, ZonedDateTime}
import java.util.{Date, UUID}
import scala.jdk.CollectionConverters._

import domain.model.user.{UserId, Auth}
import services.TokenService

class TokenServiceImpl extends TokenService[IO] {

  private val algorithm: Algorithm = Algorithm.HMAC256("secret")

  private def nowRoundedToSecond: ZonedDateTime = {
    val nowZdt = ZonedDateTime.now(ZoneId.of("UTC"))
    ZonedDateTime.ofInstant(Instant.ofEpochSecond(nowZdt.toEpochSecond), nowZdt.getZone)
  }

  def generate(auth: Auth): IO[String] = {
    val headerClaims = Map[String, AnyRef]("typ" -> "JWT", "cty" -> "JWT").asJava

    val nowZdt = nowRoundedToSecond
    val expiresZdt = nowZdt.plusSeconds(1800)
    val nowDate = Date.from(nowZdt.toInstant)
    val expiresDate = Date.from(expiresZdt.toInstant)
    val jti = UUID.randomUUID().toString

    IO.pure(
      JWT
        .create()
        .withHeader(headerClaims)
        .withIssuer("sample")
        .withAudience("sample")
        .withSubject(auth.userId.value)
        .withJWTId(jti)
        .withIssuedAt(nowDate)
        .withExpiresAt(expiresDate)
        .withClaim("user_id", auth.userId.value)
        .sign(algorithm)
    )
  }

  def verify(token: String, acceptExpiresAt: Long): IO[Auth] = {
    IO.pure(
      Auth(
        UserId(
          JWT
            .require(algorithm)
            .acceptLeeway(1)
            .acceptExpiresAt(acceptExpiresAt)
            .withIssuer("sample")
            .withAudience("sample")
            .build()
            .verify(token)
            .getSubject
        )
      )
    )
  }
}