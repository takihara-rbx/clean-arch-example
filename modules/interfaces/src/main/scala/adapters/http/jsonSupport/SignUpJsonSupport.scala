package adapters.http.jsonSupport

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

import adapters.http.json.{SignUpRequestJson, SignUpResponseJson}

trait SignUpJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val signUpJsonRequestFormat  = jsonFormat4(SignUpRequestJson)
  implicit val signUpJsonResponseFormat = jsonFormat2(SignUpResponseJson)
}