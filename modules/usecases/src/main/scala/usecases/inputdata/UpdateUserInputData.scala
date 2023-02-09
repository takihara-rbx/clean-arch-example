package usecases.inputdata

import domain.model.user.{Auth, Name, UserId}

case class UpdateUserInputData(auth: Auth, userId: UserId, newName: Name)