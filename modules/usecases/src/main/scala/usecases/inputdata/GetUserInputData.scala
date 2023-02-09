package usecases.inputdata

import domain.model.user.{Auth, UserId}

case class GetUserInputData(auth: Auth, userId: UserId)