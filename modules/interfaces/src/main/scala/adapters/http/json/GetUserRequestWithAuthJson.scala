package adapters.http.json

import domain.model.user.Auth

case class GetUserRequestWithAuthJson(auth: Auth, userId: String)