package usecases.inputboundary

import cats.MonadError

import usecases.errors.UseCaseApplicationError

/**
 * @tparam F
 * @tparam InputData
 * @tparam OutputData
 */

trait UseCase[F[_], InputData, OutputData] {
  def execute(inputData: InputData)(implicit ME: MonadError[F, UseCaseApplicationError]): F[OutputData]
}