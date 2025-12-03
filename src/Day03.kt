fun main() {
	fun part1(input: List<String>): Int {
		var total = 0
		for (line in input) {
			val (i, a) = line.withIndex().maxBy { it.value.digitToInt() }
			if (i < line.lastIndex) {
				val b = line.substring(i + 1).maxBy { it.digitToInt() }
				total += "$a$b".toInt()
			} else {
				val b = line.take(i).maxBy { it.digitToInt() }
				total += "$b$a".toInt()
			}
		}
		return total
	}

	fun calcRecurs(s: String, n: Int): String {
		if (n == 0) return ""
		if (n == s.length) return s
		val (i, a) = s.withIndex().maxBy { it.value.digitToInt() }
		return if (i <= s.length - n) {
			a + calcRecurs(s.substring(i + 1), n - 1)
		} else {
			calcRecurs(s.take(i), n - (s.length - i)) + s.substring(i)
		}
	}

	fun part2(input: List<String>): Long {
		return input.sumOf { calcRecurs(it, 12).toLong() }
	}

	val testInput = readInput("Day03_test")
	check(part1(testInput) == 357)
	check(part2(testInput) == 3121910778619L)

	val input = readInput("Day03")
	part1(input).println() // 16858
	part2(input).println() // 167549941654721
}
