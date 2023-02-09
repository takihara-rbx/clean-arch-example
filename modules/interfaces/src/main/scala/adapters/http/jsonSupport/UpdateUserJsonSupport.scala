package adapters.http.jsonSupport

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import adapters.http.json.{UpdateUserRequestJson, UpdateUserRequestWithAuthJson, UpdateUserResponseJson}

trait UpdateUserJsonSupport extends SprayJsonSupport with DefaultJsonProtocol with AuthJsonSupport {
  implicit val updateUserJsonRequest = jsonFormat1(UpdateUserRequestJson)
  implicit val updateUserJsonRequestWithAuthFormat  = jsonFormat3(UpdateUserRequestWithAuthJson)
  implicit val updateUserJsonResponseFormat = jsonFormat1(UpdateUserResponseJson)
}