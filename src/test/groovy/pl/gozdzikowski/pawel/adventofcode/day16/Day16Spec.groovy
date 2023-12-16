package pl.gozdzikowski.pawel.adventofcode.day16

import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import pl.gozdzikowski.pawel.adventofcode.shared.input.StringInput
import spock.lang.Ignore
import spock.lang.Specification

class Day16Spec extends Specification {

    TheFloorWillBeLava theFloorWillBeLava = new TheFloorWillBeLava()

    def 'sample part1'() {
        given:
        String input = """.|...\\....
|.-.\\.....
.....|-...
........|.
..........
.........\\
..../.\\\\..
.-.-/..|..
.|....-|.\\
..//.|....
"""
        when:
        def result = theFloorWillBeLava.calculateTotalEnergized(new StringInput(input))
        then:
        result == 46
    }

    def 'task part 1'() {
        given:
        Input input = new FileInput('day16.txt')
        when:
        def energized = theFloorWillBeLava.calculateTotalEnergized(input)
        then:
        energized == 7185
    }

    @Ignore
    def 'sample part 2'() {
        given:
        Input input = new FileInput('day16.txt')
        when:
        def energized = theFloorWillBeLava.maxEnergized(input)
        then:
        energized == 7616
    }
}
