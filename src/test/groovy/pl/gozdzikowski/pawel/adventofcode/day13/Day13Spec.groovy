package pl.gozdzikowski.pawel.adventofcode.day13

import pl.gozdzikowski.pawel.adventofcode.day12.HotSprings
import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import pl.gozdzikowski.pawel.adventofcode.shared.input.ListInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.StringInput
import spock.lang.Ignore
import spock.lang.Specification

class Day13Spec extends Specification {

    PointOfIncidence pointOfIncidence = new PointOfIncidence()

    def 't1'() {
        given:
            String entry = """#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.
"""
            def res = pointOfIncidence.calculateForSingleEntry(entry)
        expect:
            res == 5
    }

    def 't2'() {
        given:
            String entry = """#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#
"""
            def res = pointOfIncidence.calculateForSingleEntry(entry)
        expect:
            res == 400
    }

    def 'example'() {
        given:
            String input = """#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#
"""
        when:
            def res = pointOfIncidence.calculateForInput(new StringInput(input))
        then:
            res == 405
    }

    def 'part1'() {
        given:
            Input input = new FileInput('day13.txt')
        when:
            Long res = pointOfIncidence.calculateForInput(input)
        then:
            res == 41859
    }

}
