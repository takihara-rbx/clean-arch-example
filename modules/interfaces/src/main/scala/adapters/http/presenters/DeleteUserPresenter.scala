package adapters.http.presenters

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

import adapters.http.json.DeleteUserResponseJson
import adapters.http.jsonSupport.DeleteUserJsonSupport
import usecases.outputdata.DeleteUserOutputData

trait DeleteUserPresenter extends DeleteUserJsonSupport {
  def convert(deleteUserOutputData: DeleteUserOutputData): Route =
    complete(
      DeleteUserResponseJson(deleteUserOutputData.id.value)
    )
}