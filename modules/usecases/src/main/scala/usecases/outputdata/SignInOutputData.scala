package usecases.outputdata

import domain.model.user.UserId

case class SignInOutputData(token: String, userId: UserId)