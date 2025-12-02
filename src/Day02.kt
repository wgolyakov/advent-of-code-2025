fun main() {
	fun parse(input: List<String>): List<LongRange> {
		return input[0].split(',').map { r ->
			r.split('-').map { it.toLong() }.let { it[0]..it[1] }
		}
	}

	fun invalid(x: Long): Boolean {
		val s = x.toString()
		if (s.length % 2 == 1) return false
		return s.take(s.length / 2) == s.takeLast(s.length / 2)
	}

	fun invalid2(x: Long): Boolean {
		val s = x.toString()
		for (i in 1..s.length / 2) {
			if (s.length % i != 0) continue
			if (s.windowed(i, i).toSet().size == 1) return true
		}
		return false
	}

	fun part1(input: List<String>): Long {
		val ranges = parse(input)
		var result = 0L
		for (r in ranges) {
			for (x in r) {
				if (invalid(x)) result += x
			}
		}
		return result
    }

    fun part2(input: List<String>): Long {
		val ranges = parse(input)
		var result = 0L
		for (r in ranges) {
			for (x in r) {
				if (invalid2(x)) result += x
			}
		}
		return result
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 1227775554L)
	check(part2(testInput) == 4174379265L)

    val input = readInput("Day02")
    part1(input).println() // 18595663903
    part2(input).println() // 19058204438
}
