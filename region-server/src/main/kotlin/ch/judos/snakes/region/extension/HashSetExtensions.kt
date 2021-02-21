package ch.judos.snakes.region.extension


fun HashSet<Int>.firstMissingNumber(): Int {
	var nr = 0
	do {
		if (!contains(nr)) return nr
		nr++
	} while (true)
}
