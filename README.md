# ![Teal file icon](src/main/resources/icons/tealFile.svg) Teal Language Support for IntelliJ IDEA

IntelliJ IDEA plugin for the [Teal](https://teal-language.org/) programming language (`.tl` files).

Teal is a typed dialect of Lua. This plugin provides IDE support for editing Teal files in IntelliJ IDEA-based IDEs

## Features

- Registers `.tl` files as Teal source files
- Syntax highlighting for Teal/Lua keywords, strings, numbers, identifiers, operators, comments, and type identifiers
- Grammar-Kit based lexer/parser and PSI model
- Basic annotations for Teal type names and simple literal assignment type mismatches
- Context menu actions for `tl check`, `tl gen`, and `tl run`

## Useful Links

- [Teal website](https://teal-language.org/)
- [Teal documentation](https://teal-language.org/book/latest/introduction.html)
- [Types in Teal](https://teal-language.org/book/latest/types_in_teal.html)
- [Official Teal grammar](https://teal-language.org/book/latest/grammar.html)
- [Teal compiler source](https://github.com/teal-language/tl)
- [Teal VS Code extension](https://github.com/teal-language/vscode-teal)
- [Lua 5.4 reference manual](https://www.lua.org/manual/5.4/)

## Project Versions

- IntelliJ IDEA target: `2023.3`
- Gradle IntelliJ Plugin: `1.17.3`
- Grammar-Kit / JFlex: `1.9.1`
- Kotlin: `1.9.24`
- JVM toolchain: `17`

Version values are configured in [gradle.properties](gradle.properties) and [build.gradle.kts](build.gradle.kts).

## Requirements

- JDK 17
- IntelliJ IDEA 2023.3 or compatible IDE for plugin development
- Gradle wrapper from this repository
- Teal CLI (`tl`) is strongly recommended in `$PATH` for correct plugin behavior, including running `tl check`, `tl gen`, and `tl run` against real files

## Build Locally

Use the Gradle wrapper

```bash
./gradlew build
```

Build the plugin ZIP for local installation

```bash
./gradlew buildPlugin
```

The plugin archive is generated under

```text
build/distributions/
```

## Run in a Local IDE Sandbox

Start a sandbox IntelliJ IDEA instance with the plugin installed

```bash
./gradlew runIde
```

This is the fastest way to test syntax highlighting, parsing, annotations, and plugin actions manually.

## Generate Lexer and Parser

The lexer and parser are generated from

- [src/main/grammars/_TealLexer.flex](src/main/grammars/_TealLexer.flex)
- [src/main/grammars/teal.bnf](src/main/grammars/teal.bnf)

Generate both

```bash
./gradlew generateLexer generateParser
```

Generated sources are placed in

```text
src/main/gen/
```

`compileKotlin` depends on `generateLexer` and `generateParser`, so a normal build regenerates them when needed.

## Tests and Checks

Run tests

```bash
./gradlew test
```

Run Kotlin formatting checks

```bash
./gradlew ktlintCheck
```

Format Kotlin sources

```bash
./gradlew ktlintFormat
```

Run the IntelliJ plugin verifier task

```bash
./gradlew verifyPlugin
```

## Development Notes

- Keep the Grammar-Kit BNF aligned with the [official Teal grammar](https://teal-language.org/book/latest/grammar.html), while preserving IntelliJ-specific PSI, mixins, hooks, and recovery rules
- Prefer small grammar changes followed by `./gradlew test`; parser changes can affect generated PSI names and Kotlin code that consumes them
- When changing `.bnf` or `.flex`, regenerate and compile before testing in the IDE sandbox
- Avoid editing generated files in `src/main/gen/` manually. Change the grammar/lexer definitions instead
- Tests can keep Teal fixtures under [src/test/resources/files](src/test/resources/files)

## Plugin Actions

The plugin contributes a `Teal` context menu group with:

- `check`: run `tl check` on the current file
- `gen`: run `tl gen` on the current file
- `run`: run `tl run` on the current file

These actions expect the Teal CLI to be available in the local environment
