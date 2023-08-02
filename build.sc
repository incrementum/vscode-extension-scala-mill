/**
 * vscode-extension-sample-scala-mill: helloworld-minimal
 * 
 * Requires mill version 0.10.12  (`mill-bundler` does not yet support 
 * v0.11.x as of 2023-08)
 * 
 * Based on:
 * [Your First Extension](https://code.visualstudio.com/api/get-started/your-first-extension)
 * [Microsoft Hello World Minimal Sample](https://github.com/Microsoft/vscode-extension-samples/tree/main/helloworld-minimal-sample)
 * [pme123 sbt-based port of Hello World from above](https://github.com/pme123/vscode-scalajs-hello)
*/ 

/*
 [mill](https://github.com/com-lihaoyi/mill)
*/
import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._ // ModuleKind
import os._

/*
  [bloop](https://scalacenter.github.io/bloop/docs/build-tools/mill#install-the-plugin)

  mill mill.contrib.bloop.Bloop/install
*/
import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`

/*
  [scalablyTyped](https://github.com/lolgab/mill-scalablytyped)

  Provides module `ScalablyTyped` which on initialization scans directories
  `package.json` and `node_modules`, converts any libraries found, and adds them 
  to `ivyDeps`. Include the `ScalablyTyped` module as a module dependency.

  Configs: 
    scalablyTypedBasePath = os.pwd
    scalablyTypedIgnoredLibs = [], 
    scalablyTypedFlavour = Flavor.Normal
*/
import $ivy.`com.github.lolgab::mill-scalablytyped::latest.release`, com.github.lolgab.mill.scalablytyped._

/*
  [mill-bundler](https://github.com/nafg/mill-bundler)

  Configs:
    webpackLibraryName
    jsDeps
*/
import $ivy.`io.github.nafg.millbundler::jsdeps::0.1.0`, io.github.nafg.millbundler.jsdeps._
import $ivy.`io.github.nafg.millbundler::millbundler::0.1.0`, io.github.nafg.millbundler._

/// MODULES

object versions {
  def scalaVersion = "3.3.0"
  def scalaJSVersion= "1.13.1"  
}

object scalablyTypedModule extends ScalaJSModule with ScalablyTyped {
  def scalaVersion = versions.scalaVersion
  def scalaJSVersion= versions.scalaJSVersion
}

object app extends ScalaJSRollupModule {
  def scalaVersion = versions.scalaVersion
  def scalaJSVersion= versions.scalaJSVersion
  def moduleDeps = Seq(scalablyTypedModule)
  def moduleKind = T { ModuleKind.CommonJSModule }
}