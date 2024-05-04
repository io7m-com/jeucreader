jeucreader
===

[![Maven Central](https://img.shields.io/maven-central/v/com.io7m.jeucreader/com.io7m.jeucreader.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.io7m.jeucreader%22)
[![Maven Central (snapshot)](https://img.shields.io/nexus/s/com.io7m.jeucreader/com.io7m.jeucreader?server=https%3A%2F%2Fs01.oss.sonatype.org&style=flat-square)](https://s01.oss.sonatype.org/content/repositories/snapshots/com/io7m/jeucreader/)
[![Codecov](https://img.shields.io/codecov/c/github/io7m-com/jeucreader.svg?style=flat-square)](https://codecov.io/gh/io7m-com/jeucreader)

![com.io7m.jeucreader](./src/site/resources/jeucreader.jpg?raw=true)

| JVM | Platform | Status |
|-----|----------|--------|
| OpenJDK (Temurin) Current | Linux | [![Build (OpenJDK (Temurin) Current, Linux)](https://img.shields.io/github/actions/workflow/status/io7m-com/jeucreader/main.linux.temurin.current.yml)](https://www.github.com/io7m-com/jeucreader/actions?query=workflow%3Amain.linux.temurin.current)|
| OpenJDK (Temurin) LTS | Linux | [![Build (OpenJDK (Temurin) LTS, Linux)](https://img.shields.io/github/actions/workflow/status/io7m-com/jeucreader/main.linux.temurin.lts.yml)](https://www.github.com/io7m-com/jeucreader/actions?query=workflow%3Amain.linux.temurin.lts)|
| OpenJDK (Temurin) Current | Windows | [![Build (OpenJDK (Temurin) Current, Windows)](https://img.shields.io/github/actions/workflow/status/io7m-com/jeucreader/main.windows.temurin.current.yml)](https://www.github.com/io7m-com/jeucreader/actions?query=workflow%3Amain.windows.temurin.current)|
| OpenJDK (Temurin) LTS | Windows | [![Build (OpenJDK (Temurin) LTS, Windows)](https://img.shields.io/github/actions/workflow/status/io7m-com/jeucreader/main.windows.temurin.lts.yml)](https://www.github.com/io7m-com/jeucreader/actions?query=workflow%3Amain.windows.temurin.lts)|

## jeucreader

The `jeucreader` package provides an interface for reading Unicode codepoints
one at a time.

## Features

* Unicode codepoint reader interface.
* High coverage test suite.
* Written in pure Java 21 with no dependencies.
* [OSGi-ready](https://www.osgi.org/)
* [JPMS-ready](https://en.wikipedia.org/wiki/Java_Platform_Module_System)
* ISC license.

## Motivation

For some reason, Java does not expose any interface to read individual Unicode
codepoints from any kind of I/O stream. It _does_ provide methods to, for
example, read text into a `String` and then iterate over the codepoints of
the `String`.

The `jeucreader` package attempts to provide this missing functionality.

## Usage

Given a `java.io.Reader r`, instantiate a `UnicodeCharacterReaderType` and
use it to read individual codepoints:

```
Reader r;

try (var u = UnicodeCharacterReader.newReader(r)) {
  int c0 = u.readCodePoint();
  int c1 = u.readCodePoint();
  int c2 = u.readCodePoint();
  ...
}
```

On consuming malformed text, the reader may raise subtypes of `IOException`
such as `InvalidSurrogatePair`, `MissingLowSurrogate`, `OrphanLowSurrogate`,
and etc.

