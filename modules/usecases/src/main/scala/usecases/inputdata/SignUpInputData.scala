package usecases.inputdata

import domain.model.common.Email
import domain.model.user.{Name, PlainTextPassword, UserId}

case class SignUpInputData(userId: UserId, name: Name, email: Email, password: PlainTextPassword)