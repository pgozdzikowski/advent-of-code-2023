package pl.gozdzikowski.pawel.adventofcode.day10

import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import pl.gozdzikowski.pawel.adventofcode.shared.input.StringInput
import spock.lang.Specification

class Day10Spec extends Specification {

    PipeMaze pipeMaze = new PipeMaze()


    def 'should calculate for sample'() {
        given:
        String input ="""..F7.
.FJ|.
SJ.L7
|F--J
LJ..."""
        when:
        Integer res = pipeMaze.calculatePathInPipe(new StringInput(input))
        then:
        res == 8
    }

    def 'part1'() {
        given:
        Input input = new FileInput("day10.txt")
        when:
        Integer res = pipeMaze.calculatePathInPipe(input)
        then:
        res == 6733
    }
}
