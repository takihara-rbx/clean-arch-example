package adapters.http.json

import domain.model.user.Auth

case class UpdateUserRequestJson(newName: String)
case class UpdateUserRequestWithAuthJson(auth: Auth, userId: String, request: UpdateUserRequestJson)