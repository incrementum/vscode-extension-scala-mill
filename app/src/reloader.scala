package dev

import scala.scalajs.js
import io.scalajs.nodejs.fs.Fs
import typings.vscode.mod

object reloader:
  def reloadOnChange = Fs.watch(
    js.Dynamic.global.__filename.asInstanceOf[String],
    ( eventType: String, filename: String ) =>
      mod.commands.executeCommand( "workbench.action.reloadWindow" )
  )
