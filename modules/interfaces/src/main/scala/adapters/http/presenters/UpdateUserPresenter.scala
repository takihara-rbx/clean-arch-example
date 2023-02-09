package adapters.http.presenters

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

import adapters.http.json.UpdateUserResponseJson
import adapters.http.jsonSupport.UpdateUserJsonSupport
import usecases.outputdata.UpdateUserOutputData

trait UpdateUserPresenter extends UpdateUserJsonSupport {
  def convert(updateUserOutputData: UpdateUserOutputData): Route =
    complete(
      UpdateUserResponseJson(updateUserOutputData.userId.value)
    )
}