package adapters.http.presenters

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

import adapters.http.json.SignInResponseJson
import adapters.http.jsonSupport.SignInJsonSupport
import usecases.outputdata.SignInOutputData

trait SignInPresenter extends SignInJsonSupport {
  def convert(signInOutputData: SignInOutputData): Route =
    complete(SignInResponseJson(Some(signInOutputData.token), signInOutputData.userId.value))
}