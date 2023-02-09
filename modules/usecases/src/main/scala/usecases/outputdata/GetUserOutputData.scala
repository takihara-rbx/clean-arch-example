package usecases.outputdata

import domain.model.common.Email
import domain.model.user.{Name, UserId}

case class GetUserOutputData(id: UserId, name: Name, email: Email)