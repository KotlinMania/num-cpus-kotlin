package io.github.kotlinmania.numcpus

import io.github.kotlinmania.libc.unix.getenv
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NumCpusTest {
    private fun envVar(name: String): Int? {
        val value =
            try {
                getenv(name)
            } catch (_: Throwable) {
                null
            }
        return value?.toIntOrNull()
    }

    @Test
    fun testGet() {
        val num = get()
        val n = envVar("NUM_CPUS_TEST_GET")
        if (n != null) {
            assertEquals(num, n)
        } else {
            assertTrue(num > 0)
            assertTrue(num < 236451)
        }
    }

    @Test
    fun testGetPhysical() {
        val num = getPhysical()
        val n = envVar("NUM_CPUS_TEST_GET_PHYSICAL")
        if (n != null) {
            assertEquals(num, n)
        } else {
            assertTrue(num > 0)
            assertTrue(num < 236451)
        }
    }
}
