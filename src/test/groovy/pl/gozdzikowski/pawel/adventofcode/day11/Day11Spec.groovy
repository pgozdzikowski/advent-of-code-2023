package pl.gozdzikowski.pawel.adventofcode.day11

import org.junit.Ignore
import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import pl.gozdzikowski.pawel.adventofcode.shared.input.StringInput
import spock.lang.Specification

class Day11Spec extends Specification{
    CosmicExpansion cosmicExpansion = new CosmicExpansion()
    def 'should calculate for sample'() {
        given:
        String input = """...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
"""
        when:
        def paths = cosmicExpansion.findSumOfShortestPaths(new StringInput(input), 2)
        then:
        paths == 374
    }

    def 'part1'() {
        given:
        Input input = new FileInput("day11.txt")
        when:
        def paths = cosmicExpansion.findSumOfShortestPaths(input, 2)
        then:
        paths == 10154062
    }

    def 'should calculate for sample part2'() {
        given:
            String input = """...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
"""
        when:
            def paths = cosmicExpansion.findSumOfShortestPaths(new StringInput(input), 100)
        then:
            paths == 8410
    }

    def 'part2'() {
        given:
            Input input = new FileInput("day11.txt")
        when:
            def paths = cosmicExpansion.findSumOfShortestPaths(input, 1000_000)
        then:
            paths == 553083047914
    }
}
