# num-cpus-kotlin in Kotlin

[![GitHub link](https://img.shields.io/badge/GitHub-KotlinMania%2Fnum-cpus-kotlin-blue.svg)](https://github.com/KotlinMania/num-cpus-kotlin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.kotlinmania/num-cpus-kotlin)](https://central.sonatype.com/artifact/io.github.kotlinmania/num-cpus-kotlin)
[![Build status](https://img.shields.io/github/actions/workflow/status/KotlinMania/num-cpus-kotlin/ci.yml?branch=main)](https://github.com/KotlinMania/num-cpus-kotlin/actions)

This is a Kotlin Multiplatform line-by-line transliteration port of [`num_cpus`](https://github.com/seanmonstar/num_cpus).

**Original Project:** This port is based on [`num_cpus`](https://github.com/seanmonstar/num_cpus). All design credit and project intent belong to the upstream authors; this repository is a faithful port to Kotlin Multiplatform with no behavioural changes intended.

### Porting status

This is an **in-progress port**. The goal is feature parity with the upstream Rust crate while providing a native Kotlin Multiplatform API. Every Kotlin file carries a `// port-lint: source <path>` header naming its upstream Rust counterpart so the AST-distance tool can track provenance.

---

## Upstream README — `num_cpus`

> The text below is reproduced from [`https://github.com/seanmonstar/num_cpus`](https://github.com/seanmonstar/num_cpus). It is the upstream project's own description and remains under the upstream authors' authorship; links have been rewritten to absolute upstream URLs where appropriate so they continue to resolve from this repository.

# num_cpus

[![crates.io](https://img.shields.io/crates/v/num_cpus.svg)](https://crates.io/crates/num_cpus)
[![Travis CI Status](https://travis-ci.org/seanmonstar/num_cpus.svg?branch=master)](https://travis-ci.org/seanmonstar/num_cpus)
[![AppVeyor status](https://ci.appveyor.com/api/projects/status/qn8t6grhko5jwno6?svg=true)](https://ci.appveyor.com/project/seanmonstar/num-cpus)

- [Documentation](https://docs.rs/num_cpus)
- [CHANGELOG](CHANGELOG.md)

Count the number of CPUs on the current machine.

## Usage

Add to Cargo.toml:

```toml
[dependencies]
num_cpus = "1.0"
```

In your `main.rs` or `lib.rs`:

```rust
extern crate num_cpus;

// count logical cores this process could try to use
let num = num_cpus::get();
```

---

## About this Kotlin port

### Installation

```kotlin
dependencies {
    implementation("io.github.kotlinmania:num-cpus-kotlin:0.1.0")
}
```

### Building

```bash
./gradlew build
./gradlew test
```

### Targets

- macOS arm64
- Linux x64
- Windows mingw-x64
- iOS arm64 / simulator-arm64 (Swift export + XCFramework)
- JS (browser + Node.js)
- Wasm-JS (browser + Node.js)
- Android (API 24+)

### Porting guidelines

See [AGENTS.md](AGENTS.md) and [CLAUDE.md](CLAUDE.md) for translator discipline, port-lint header convention, and Rust → Kotlin idiom mapping.

### License

This Kotlin port is distributed under the same license terms as the upstream [`num_cpus`](https://github.com/seanmonstar/num_cpus). See [LICENSE](LICENSE) (and any sibling `LICENSE-*` / `NOTICE` files mirrored from upstream) for the full text.

Original work copyrighted by the num_cpus authors.  
Kotlin port: Copyright (c) 2026 Sydney Renee and The Solace Project.

### Acknowledgments

Thanks to the [`num_cpus`](https://github.com/seanmonstar/num_cpus) maintainers and contributors for the original Rust implementation. This port reproduces their work in Kotlin Multiplatform; bug reports about upstream design or behavior should go to the upstream repository.
