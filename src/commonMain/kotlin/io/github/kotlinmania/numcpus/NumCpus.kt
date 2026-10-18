package io.github.kotlinmania.numcpus

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
