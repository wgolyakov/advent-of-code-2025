import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
	data class Point(val x: Int, val y: Int)

	data class Rectangle(val p1: Point, val p2: Point) {
		fun area() = (abs(p1.x - p2.x) + 1).toLong() * (abs(p1.y - p2.y) + 1).toLong()

		fun isColored(colored: Array<MutableList<IntRange>>): Boolean {
			val xMin = min(p1.x, p2.x)
			val xMax = max(p1.x, p2.x)
			val yMin = min(p1.y, p2.y)
			val yMax = max(p1.y, p2.y)
			val horMin = colored[yMin]
			val horMax = colored[yMax]
			for (x in xMin..xMax) {
				if (!horMin.any { x in it }) return false
				if (!horMax.any { x in it }) return false
			}
			for (y in yMin..yMax) {
				val hor = colored[y]
				if (!hor.any { xMin in it }) return false
				if (!hor.any { xMax in it }) return false
			}
			return true
		}
	}

	fun parse(input: List<String>): List<Point> {
		return input.map { line ->
			val (x, y) = line.split(',').map { it.toInt() }
			Point(x, y)
		}
	}

	fun part1(input: List<String>): Long {
		val points = parse(input)
		var maxArea = 0L
		for ((i, p1) in points.withIndex()) {
			for (j in i + 1 .. points.lastIndex) {
				val p2 = points[j]
				val area = Rectangle(p1, p2).area()
				if (area > maxArea) maxArea = area
			}
		}
		return maxArea
	}

	fun horizontalEdges(points: List<Point>): Array<MutableList<IntRange>> {
		val yMax = points.maxOf { it.y }
		val horizontalEdges = Array(yMax + 2) { mutableListOf<IntRange>() }
		val loop = points + points.first()
		for ((p1, p2) in loop.windowed(2)) {
			if (p1.y == p2.y) {
				horizontalEdges[p1.y].add(min(p1.x, p2.x)..max(p1.x, p2.x))
			}
		}
		return horizontalEdges
	}

	fun simplify(lines: MutableList<IntRange>): MutableList<IntRange> {
		if (lines.size < 2) return lines
		val result = mutableListOf<IntRange>()
		var x1 = -1
		for ((h1, h2) in lines.windowed(2)) {
			if (h1.last == h2.first) {
				if (x1 == -1) x1 = h1.first
			} else {
				if (x1 == -1) {
					result.add(h1)
				} else {
					result.add(x1..h1.last)
					x1 = -1
				}
			}
		}
		if (x1 == -1) {
			result.add(lines.last())
		} else {
			result.add(x1..lines.last().last)
		}
		return result
	}

	fun plus(h1: MutableList<IntRange>, h2: MutableList<IntRange>): MutableList<IntRange> {
		if (h1.isEmpty()) return h2
		if (h2.isEmpty()) return h1
		val points = mutableSetOf<Int>()
		for (h in h1) {
			points.add(h.first)
			points.add(h.last)
		}
		for (h in h2) {
			points.add(h.first)
			points.add(h.last)
		}
		val result = mutableListOf<IntRange>()
		for ((p1, p2) in points.sorted().windowed(2)) {
			val c = (p1 + p2) / 2
			if (h1.any { c in it } || h2.any { c in it }) {
				result.add(p1..p2)
			}
		}
		return simplify(result)
	}

	fun inverse(h1: MutableList<IntRange>, h2: MutableList<IntRange>): MutableList<IntRange> {
		if (h1.isEmpty()) return h2
		if (h2.isEmpty()) return h1
		val points = mutableSetOf<Int>()
		for (h in h1) {
			points.add(h.first)
			points.add(h.last)
		}
		for (h in h2) {
			points.add(h.first)
			points.add(h.last)
		}
		val result = mutableListOf<IntRange>()
		for ((p1, p2) in points.sorted().windowed(2)) {
			val c = (p1 + p2) / 2
			if (h1.any { c in it } != h2.any { c in it }) {
				result.add(p1..p2)
			}
		}
		return simplify(result)
	}

	fun fillTiles(points: List<Point>, horizontalEdges: Array<MutableList<IntRange>>): Array<MutableList<IntRange>> {
		val yMax = points.maxOf { it.y }
		val colored = Array(yMax + 1) { mutableListOf<IntRange>() }
		var prev = colored[0]
		for (y in 1..yMax) {
			colored[y] = plus(prev, horizontalEdges[y])
			prev = inverse(prev, horizontalEdges[y])
		}
		return colored
	}

	fun part2(input: List<String>): Long {
		val points = parse(input)
		val horizontalEdges = horizontalEdges(points)
		val colored = fillTiles(points, horizontalEdges)
		var maxArea = 0L
		for ((i, p1) in points.withIndex()) {
			for (j in i + 1 .. points.lastIndex) {
				val p2 = points[j]
				val r = Rectangle(p1, p2)
				if (r.isColored(colored)) {
					val area = r.area()
					if (area > maxArea) {
						maxArea = area
					}
				}

			}
		}
		return maxArea
	}

	val testInput = readInput("Day09_test")
	check(part1(testInput) == 50L)
	check(part2(testInput) == 24L)

	val input = readInput("Day09")
	part1(input).println() // 4741848414
	part2(input).println() // 1508918480
}
