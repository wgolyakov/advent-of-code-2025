import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
	data class Point(val x: Int, val y: Int) {
		fun neighbors() = listOf(
			Point(x + 1, y),
			Point(x, y + 1),
			Point(x - 1, y),
			Point(x, y - 1),
		)
	}

	data class Rectangle(val p1: Point, val p2: Point) {
		fun area() = (abs(p1.x - p2.x) + 1).toLong() * (abs(p1.y - p2.y) + 1).toLong()

		fun isColored(grid: Array<StringBuilder>): Boolean {
			val xMin = min(p1.x, p2.x)
			val xMax = max(p1.x, p2.x)
			val yMin = min(p1.y, p2.y)
			val yMax = max(p1.y, p2.y)
			for (x in xMin..xMax) {
				if (grid[yMin][x] != '#') return false
				if (grid[yMax][x] != '#') return false
			}
			for (y in yMin..yMax) {
				if (grid[y][xMin] != '#') return false
				if (grid[y][xMax] != '#') return false
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

	fun drawEdges(points: List<Point>, grid: Array<StringBuilder>) {
		val loop = points + points.first()
		for ((p1, p2) in loop.windowed(2)) {
			if (p1.x == p2.x) {
				for (y in min(p1.y, p2.y)..max(p1.y, p2.y)) grid[y][p1.x] = '#'
			} else {
				for (x in min(p1.x, p2.x)..max(p1.x, p2.x)) grid[p1.y][x] = '#'
			}
		}
	}

	fun bfsFill(points: List<Point>, grid: Array<StringBuilder>) {
		val minPoint = points.minWith(compareBy({ it.y }, { it.x }))
		val start = Point(minPoint.x + 1, minPoint.y + 1)
		grid[start.y][start.x] = '#'
		val queue = mutableListOf(start)
		while (queue.isNotEmpty()) {
			val curr = queue.removeFirst()
			for (next in curr.neighbors()) {
				if (grid[next.y][next.x] != '#') {
					grid[next.y][next.x] = '#'
					queue.addLast(next)
				}
			}
		}
	}

	fun part2(input: List<String>): Long {
		val points = parse(input)

		val xs = points.map { it.x }.toSet().sorted()
		val xToSmall = mutableMapOf<Int, Int>()
		var currMinus = 0
		var prev = 0
		for (x in xs) {
			if (x - prev > 2) {
				val minus = x - prev - 2
				currMinus += minus
			}
			xToSmall[x] = x - currMinus
			prev = x
		}
		val xToBig = mutableMapOf<Int, Int>()
		for ((k, v) in xToSmall) xToBig[v] = k

		val ys = points.map { it.y }.toSet().sorted()
		val yToSmall = mutableMapOf<Int, Int>()
		currMinus = 0
		prev = 0
		for (y in ys) {
			if (y - prev > 2) {
				val minus = y - prev - 2
				currMinus += minus
			}
			yToSmall[y] = y - currMinus
			prev = y
		}
		val yToBig = mutableMapOf<Int, Int>()
		for ((k, v) in yToSmall) yToBig[v] = k

		val smallPoints = points.map { Point(xToSmall[it.x]!!, yToSmall[it.y]!!) }
		val xMax = smallPoints.maxOf { it.x }
		val yMax = smallPoints.maxOf { it.y }
		val grid = Array(yMax + 2) { StringBuilder(".".repeat(xMax + 2)) }
		drawEdges(smallPoints, grid)
		bfsFill(smallPoints, grid)
		// println(grid.joinToString("\n"))
		var maxArea = 0L
		for ((i, p1) in smallPoints.withIndex()) {
			for (j in i + 1 .. smallPoints.lastIndex) {
				val p2 = smallPoints[j]
				val r = Rectangle(p1, p2)
				if (r.isColored(grid)) {
					val p1Big = Point(xToBig[p1.x]!!, yToBig[p1.y]!!)
					val p2Big = Point(xToBig[p2.x]!!, yToBig[p2.y]!!)
					val area = Rectangle(p1Big, p2Big).area()
					if (area > maxArea) maxArea = area
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
