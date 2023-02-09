package adapters.http.presenters

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

import adapters.http.json.GetUserResponseJson
import adapters.http.jsonSupport.GetUserJsonSupport
import usecases.outputdata.GetUserOutputData

trait GetUserPresenter extends GetUserJsonSupport {
  def convert(getUserOutputData: GetUserOutputData): Route =
    complete(
      GetUserResponseJson(getUserOutputData.id.value, getUserOutputData.name.value, getUserOutputData.email.value)
    )
}