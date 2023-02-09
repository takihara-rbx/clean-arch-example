package adapters.http.json

case class SignInResponseJson(token: Option[String], userId: String)