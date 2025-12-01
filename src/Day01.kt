fun main() {
    fun part1(input: List<String>): Int {
		var dial = 50
		var zeroCount = 0
		for (line in input) {
			val d = line[0]
			val n = line.drop(1).toInt()
			dial = if (d == 'L') (dial + 100 - (n % 100)) % 100 else (dial + n) % 100
			if (dial == 0) zeroCount++
		}
		return zeroCount
    }

    fun part2(input: List<String>): Int {
		var dial = 50
		var zeroCount = 0
		for (line in input) {
			val d = line[0]
			val n = line.drop(1).toInt()
			if (d == 'L') {
				zeroCount += n / 100
				if (dial > 0 && dial <= n % 100) zeroCount++
				dial = (dial + 100 - (n % 100)) % 100
			} else {
				zeroCount += (dial + n) / 100
				dial = (dial + n) % 100
			}
		}
		return zeroCount
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3)
	check(part2(testInput) == 6)

    val input = readInput("Day01")
    part1(input).println() // 1040
    part2(input).println() // 6027
}
