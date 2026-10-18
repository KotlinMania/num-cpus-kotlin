package io.github.kotlinmania.numcpus

internal expect fun platformLogicalCpus(): Int

internal expect fun platformPhysicalCpus(): Int
