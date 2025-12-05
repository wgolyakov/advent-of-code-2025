fun main() {
	fun parse(input: List<String>): Pair<List<LongRange>, List<Long>> {
		var rangesLoaded = false
		val fresh = mutableListOf<LongRange>()
		val available = mutableListOf<Long>()
		for (line in input) {
			if (rangesLoaded) {
				available.add(line.toLong())
			} else {
				if (line.isEmpty()) {
					rangesLoaded = true
				} else {
					val (a, b) = line.split('-').map { it.toLong() }
					fresh.add(a..b)
				}
			}
		}
		return fresh to available
	}

	fun part1(input: List<String>): Int {
		val (fresh, available) = parse(input)
		return available.count { id -> fresh.any { id in it } }
	}

	fun part2(input: List<String>): Long {
		val (fresh, _) = parse(input)
		val ids = mutableSetOf<Long>()
		for (range in fresh) {
			ids.add(range.first)
			ids.add(range.last)
		}
		if (ids.size < 2) return ids.size.toLong()
		val sortedIds = ids.sorted()
		var count = 0L
		var bLast = -1L
		for ((a, b) in sortedIds.windowed(2)) {
			val c = (a + b) / 2
			if (fresh.any { c in it }) {
				if (a != bLast) count++
				count += b - a
				bLast = b
			}
		}
		return count
	}

	val testInput = readInput("Day05_test")
	check(part1(testInput) == 3)
	check(part2(testInput) == 14L)

	val input = readInput("Day05")
	part1(input).println() // 862
	part2(input).println() // 357907198933892
}
