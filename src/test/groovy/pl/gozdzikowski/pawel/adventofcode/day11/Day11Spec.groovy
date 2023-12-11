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
        def paths = cosmicExpansion.findSumOfShortestPaths(new StringInput(input))
        then:
        paths == 374
    }

    @Ignore
    def 'part1'() {
        given:
        Input input = new FileInput("day11.txt")
        when:
        def paths = cosmicExpansion.findSumOfShortestPaths(input)
        then:
        paths == 374
    }
}