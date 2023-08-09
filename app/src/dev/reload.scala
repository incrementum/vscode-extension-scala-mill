package dev

import typings.vscode.mod

import scala.scalajs.js
import io.scalajs.nodejs.fs.Fs

object reload:
  lazy val onChange = Fs.watch(
    js.Dynamic.global.__filename.asInstanceOf[String],
    ( eventType: String, filename: String ) =>
      mod.commands.executeCommand( "workbench.action.reloadWindow" )
  )
