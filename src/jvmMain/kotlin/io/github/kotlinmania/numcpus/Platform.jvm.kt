package io.github.kotlinmania.numcpus

internal actual fun platformLogicalCpus(): Int =
    Runtime.getRuntime().availableProcessors().coerceAtLeast(1)

internal actual fun platformPhysicalCpus(): Int =
    platformLogicalCpus()
