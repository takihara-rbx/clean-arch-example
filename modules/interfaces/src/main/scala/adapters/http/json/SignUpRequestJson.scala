package adapters.http.json

/**
 *
 * @param userId
 * @param name : UserName
 * @param email
 * @param password
 */
case class SignUpRequestJson(userId: String, name: String, email: String, password: String)