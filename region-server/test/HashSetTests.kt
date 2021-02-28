import ch.judos.snakes.region.extension.firstMissingNumber
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class HashSetTests {

	@Test
	fun testHashSet() {
		val set = hashSetOf(1, 2, 4)
		val result = set.firstMissingNumber()
		assertEquals(0, result)
	}

	@Test
	fun testHashSet2() {
		val set = hashSetOf(0, 1, 2, 4)
		val result = set.firstMissingNumber()
		assertEquals(3, result)
	}

	@Test
	fun testHashSet3() {
		val set = hashSetOf(0, 1, 2, 3, 4)
		val result = set.firstMissingNumber()
		assertEquals(5, result)
	}
}
