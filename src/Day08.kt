import kotlin.math.sqrt

fun main() {
	data class Box(val x: Int, val y: Int, val z: Int)

	fun parse(input: List<String>): List<Box> {
		return input.map { line ->
			val (x, y, z) = line.split(',').map { it.toInt() }
			Box(x, y, z)
		}
	}

	fun distance(b1: Box, b2: Box): Double {
		val dx = (b1.x - b2.x).toDouble()
		val dy = (b1.y - b2.y).toDouble()
		val dz = (b1.z - b2.z).toDouble()
		return sqrt(dx * dx + dy * dy + dz * dz)
	}

	fun part1(input: List<String>, conNum: Int = 1000): Int {
		val boxes = parse(input)
		val distances = mutableMapOf<Pair<Box, Box>, Double>()
		for ((i, b1) in boxes.withIndex()) {
			for (j in i + 1 .. boxes.lastIndex) {
				val b2 = boxes[j]
				distances[b1 to b2] = distance(b1, b2)
			}
		}
		val sortedConnections = distances.entries.sortedBy { it.value }.map { it.key }
		val circuits = mutableListOf<MutableSet<Box>>()
		for (i in 0 until conNum) {
			val (b1, b2) = sortedConnections[i]
			val c1 = circuits.find { b1 in it }
			val c2 = circuits.find { b2 in it }
			if (c1 == null) {
				if (c2 == null) {
					circuits.add(mutableSetOf(b1, b2))
				} else {
					c2.add(b1)
				}
			} else {
				if (c2 == null) {
					c1.add(b2)
				} else if (c1 !== c2) {
					circuits.remove(c2)
					c1.addAll(c2)
				}
			}
		}
		return circuits.sortedByDescending { it.size }.take(3).map { it.size }.reduce { a, e -> a * e }
	}

	fun part2(input: List<String>): Long {
		val boxes = parse(input)
		val distances = mutableMapOf<Pair<Box, Box>, Double>()
		for ((i, b1) in boxes.withIndex()) {
			for (j in i + 1 .. boxes.lastIndex) {
				val b2 = boxes[j]
				distances[b1 to b2] = distance(b1, b2)
			}
		}
		val sortedConnections = distances.entries.sortedBy { it.value }.map { it.key }
		val circuits = mutableListOf<MutableSet<Box>>()
		for ((b1, b2) in sortedConnections) {
			val c1 = circuits.find { b1 in it }
			val c2 = circuits.find { b2 in it }
			if (c1 == null) {
				if (c2 == null) {
					circuits.add(mutableSetOf(b1, b2))
				} else {
					c2.add(b1)
				}
			} else {
				if (c2 == null) {
					c1.add(b2)
				} else if (c1 !== c2) {
					circuits.remove(c2)
					c1.addAll(c2)
				}
			}
			if (circuits.size == 1 && circuits.first().size == boxes.size) return b1.x.toLong() * b2.x.toLong()
		}
		return -1
	}

	val testInput = readInput("Day08_test")
	check(part1(testInput, 10) == 40)
	check(part2(testInput) == 25272L)

	val input = readInput("Day08")
	part1(input).println() // 24360
	part2(input).println() // 2185817796
}
