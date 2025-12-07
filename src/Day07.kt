fun main() {
	fun part1(input: List<String>): Int {
		var splitCount = 0
		val grid = input.map { StringBuilder(it) }
		val beams = mutableSetOf<Int>()
		for ((y, line) in grid.withIndex()) {
			if (y == 0) {
				val s = line.indexOf('S')
				beams.add(s)
			} else if (y % 2 == 1) {
				for (x in beams) line[x] = '|'
			} else {
				for ((x, c) in line.withIndex()) {
					if (c == '^' && x in beams) {
						beams.remove(x)
						beams.add(x - 1)
						beams.add(x + 1)
						line[x - 1] = '|'
						line[x + 1] = '|'
						splitCount++
					}
				}
			}
		}
		return splitCount
	}

	fun part2(input: List<String>): Long {
		val beamCounts = LongArray(input[0].length)
		val grid = input.map { StringBuilder(it) }
		for ((y, line) in grid.withIndex()) {
			if (y == 0) {
				val s = line.indexOf('S')
				beamCounts[s] = 1
			} else if (y % 2 == 1) {
				for ((x, n) in beamCounts.withIndex()) if (n > 0) line[x] = '|'
			} else {
				for ((x, c) in line.withIndex()) {
					if (c == '^' && beamCounts[x] > 0) {
						beamCounts[x - 1] += beamCounts[x]
						beamCounts[x + 1] += beamCounts[x]
						beamCounts[x] = 0
						line[x - 1] = '|'
						line[x + 1] = '|'
					}
				}
			}
		}
		return beamCounts.sum()
	}

	val testInput = readInput("Day07_test")
	check(part1(testInput) == 21)
	check(part2(testInput) == 40L)

	val input = readInput("Day07")
	part1(input).println() // 1550
	part2(input).println() // 9897897326778
}
