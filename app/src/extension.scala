import typings.vscode.Thenable
import typings.vscode.anon.Dispose
import typings.vscode.mod
import typings.vscode.mod.ExtensionContext
import typings.vscode.mod.window

import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scala.scalajs.js.annotation.JSExportTopLevel

object extension:

  /**
   * Register commands
   */
  @JSExportTopLevel( "activate" )
  def activate( context: ExtensionContext ) = List(
    ( "vscmill.helloWorld", commands.showHello )
  ).foreach {
    case ( name, fun ) =>
      context.subscriptions.push(
        mod.commands
          .registerCommand( name, fun )
          .asInstanceOf[Dispose]
      )

    // reload the extension automatically on code changes
    dev.reloader.reloadOnChange
  }

  /**
   * Deactivate extension
   */
  @JSExportTopLevel( "deactivate" )
  def deactivate() = println( "vscmill deactivated!" )

  /**
   * Command implementations
   */
  object commands:
    def showHello: js.Function1[Any, Thenable[UndefOr[String]]] =
      in => mod.window.showInformationMessage( s"Hello World!" )
