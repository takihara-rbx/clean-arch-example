package adapters.gateway.services

import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import java.nio.charset.StandardCharsets
import java.util.Base64

import cats.effect.IO
import services.EncryptService

class EncryptServiceImpl extends EncryptService[IO] {

  private val SECRET_KEY = "secret_key_sample"
  private val SALT = "salt_sample"

  def matches(v0: String, v1: String): IO[Boolean] =
    IO.pure {
      try {
        val iv: Array[Byte] = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        val ivspec = new IvParameterSpec(iv)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = new PBEKeySpec(SECRET_KEY.toCharArray, SALT.getBytes, 65536, 256)
        val tmp = factory.generateSecret(spec)
        val secretKey = new SecretKeySpec(tmp.getEncoded, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec)
        val encrypted = Base64.getEncoder.encodeToString(cipher.doFinal(v0.getBytes(StandardCharsets.UTF_8)))
        encrypted == v1
      } catch {
        case e: Exception => false
      }
    }

  override def encrypt(plainTextPassword: String): IO[String] =
    IO.pure {
      try {
        val iv: Array[Byte] = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        val ivspec = new IvParameterSpec(iv)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = new PBEKeySpec(SECRET_KEY.toCharArray, SALT.getBytes, 65536, 256)
        val tmp = factory.generateSecret(spec)
        val secretKey = new SecretKeySpec(tmp.getEncoded, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec)
        Base64.getEncoder.encodeToString(cipher.doFinal(plainTextPassword.getBytes(StandardCharsets.UTF_8)))
      } catch {
        case e: Exception => throw new Exception("encryption failure!!")
      }
    }
}