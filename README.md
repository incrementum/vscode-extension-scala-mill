# vscode-extension-scala-mill
### vscode extension in scala

Ever wanted to write a **vscode extension in scala** that also supports live reloading while you code?
<br/><br/>
A **'Hello World' vscode extension** that reloads when its code changes, written in **Scala** and built with **Mill**. 

##
**Credits:** This project ports Microsoft's vscode extension [```helloworld-minimal-sample```](https://github.com/Microsoft/vscode-extension-samples/tree/main/helloworld-minimal-sample)
to **Scala** with **Mill**, leveraging [mill-scalablyTyped](https://github.com/lolgab/mill-scalablytyped) (for deriving scala facades to vscode's npm-based api) and [mill-bundler](https://github.com/nafg/mill-bundler).


## Quick Start (5 mins)

```sh
# use coursier (cs) to install mill
cs install mill

# make sure npm is installed
sudo apt-get update
sudo apt install nodejs npm

# clone this repo
git clone https://github.com/incrementum/vscode-extension-scala-mill.git
cd vscode-extension-scala-mill

# have mill generate the initial .bloop directory for metals' use
mill mill.contrib.bloop.Bloop/install

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

Scala compiles to JavaScript when using [scala.js](https://www.scala-js.org/) and since vscode extension can also be written in JavaScript, writing a vscode extension in scala becomes an exercise of integrating our scala code with the vscode api and any other npm modules we would like to call. 
 <br/><br/>
Fortunately, [mill-scalablyTyped](https://github.com/lolgab/mill-scalablytyped) automates the creation of scala facades for all the npm modules we specify in ```./package.json```, including the ones that make up the vscode api itself. For this purpose, the build includes a ```scalablyTypedModule``` that the main ```app``` module depends on. 
 <br/><br/>
During its initialization upon each build run, the ```scalablyTypedModule``` scans the ```node_modules``` directory for npm modules and creates a scala.js facade library for each, so that the npm functionality can be called from scala more easily. Conveniently, the facade libraries are then transparently added to ivy's local cache and to mill's `ivyDeps`. 
 <br/><br/>
The ```app``` module's dependency on the ```scalablyTypedModule``` ensures that the main app's dependencies on any npm modules specified in ```package.json``` are fulfilled. To make this work more seamless, the ```scalablyTypedModule``` also checks for the presence of the ```node_modules``` directory, and if it does not exist, calls ```npm install```, which by npm convention creates this directory and installs all the npm modules specified in ```package.json``` there. 
 
> Note that the very first build might take some time to complete, due to the scala facades creation. If you modify the requirements for npm modules in ```package.json```, you may need to call ```npm install``` manually or simply delete the ```node_modules``` directory and the build will call ```npm install``` for you and repopulate the directory.

The net effect is that the main ```app``` modules's dependencies are now the sum of the explicitly specified ```ivyDeps``` underneath the ```app``` module and the transparently added scala.js facades of the npm modules specified in ```package.json```. 
 <br/><br/>
Finally, the main ```app``` module not only depends on the ```scalablytypeModule``` described above, but also extends the functionality of [mill-bundler](https://github.com/nafg/mill-bundler) for additional ways of including npm modules.
 <br/><br/>
Find below more detailed descriptions of what roles the various files play. Most of which you will be familiar with if you have gone through Microsoft's basic tutorial ["vscode | Your First Extension"](https://code.visualstudio.com/api/get-started/your-first-extension) first (highly recommended).

**[npm, typescript, vscode api]** The integration with vscode's extension api relies on the [npm](https://www.npmjs.com/) package manager and the subsequent installation of the [typescript](https://www.typescriptlang.org/) and [vscode](https://code.visualstudio.com/api) modules.

> If the ```./node_modules``` directory does not yet exist, the build will perform ```npm install``` to install the `typescript` and `vscode` modules as specified in the ```./package.json``` file.

**[```./package.json```]**, [vscode's extension manifest](https://code.visualstudio.com/api/references/extension-manifest) is the central file that describes the dependencies, contributed commands, and the ```main``` reference pointing to the javascript file generated by ```mill app.fastOpt```. The extension is activated on vscode's ```onStartupFinished``` event to make sure that the reload mechanism described below initializes - and changes to the extension's code are not missed. 

**[```./vscode/launch.json```]** specifies to launch vscode's "extension host" when issuing a run/debug command (F5)

**[```./app/src/extension.scala```]** contains the source code of the extension, which on activation registers the contributed command(s) of the extension. The activation also contains
an instruction to reload the extension when its source code changes. 

**[```./app/src/dev/reload.scala```]** contains code to trigger the reloading of the vscode extension host window when changes to the file containing the extension's javascript code are detected. This file is updated whenever ```mill -w app.fastOpt``` re-generates it, which in turn happens on any saved changes to the underlying scala sources.

**[```./build.sc```]** contains in-line comments explaining its makeup, but its main feature is provided by `scalablytyped` which creates and ivy local caches scalajs facades for all the npm module dependencies listed in package.json. These facades are also added to the `jsdeps` of the scalajsmodule.

## How to develop new extension behavior

- Refer to [vscode's extension api](https://code.visualstudio.com/api)

## How to distribute extensions

- Refer to [Publishing Extensions](https://code.visualstudio.com/api/working-with-extensions/publishing-extension)

## References

* [vscode | Your First Extension](https://code.visualstudio.com/api/get-started/your-first-extension)
* [microsoft vscode extension github: Hello World Minimal Sample](https://github.com/Microsoft/vscode-extension-samples/tree/main/helloworld-minimal-sample)
* [pme123 sbt-based port of Hello World](https://github.com/pme123/vscode-scalajs-hello)
