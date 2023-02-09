package adapters.http.jsonSupport

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

import adapters.http.json.{DeleteUserRequestWithAuthJson, DeleteUserResponseJson}

trait DeleteUserJsonSupport extends SprayJsonSupport with DefaultJsonProtocol with AuthJsonSupport {
  implicit val deleteUserJsonRequestFormat  = jsonFormat2(DeleteUserRequestWithAuthJson)
  implicit val deleteUserJsonResponseFormat = jsonFormat1(DeleteUserResponseJson)
}