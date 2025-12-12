fun main() {
	data class Point(val x: Int, val y: Int)

	class Shape(val n: Int): ArrayList<String>(3) {
		constructor(n: Int, grid: Array<StringBuilder>): this(n) {
			addAll(grid.map { it.toString() })
		}

		val variants: List<Shape> by lazy {
			var s = this
			val shapesSet = mutableSetOf<Shape>()
			repeat(4) {
				shapesSet.add(s)
				shapesSet.add(s.flippedVer())
				shapesSet.add(s.flippedHor())
				s = s.rotated()
			}
			shapesSet.toList()
		}

		val area: Int by lazy { sumOf { row -> row.count { it == '#' } } }

		fun flippedVer(): Shape {
			val grid = Array(3) { StringBuilder("___") }
			for (y in 0..2) {
				for (x in 0..2) {
					grid[y][2 - x] = this[y][x]
				}
			}
			return Shape(n, grid)
		}

		fun flippedHor(): Shape {
			val grid = Array(3) { StringBuilder("___") }
			for (y in 0..2) {
				for (x in 0..2) {
					grid[2 - y][x] = this[y][x]
				}
			}
			return Shape(n, grid)
		}

		fun rotated(): Shape {
			val grid = Array(3) { StringBuilder("___") }
			for (y in 0..2) {
				for (x in y..2) {
					grid[y][x] = this[x][y]
					grid[x][y] = this[y][x]
				}
			}
			return Shape(n, grid).flippedVer()
		}

		override fun toString() = "Shape($n)"
	}

	class Region(val w: Int, val h: Int, val presents: List<Int>) {
		fun area() = w * h
	}

	fun parse(input: List<String>): Pair<List<Shape>, List<Region>> {
		val shapes = mutableListOf<Shape>()
		val regions = mutableListOf<Region>()
		var shape = Shape(-1)
		for (line in input) {
			if (line.isEmpty()) {
				shapes.add(shape)
			} else if (line[0].isDigit()) {
				if (line[1] == ':') {
					shape = Shape(line[0].digitToInt())
				} else {
					val (sizes, strPresents) = line.split(": ")
					val (w, h) = sizes.split('x').map { it.toInt() }
					val presents = strPresents.split(' ').map { it.toInt() }
					regions.add(Region(w, h, presents))
				}
			} else {
				shape.add(line)
			}
		}
		return shapes to regions
	}

	fun nextCell(p: Point, grid: Array<StringBuilder>): Point {
		return if (p.x + 1 < grid[0].length) {
			Point(p.x + 1, p.y)
		} else {
			Point(0, p.y + 1)
		}
	}

	fun fitShape(shape: Shape, grid: Array<StringBuilder>, p: Point): Boolean {
		if (p.y + 3 > grid.size) return false
		if (p.x + 3 > grid[0].length) return false
		for (x in 0..2) {
			for (y in 0..2) {
				if (shape[y][x] == '#' && grid[p.y + y][p.x + x] == '#') return false
			}
		}
		return true
	}

	fun putShape(shape: Shape, grid: Array<StringBuilder>, p: Point) {
		for (x in 0..2) {
			for (y in 0..2) {
				if (shape[y][x] == '#') grid[p.y + y][p.x + x] = '#'
			}
		}
	}

	fun removeShape(shape: Shape, grid: Array<StringBuilder>, p: Point) {
		for (x in 0..2) {
			for (y in 0..2) {
				if (shape[y][x] == '#') grid[p.y + y][p.x + x] = '.'
			}
		}
	}

	fun fitRecurs(grid: Array<StringBuilder>, p: Point, presents: IntArray, n: Int, shapes: List<Shape>): Boolean {
		if (n == 0) return true
		if (p.y >= grid.size) return false
		for (i in presents.indices) {
			val c = presents[i]
			if (c > 0) {
				for (shape in shapes[i].variants) {
					if (fitShape(shape, grid, p)) {
						putShape(shape, grid, p)
						presents[i] = c - 1
						val res = fitRecurs(grid, nextCell(p, grid), presents, n - 1, shapes)
						if (res) return true
						presents[i] = c
						removeShape(shape, grid, p)
					}
				}
			}
		}
		// skip cell
		return fitRecurs(grid, nextCell(p, grid), presents, n, shapes)
	}

	fun fit(region: Region, shapes: List<Shape>): Boolean {
		val area = region.presents.withIndex().sumOf { shapes[it.index].area * it.value }
		if (area > region.area()) return false
		if (region.presents.sum() * 9 <= (region.w / 3) * 3 * (region.h / 3) * 3) return true
		val grid = Array(region.h) { StringBuilder(".".repeat(region.w)) }
		return fitRecurs(grid, Point(0, 0), region.presents.toIntArray(), region.presents.sum(), shapes)
	}

	fun part1(input: List<String>): Int {
		val (shapes, regions) = parse(input)
		return regions.count { fit(it, shapes) }
	}

	val testInput = readInput("Day12_test")
	check(part1(testInput) == 2)

	val input = readInput("Day12")
	part1(input).println() // 474
}
