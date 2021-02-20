package ch.judos.snakes.region.extension


fun <K, V> Map<K, V>.containsAllKeys(vararg keys: K): Boolean {
	for (key in keys) {
		if (!this.containsKey(key)) {
			return false
		}
	}
	return true
}
