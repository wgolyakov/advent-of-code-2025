fun main() {
	fun part1(input: List<String>): Long {
		val numbers = mutableListOf<List<Long>>()
		var actions = listOf<String>()
		for (line in input) {
			if (line[0] in listOf('+', '*')) {
				actions = line.trim().split("\\s+".toRegex())
			} else {
				numbers.add(line.trim().split("\\s+".toRegex()).map { it.toLong() })
			}
		}
		var total = 0L
		for ((x, action) in actions.withIndex()) {
			val answer = if (action == "+") {
				numbers.sumOf { it[x] }
			} else {
				numbers.map { it[x] }.reduce { a, e -> a * e }
			}
			total += answer
		}
		return total
	}

	fun part2(input: List<String>): Long {
		var total = 0L
		val maxSize = input.maxOf { it.length }
		val grid = input.map { it.padEnd(maxSize, ' ') }
		var action = '+'
		val numbers = mutableListOf<Long>()
		for (x in 0 until maxSize) {
			if (grid.all { it[x] == ' ' }) {
				total += if (action == '+') numbers.sum() else numbers.reduce { a, e -> a * e }
				numbers.clear()
			} else {
				val n = grid.dropLast(1).map { it[x] }.toCharArray().concatToString().trim().toLong()
				numbers.add(n)
				if (grid.last()[x] != ' ') action = grid.last()[x]
			}
		}
		total += if (action == '+') numbers.sum() else numbers.reduce { a, e -> a * e }
		return total
	}

	val testInput = readInput("Day06_test")
	check(part1(testInput) == 4277556L)
	check(part2(testInput) == 3263827L)

	val input = readInput("Day06")
	part1(input).println() // 5171061464548
	part2(input).println() // 10189959087258
}
