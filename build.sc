/**
 * vscode-extension-sample-scala-mill: helloworld-minimal
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
import mill.define._
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

  Provides the `ScalablyTyped` module, which on initialization scans `package.json` and `node_modules`, 
  converts any libraries found to scala.js facades, and adds them to ivy's local cache and `ivyDeps`.

  Notable Configs: 
    scalablyTypedBasePath = os.pwd
    scalablyTypedIgnoredLibs = [], 
    scalablyTypedFlavour = Flavor.Normal
*/
import $ivy.`com.github.lolgab::mill-scalablytyped::latest.release`, com.github.lolgab.mill.scalablytyped._

/*
  [mill-bundler](https://github.com/nafg/mill-bundler)

  Notable Configs:
    webpackLibraryName
    jsDeps
*/
import $ivy.`io.github.nafg.millbundler::jsdeps::latest.release`, io.github.nafg.millbundler.jsdeps._
import $ivy.`io.github.nafg.millbundler::millbundler::latest.release`, io.github.nafg.millbundler._


/// MODULES

object versions {
  def scalaVersion = "3.3.1"
  def scalaJSVersion= "1.15.0"  
}

object scalablyTypedModule extends ScalaJSModule with ScalablyTyped {
  def scalaVersion = versions.scalaVersion
  def scalaJSVersion= versions.scalaJSVersion
  override def scalablyTypedBasePath: T[os.Path] = T {
    // make sure dir `node_modules` exists and initally required modules 
    // as per `package.json` are installed
    if (!os.isDir(os.pwd / "node_modules")) { 
      os.proc("npm", "install").call(cwd = os.pwd)
    }
    T.workspace
  }
}

object app extends ScalaJSRollupModule {
  def scalaVersion = versions.scalaVersion
  def scalaJSVersion= versions.scalaJSVersion
  def moduleDeps = Seq(scalablyTypedModule)
  def moduleKind = T { ModuleKind.CommonJSModule }  
  def ivyDeps = Agg(   
    ivy"net.exoego:scala-js-nodejs-v16_sjs1_2.13:latest.release" // required for `io.scalajs.nodejs.fs.Fs` (watching for code changes)
  )
}
