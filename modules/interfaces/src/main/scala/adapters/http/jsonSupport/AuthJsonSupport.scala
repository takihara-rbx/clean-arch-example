package adapters.http.jsonSupport

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

import domain.model.user.{UserId, Auth}

trait AuthJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userIdJsonFormat = jsonFormat1(UserId)
  implicit val authJsonFormat   = jsonFormat1(Auth)
}