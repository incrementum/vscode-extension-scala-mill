# vscode-extension-scala-mill: helloworld-minimal

**VScode** 'Hello World' **Extension**, written in **Scala** and built with **Mill**. 

**Keywords:** vscode, vscode extension, scala, scalajs, mill, scalablytyped, mill-builder

Microsoft's vscode extension [```helloworld-minimal-sample```](https://github.com/Microsoft/vscode-extension-samples/tree/main/helloworld-minimal-sample)
ported to **Scala** with **Mill**, leveraging [mill-scalablyTyped](https://github.com/lolgab/mill-scalablytyped) (for deriving scala facades to vscode's npm-based api) and [mill-bundler](https://github.com/nafg/mill-bundler) (for bundling of scalajs-derived artifacts).


## Getting Started

Clone this repository:

```
git clone https://github.com/incrementum/vscode-extension-scala-mill.git
```

#### Requirements

**Mill 0.10.12** Currently, a component of this build, ```mill-bundler```, does not yet work with mill version 0.11, so this project requires mill:0.10.12:
```sh
cs install mill:0.10.12 # using coursier to install mill version 0.10.12
mill --version
```

**NPM, TYPESCRIPT** The integration with [vscode's extension api](https://code.visualstudio.com/api) relies on the [npm](https://www.npmjs.com/) package manager and the subsequent installation of [typescript](https://www.typescriptlang.org/) and the vscode api:

```sh
 sudo apt install nodejs npm # installing on ubuntu
 npm install # run in repo base directory; installs packages specified in `package.json`, which include `typescript` and `vscode`, underneath a new `npm_modules` directory
```


#### Inspect the extension's configuration and vscode's launch configuration:
```sh
code ./package.json # note the dependencies, contributed commands, and main reference 
code ./vscode/launch.json # specifies to launch vscode's "extension host" with the artifacts of this repository when issuing a run/debug command (F5) from vscode
```

#### Inspect the extension's source code and mill build:

```sh
code ./app/src/extension.scala # note the correspondence of the registered command with the contributed command in `package.json`
code ./build.sc # become familiar with `scalablytyped` and `mill-builder` following the links to the respective documentation
```

#### Build the extension:
```sh
mill app.fastOpt # builds the development version of the extension at `./out/app/fastOpt.dest/out.js`, which is also referenced by vscode's launch configuration, see above. The build relies on the `scalablyTypedModule`` which generates scalajs facade libraries at the local ivy cache for the module dependencies listed in `package.json` and also implicitly includes the respective `jsDeps`.

# alternatively, continuously build the application, watching for changes:
mill -w app.fastOpt
```

#### Run and test the extension interactively while developing:

1. **Launch the "Extension Host".** Issue "Run->Run without Debugging" (**Ctrl+F5**) to launch the "Extension Host" vscode window instance
2. **Open Command Panel.** In the new vscode window, open the command panel with "**Cmd+Shift+P**"
3. **Invoke the Extension.** On the command panel, locate the "Hello World" extension and invoke it
4. **Observe the Extension's Action.** Notice the pop-up information panel at the bottom-right corner, displaying the "Hello World!" message
5. **Debug the Extension.** Add a breakpoint in the `showHello` function. Start the "Extension Host" with debugging enabled "**F5**". Invoke the extension as described above and the execution will pause at the breakpoint. 

#### Develop new extension behavior:

- Refer to [vscode's Extension API](https://code.visualstudio.com/api)

#### Privately distribute or publish to the extension marketplace:

- Refer to [Publishing Extensions](https://code.visualstudio.com/api/working-with-extensions/publishing-extension)

## References

* [Your First Extension](https://code.visualstudio.com/api/get-started/your-first-extension)
 * [Microsoft Hello World Minimal Sample](https://github.com/Microsoft/vscode-extension-samples/tree/main/helloworld-minimal-sample)
 * [pme123 sbt-based port of Hello World](https://github.com/pme123/vscode-scalajs-hello)









