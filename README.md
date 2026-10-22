# E.164 CLI

A small command-line application that checks whether a phone number satisfies the project's E.164 constraint. It uses Micronaut's Bean Validation integration to validate a `Contact` and prints whether the number is valid, along with any validation message.

The validator accepts digits with an optional leading `+`. For example, `+14155552671` is valid, while `+1-4155552671` is not.
Valid results are shown in green and invalid results in red when the terminal supports ANSI colors.

## Use the CLI

Pass the phone number directly as the first argument:

```shell
./gradlew run --args='+14155552671'
```

Example output:

```text
Phone number '+14155552671' is valid E.164.
```

An invalid number prints the validation error:

```shell
./gradlew run --args='+1-4155552671'
```

```text
Phone number '+1-4155552671' is not valid E.164.
must be a phone in E.164 format
```

The `--phone` and `-p` options are also accepted. Show the command options with:

```shell
./gradlew run --args='--help'
```

## Build a native image

Install GraalVM JDK 25 with Native Image support, then run:

```shell
./gradlew nativeCompile
```

The executable is created at:

- Linux and macOS: `build/native/nativeCompile/e164`
- Windows: `build/native/nativeCompile/e164.exe`

Run it by passing the phone number directly. For example:

```shell
./build/native/nativeCompile/e164 +14155552671
```

## Measure execution time on macOS

Use `/usr/bin/time -p` before the command to measure its execution time. For the native image:

```shell
/usr/bin/time -p ./build/native/nativeCompile/e164 +34653850085
```

To measure the fat JAR, build it first and then run it with Java:

```shell
./gradlew shadowJar
/usr/bin/time -p java -jar build/libs/e164-0.1-all.jar +34653850085
```

The `real`, `user`, and `sys` values are reported in seconds. `real` is the elapsed wall-clock time; `user` and `sys` are CPU time used by the process and operating system. The JAR measurement includes JVM startup, while the native image measurement includes native process startup.

## Download a native image

Published GitHub releases include native executables for Linux x86-64, macOS arm64, and Windows x86-64. Visit the [E.164 CLI releases](https://github.com/sdelamo/e164cli/releases), open the release you want, and download the executable matching your operating system and processor:

| Operating system | Release asset |
| --- | --- |
| Linux x86-64 | `e164-<version>-linux-amd64` |
| macOS arm64 | `e164-<version>-macos-arm64` |
| Windows x86-64 | `e164-<version>-windows-amd64.exe` |

Each executable also has a `.sha256` checksum file alongside it. On Linux or macOS, make the downloaded executable runnable and invoke it with your phone number:

```shell
chmod +x ./e164-<version>-linux-amd64
./e164-<version>-linux-amd64 +14155552671
```

Use the `macos-arm64` asset name for macOS. In PowerShell, run the Windows executable like this:

```powershell
.\e164-<version>-windows-amd64.exe +14155552671
```

The native release workflow builds and attaches these executables and checksum files when a GitHub release is published. It can also be run manually for an existing release tag.
