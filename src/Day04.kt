fun main() {
	fun accessible(grid: List<CharSequence>, x: Int, y: Int): Boolean {
		var adjacentCount = 0
		for (ny in y - 1 .. y + 1) {
			if (ny < 0 || ny >= grid.size) continue
			val row = grid[ny]
			for (nx in x - 1 .. x + 1) {
				if (nx < 0 || nx >= row.length) continue
				if (nx == x && ny == y) continue
				if (row[nx] != '@') continue
				adjacentCount++
			}
		}
		return adjacentCount < 4
	}

	fun part1(input: List<String>): Int {
		var count = 0
		for ((y, row) in input.withIndex()) {
			for ((x, c) in row.withIndex()) {
				if (c != '@') continue
				if (accessible(input, x, y)) count++
			}
		}
		return count
	}

	fun part2(input: List<String>): Int {
		val grid = input.map { StringBuilder(it) }
		var count = 0
		var removed = true
		while (removed) {
			removed = false
			for ((y, row) in grid.withIndex()) {
				for ((x, c) in row.withIndex()) {
					if (c != '@') continue
					if (accessible(grid, x, y)) {
						count++
						row[x] = 'x'
						removed = true
					}
				}
			}
		}
		return count
	}

	val testInput = readInput("Day04_test")
	check(part1(testInput) == 13)
	check(part2(testInput) == 43)

	val input = readInput("Day04")
	part1(input).println() // 1505
	part2(input).println() // 9182
}
