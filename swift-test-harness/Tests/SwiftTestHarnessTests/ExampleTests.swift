#if canImport(Testing)
import Testing
import NumCpus

@Suite("NumCpus Swift Export Tests")
struct NumCpusExportTests {
    @Test("NumCpus swift module imported cleanly")
    func testSwiftModuleLoads() throws {
        #expect(Bool(true), "NumCpus swift module imported cleanly")
    }
}
#elseif canImport(XCTest)
import XCTest
import NumCpus

final class NumCpusExportTests: XCTestCase {
    func testSwiftModuleLoads() throws {
        XCTAssertTrue(true, "NumCpus swift module imported cleanly")
    }
}
#endif
