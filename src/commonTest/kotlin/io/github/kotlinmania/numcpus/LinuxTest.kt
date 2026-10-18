// port-lint: tests linux.rs
package io.github.kotlinmania.numcpus

import io.github.kotlinmania.io.files.Path
import io.github.kotlinmania.io.files.SystemFileSystem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

private fun resolveFixturePath(relPath: String): String {
    val candidates =
        listOf(
            relPath,
            "fixtures/${relPath.removePrefix("fixtures/")}",
            "tmp/num_cpus/$relPath",
            "../../$relPath",
            "../../../$relPath",
            "../../../../$relPath",
            "../../tmp/num_cpus/$relPath",
            "../../../tmp/num_cpus/$relPath",
            "../../../../tmp/num_cpus/$relPath",
        )
    for (candidate in candidates) {
        try {
            if (SystemFileSystem.exists(Path(candidate))) {
                return candidate
            }
        } catch (_: Throwable) {
            // Filesystem not available (e.g. browser JS)
        }
    }
    return relPath
}

private fun join(base: String, vararg paths: String): String {
    var result = base
    for (path in paths) {
        result = joinPath(result, path)
    }
    return resolveFixturePath(result)
}

class LinuxV1Test {
    private val FIXTURES_PROC: String = "fixtures/cgroups/proc/cgroups"
    private val FIXTURES_CGROUPS: String = "fixtures/cgroups/cgroups"

    @Test
    fun testLoadMountinfo() {
        val path = join(FIXTURES_PROC, "mountinfo")
        val mntInfo = MountInfo.loadCpu(path, CgroupVersion.V1)
        assertNotNull(mntInfo)
        assertEquals("/", mntInfo.root)
        assertEquals("/sys/fs/cgroup/cpu,cpuacct", mntInfo.mountPoint)

        val zeroPath = join(FIXTURES_PROC, "mountinfo_zero_opt")
        val zeroMntInfo = MountInfo.loadCpu(zeroPath, CgroupVersion.V1)
        assertNotNull(zeroMntInfo)
        assertEquals("/", zeroMntInfo.root)
        assertEquals("/sys/fs/cgroup/cpu,cpuacct", zeroMntInfo.mountPoint)

        val multiPath = join(FIXTURES_PROC, "mountinfo_multi_opt")
        val multiMntInfo = MountInfo.loadCpu(multiPath, CgroupVersion.V1)
        assertNotNull(multiMntInfo)
        assertEquals("/", multiMntInfo.root)
        assertEquals("/sys/fs/cgroup/cpu,cpuacct", multiMntInfo.mountPoint)
    }

    @Test
    fun testLoadSubsys() {
        val path = join(FIXTURES_PROC, "cgroup")
        val subsys = Subsys.loadCpu(path)
        assertNotNull(subsys)
        assertEquals("/", subsys.base)
        assertEquals(CgroupVersion.V1, subsys.version)
    }

    @Test
    fun testCgroupMount() {
        data class TestCase(
            val root: String,
            val mountPoint: String,
            val subsys: String,
            val expected: String?,
        )

        val cases =
            listOf(
                TestCase("/", "/sys/fs/cgroup/cpu", "/", "/sys/fs/cgroup/cpu"),
                TestCase("/docker/01abcd", "/sys/fs/cgroup/cpu", "/docker/01abcd", "/sys/fs/cgroup/cpu"),
                TestCase("/docker/01abcd", "/sys/fs/cgroup/cpu", "/docker/01abcd/", "/sys/fs/cgroup/cpu"),
                TestCase("/docker/01abcd", "/sys/fs/cgroup/cpu", "/docker/01abcd/large", "/sys/fs/cgroup/cpu/large"),
                TestCase("/docker/01abcd", "/sys/fs/cgroup/cpu", "/", null),
                TestCase("/docker/01abcd", "/sys/fs/cgroup/cpu", "/docker", null),
                TestCase("/docker/01abcd", "/sys/fs/cgroup/cpu", "/elsewhere", null),
                TestCase("/docker/01abcd", "/sys/fs/cgroup/cpu", "/docker/01abcd-other-dir", null),
            )

        for (case in cases) {
            val mntInfo =
                MountInfo(
                    version = CgroupVersion.V1,
                    root = case.root,
                    mountPoint = case.mountPoint,
                )
            val subsys =
                Subsys(
                    version = CgroupVersion.V1,
                    base = case.subsys,
                )

            val actual = Cgroup.translate(mntInfo, subsys)?.base
            assertEquals(case.expected, actual)
        }
    }

    @Test
    fun testCgroupCpuQuota() {
        val cgroup = Cgroup.new(CgroupVersion.V1, join(FIXTURES_CGROUPS, "good"))
        assertEquals(6, cgroup.cpuQuota())
    }

    @Test
    fun testCgroupCpuQuotaDivideByZero() {
        val cgroup = Cgroup.new(CgroupVersion.V1, join(FIXTURES_CGROUPS, "zero-period"))
        assertNotNull(cgroup.quotaUs())
        assertEquals(0, cgroup.periodUs())
        assertNull(cgroup.cpuQuota())
    }

    @Test
    fun testCgroupCpuQuotaCeil() {
        val cgroup = Cgroup.new(CgroupVersion.V1, join(FIXTURES_CGROUPS, "ceil"))
        assertEquals(2, cgroup.cpuQuota())
    }
}

class LinuxV2Test {
    private val FIXTURES_PROC: String = "fixtures/cgroups2/proc/cgroups"
    private val FIXTURES_CGROUPS: String = "fixtures/cgroups2/cgroups"

    @Test
    fun testLoadMountinfo() {
        val path = join(FIXTURES_PROC, "mountinfo")
        val mntInfo = MountInfo.loadCpu(path, CgroupVersion.V2)
        assertNotNull(mntInfo)
        assertEquals("/", mntInfo.root)
        assertEquals("/sys/fs/cgroup", mntInfo.mountPoint)
    }

    @Test
    fun testLoadSubsys() {
        val path = join(FIXTURES_PROC, "cgroup")
        val subsys = Subsys.loadCpu(path)
        assertNotNull(subsys)
        assertEquals("/", subsys.base)
        assertEquals(CgroupVersion.V2, subsys.version)
    }

    @Test
    fun testLoadSubsysMulti() {
        val path = join(FIXTURES_PROC, "cgroup_multi")
        val subsys = Subsys.loadCpu(path)
        assertNotNull(subsys)
        assertEquals("/", subsys.base)
        assertEquals(CgroupVersion.V1, subsys.version)
    }

    @Test
    fun testCgroupMount() {
        data class TestCase(
            val root: String,
            val mountPoint: String,
            val subsys: String,
            val expected: String?,
        )

        val cases =
            listOf(
                TestCase("/", "/sys/fs/cgroup/cpu", "/", "/sys/fs/cgroup/cpu"),
                TestCase("/docker/01abcd", "/sys/fs/cgroup/cpu", "/docker/01abcd", "/sys/fs/cgroup/cpu"),
                TestCase("/docker/01abcd", "/sys/fs/cgroup/cpu", "/docker/01abcd/", "/sys/fs/cgroup/cpu"),
                TestCase("/docker/01abcd", "/sys/fs/cgroup/cpu", "/docker/01abcd/large", "/sys/fs/cgroup/cpu/large"),
                TestCase("/docker/01abcd", "/sys/fs/cgroup/cpu", "/", null),
                TestCase("/docker/01abcd", "/sys/fs/cgroup/cpu", "/docker", null),
                TestCase("/docker/01abcd", "/sys/fs/cgroup/cpu", "/elsewhere", null),
                TestCase("/docker/01abcd", "/sys/fs/cgroup/cpu", "/docker/01abcd-other-dir", null),
            )

        for (case in cases) {
            val mntInfo =
                MountInfo(
                    version = CgroupVersion.V1,
                    root = case.root,
                    mountPoint = case.mountPoint,
                )
            val subsys =
                Subsys(
                    version = CgroupVersion.V1,
                    base = case.subsys,
                )

            val actual = Cgroup.translate(mntInfo, subsys)?.base
            assertEquals(case.expected, actual)
        }
    }

    @Test
    fun testCgroupCpuQuota() {
        val cgroup = Cgroup.new(CgroupVersion.V2, join(FIXTURES_CGROUPS, "good"))
        assertEquals(6, cgroup.cpuQuota())
    }

    @Test
    fun testCgroupCpuQuotaDivideByZero() {
        val cgroup = Cgroup.new(CgroupVersion.V2, join(FIXTURES_CGROUPS, "zero-period"))
        val period = cgroup.max()?.second
        assertEquals(0, period)
        assertNull(cgroup.cpuQuota())
    }

    @Test
    fun testCgroupCpuQuotaCeil() {
        val cgroup = Cgroup.new(CgroupVersion.V2, join(FIXTURES_CGROUPS, "ceil"))
        assertEquals(2, cgroup.cpuQuota())
    }
}
