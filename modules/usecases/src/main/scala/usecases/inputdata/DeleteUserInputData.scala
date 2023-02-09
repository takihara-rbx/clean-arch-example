package usecases.inputdata

import domain.model.user.{Auth, UserId}

case class DeleteUserInputData(auth: Auth, userId: UserId)