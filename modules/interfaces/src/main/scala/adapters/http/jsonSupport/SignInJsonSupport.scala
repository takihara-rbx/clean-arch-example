package adapters.http.jsonSupport

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

import adapters.http.json.{SignInRequestJson, SignInResponseJson}

trait SignInJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val signInJsonRequestFormat  = jsonFormat2(SignInRequestJson)
  implicit val signInJsonResponseFormat = jsonFormat2(SignInResponseJson)
}