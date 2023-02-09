package adapters.http.json

import domain.model.user.Auth

case class DeleteUserRequestWithAuthJson(auth: Auth, userId: String)