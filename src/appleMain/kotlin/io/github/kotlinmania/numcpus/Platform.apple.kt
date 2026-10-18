package io.github.kotlinmania.numcpus

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.ULongVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.darwin.sysctlbyname
import platform.posix._SC_NPROCESSORS_CONF
import platform.posix._SC_NPROCESSORS_ONLN
import platform.posix.sysconf

@OptIn(ExperimentalForeignApi::class)
internal actual fun platformLogicalCpus(): Int {
    val conf = sysconf(_SC_NPROCESSORS_CONF)
    return if (conf < 1L) {
        val online = sysconf(_SC_NPROCESSORS_ONLN)
        if (online < 1L) 1 else online.toInt()
    } else {
        conf.toInt()
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun platformPhysicalCpus(): Int {
    val cpus =
        memScoped {
            val size = alloc<ULongVar>()
            size.value = 4uL
            val value = alloc<IntVar>()
            if (sysctlbyname("hw.physicalcpu", value.ptr, size.ptr, null, 0uL) == 0) {
                value.value
            } else {
                0
            }
        }
    return if (cpus >= 1) cpus else platformLogicalCpus()
}
