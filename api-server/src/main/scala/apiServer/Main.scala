package apiServer

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import wvlet.airframe.{Design, Session}
import scala.concurrent.ExecutionContextExecutor
import io.StdIn

import adapters.DIModules
import adapters.http.controllers.Controller

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("clean-arch-example-system")
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val interface = "localhost"
    val port = 8081

    val design: Design = DIModules.designs
    val session: Session = design.newSession
    val controller = session.build[Controller]

    val bindingFuture = Http().newServerAt(interface, port).bindFlow(controller.routes)

    println(s"Server online at http://$interface:$port/\nPress RETURN to stop...")

    // develop
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())

    // main
    // sys.addShutdownHook(
    //   bindingFuture
    //     .flatMap(_.unbind())
    //     .onComplete(_ => system.terminate())
    // )
  }
}