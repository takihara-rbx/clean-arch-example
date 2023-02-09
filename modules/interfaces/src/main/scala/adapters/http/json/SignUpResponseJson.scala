package adapters.http.json

case class SignUpResponseJson(id: Option[String], error_message: Seq[String] = Seq.empty)