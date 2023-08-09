# vscode-extension-scala-mill

**vscode extension** 'Hello World' that reloads when its code changes, written in **Scala** and built with **Mill**. 
##

**Keywords:** vscode, vscode extension, scala, scalajs, mill, scalablytyped, mill-builder
##
**Credits:** This project ports Microsoft's vscode extension [```helloworld-minimal-sample```](https://github.com/Microsoft/vscode-extension-samples/tree/main/helloworld-minimal-sample)
to **Scala** with **Mill**, leveraging [mill-scalablyTyped](https://github.com/lolgab/mill-scalablytyped) (for deriving scala facades to vscode's npm-based api) and [mill-bundler](https://github.com/nafg/mill-bundler) (for bundling of scalajs-derived artifacts).


## Quick Start (5 mins)

```sh
# install the correct version of mill, `mill-bundler` still requires v0.10.12
cs install mill:0.10.12 # using coursier to install mill
mill --version # prints the version and re-starts mill server

# make sure npm is installed
sudo apt-get update
sudo apt install nodejs npm

# clone this repo
git clone https://github.com/incrementum/vscode-extension-scala-mill.git
cd vscode-extension-scala-mill

# open vscode and have metals import the build file
code . 

# compile and watch for changes
mill -w app.fastOpt 
```

### Use the Extension

In **vscode**:
- ```F5``` to open the extension host window

In the **extension host window**: 
- **```ctrl+shift+p```** to open the command panel
- invoke the **"Hello World"** command
- observe in the bottom right corner the information pop window with the "Hello World" message
- make changes to the extension, re-compile; note the extension host window reloads with the changes applied

## How it works

**[npm, typescript, vscode api]** The integration with vscode's extension api relies on the [npm](https://www.npmjs.com/) package manager and the subsequent installation of the [typescript](https://www.typescriptlang.org/) and [vscode](https://code.visualstudio.com/api) modules.

> If the ```./node_modules``` directory does not yet exist, the build will perform ```npm install``` to install the `typescript` and `vscode` modules as specified in the ```./package.json``` file.

**[```./package.json```]** describes the dependencies, contributed commands, and the ```main``` reference pointing to the javascript file generated by ```mill app.fastOpt```. The extension is activated on vscode's ```onStartupFinished``` event to make sure that is is always activated when vscode starts, so that the reload mechanism described below initializes and changes to the extension's code are not missed.

**[```./vscode/launch.json```]** specifies to launch vscode's "extension host" when issuing a run/debug command (F5)

**[```./app/src/extension.scala```]** contains the source code of the extension, which on activation registers the contributed command(s) of the extension. The activation also contains
an instruction to reload the extension when its source code changes. 

**[```./app/src/dev/reload.scala```]** contains code to trigger the reloading of the vscode extension host window when changes to the file containing the extension's javascript code are detected. This file is updated whenever ```mill -w app.fastOpt``` re-generates it, which in turn happens on any saved changes to the underlying scala sources.

**[```./build.sc```]** contains in-line comments explaining its makeup

## How to develop new extension behavior

- Refer to [vscode's extension api](https://code.visualstudio.com/api)

## How to distribute extensions

- Refer to [Publishing Extensions](https://code.visualstudio.com/api/working-with-extensions/publishing-extension)

## References

* [vscode | Your First Extension](https://code.visualstudio.com/api/get-started/your-first-extension)
* [microsoft vscode extension github: Hello World Minimal Sample](https://github.com/Microsoft/vscode-extension-samples/tree/main/helloworld-minimal-sample)
* [pme123 sbt-based port of Hello World](https://github.com/pme123/vscode-scalajs-hello)
