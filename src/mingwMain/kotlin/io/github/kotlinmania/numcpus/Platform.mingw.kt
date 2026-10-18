package io.github.kotlinmania.numcpus

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.windows.GetSystemInfo
import platform.windows.SYSTEM_INFO

@OptIn(ExperimentalForeignApi::class)
internal actual fun platformLogicalCpus(): Int =
    memScoped {
        val info = alloc<SYSTEM_INFO>()
        GetSystemInfo(info.ptr)
        val num = info.dwNumberOfProcessors.toInt()
        if (num < 1) 1 else num
    }

internal actual fun platformPhysicalCpus(): Int =
    platformLogicalCpus()
