package io.github.kotlinmania.numcpus

import io.github.kotlinmania.libc.unix.bsd.apple._SC_NPROCESSORS_CONF
import io.github.kotlinmania.libc.unix.bsd.apple._SC_NPROCESSORS_ONLN
import io.github.kotlinmania.libc.unix.bsd.apple.sysctlbyname
import io.github.kotlinmania.libc.unix.sysconf

/**
 * Returns the number of available CPUs of the current system.
 *
 * This function will get the number of logical cores.
 * This will always return at least `1`.
 */
public fun get(): Int = getNumCpus()

/**
 * Returns the number of physical cores of the current system.
 *
 * This will always return at least `1`.
 */
public fun getPhysical(): Int = getNumPhysicalCpus()

internal fun getNumPhysicalCpus(): Int {
    val cpus = 0
    val cpusSize = 4uL
    val sysctlName = "hw.physicalcpu\u0000"
    if (0 != sysctlbyname(sysctlName, null, cpusSize, null, 0uL)) {
        return getNumCpus()
    }
    return if (cpus < 1) getNumCpus() else cpus
}

internal fun getNumCpus(): Int {
    val confName = _SC_NPROCESSORS_CONF
    val cpus = sysconf(confName)
    return if (cpus < 1L) {
        val online = sysconf(_SC_NPROCESSORS_ONLN)
        if (online < 1L) 1 else online.toInt()
    } else {
        cpus.toInt()
    }
}
