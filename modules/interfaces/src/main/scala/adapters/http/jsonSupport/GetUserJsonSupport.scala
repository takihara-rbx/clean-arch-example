package adapters.http.jsonSupport

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

import adapters.http.json.{GetUserRequestWithAuthJson, GetUserResponseJson}

trait GetUserJsonSupport extends SprayJsonSupport with DefaultJsonProtocol with AuthJsonSupport {
  implicit val getUserJsonRequestFormat  = jsonFormat2(GetUserRequestWithAuthJson)
  implicit val getUserJsonResponseFormat = jsonFormat3(GetUserResponseJson)
}