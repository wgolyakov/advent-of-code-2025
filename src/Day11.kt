fun main() {
	fun parse(input: List<String>): Map<String, Set<String>> {
		val devices = mutableMapOf<String, Set<String>>()
		for (line in input) {
			val (name, outputs) = line.split(": ")
			devices[name] = outputs.split(' ').toSet()
		}
		return devices
	}

	fun recurs1(device: String, devices: Map<String, Set<String>>): Int {
		if (device == "out") return 1
		return devices[device]!!.sumOf { recurs1(it, devices) }
	}

	fun recurs2(path: Set<String>, devices: Map<String, Set<String>>, counters: MutableMap<List<Any>, Long>): Long {
		val device = path.last()
		if (device == "out") return if ("dac" in path && "fft" in path) 1 else 0
		return devices[device]!!.sumOf {
			val key = listOf(it, "dac" in path, "fft" in path)
			counters.getOrPut(key) { recurs2(path + it, devices, counters) }
		}
	}

	fun part1(input: List<String>): Int {
		val devices = parse(input)
		return recurs1("you", devices)
	}

	fun part2(input: List<String>): Long {
		val devices = parse(input)
		return recurs2(setOf("svr"), devices, mutableMapOf())
	}

	val testInput = readInput("Day11_test")
	check(part1(testInput) == 5)
	val testInput2 = readInput("Day11_test2")
	check(part2(testInput2) == 2L)

	val input = readInput("Day11")
	part1(input).println() // 428
	part2(input).println() // 331468292364745
}
