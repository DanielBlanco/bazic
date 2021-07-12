package app

import com.raquo.laminar.api.L._
import org.scalajs.dom
import scala.scalajs.js

object Main:

  lazy val container = dom.document.getElementById("app-container")

  lazy val consoleWelcome = println("ScalaJS is up and running!")

  def main(args: Array[String]): Unit =
    consoleWelcome
    documentEvents
      .onDomContentLoaded
      .foreach(_ => render(container, program))(unsafeWindowOwner)

  private def program =
    div(cls := "w-full h-full", view)

  private def view: Div =
    div(
      h3("IMPORTANT WEBSITE"),
      "doing something here"
    )

