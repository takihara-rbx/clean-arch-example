package adapters.http.presenters

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import adapters.http.json.SignUpResponseJson
import adapters.http.jsonSupport.SignUpJsonSupport
import usecases.outputdata.SignUpOutputData

trait SignUpPresenter extends SignUpJsonSupport {
  def convert(signUpOutputData: SignUpOutputData): Route =
    complete(SignUpResponseJson(Some(signUpOutputData.userId.value)))
}