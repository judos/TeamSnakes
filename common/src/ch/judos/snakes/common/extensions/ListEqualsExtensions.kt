package ch.judos.snakes.common.extensions

fun <T> List<T>.equalsHashing(otherList: List<T>, hash: (T) -> Any): Boolean {
	if (this.size != otherList.size) {
		return false
	}

	val existing = this.map { hash(it) }.toMutableSet()
	for (elem in otherList) {
		if (!existing.remove(hash(elem))) {
			return false
		}
	}
	return existing.size == 0
}