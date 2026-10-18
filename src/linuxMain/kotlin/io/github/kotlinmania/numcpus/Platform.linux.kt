package io.github.kotlinmania.numcpus

import platform.posix._SC_NPROCESSORS_CONF
import platform.posix._SC_NPROCESSORS_ONLN
import platform.posix.sysconf

internal actual fun platformLogicalCpus(): Int {
    val conf = sysconf(_SC_NPROCESSORS_CONF)
    return if (conf < 1L) {
        val online = sysconf(_SC_NPROCESSORS_ONLN)
        if (online < 1L) 1 else online.toInt()
    } else {
        conf.toInt()
    }
}

internal actual fun platformPhysicalCpus(): Int =
    platformLogicalCpus()
