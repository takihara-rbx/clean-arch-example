package usecases.inputdata

import domain.model.common.Email
import domain.model.user.PlainTextPassword

case class SignInInputData(email: Email, password: PlainTextPassword)
