package pl.gozdzikowski.pawel.adventofcode.day10

import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import pl.gozdzikowski.pawel.adventofcode.shared.input.StringInput
import spock.lang.Ignore
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

    @Ignore
    def 'part2 sample'() {
        given:
        String input ="""...........
.S-------7.
.|F-----7|.
.||.....||.
.||.....||.
.|L-7.F-J|.
.|..|.|..|.
.L--J.L--J.
..........."""
        when:
        Integer res = pipeMaze.calculateSurroundedTiles(new StringInput(input))
        then:
        res == 4
    }

    @Ignore
    def 'part2 sample2'() {
        given:
        String input =""".F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ..."""
        when:
        Integer res = pipeMaze.calculateSurroundedTiles(new StringInput(input))
        then:
        res == 8
    }

    @Ignore
    def 'part2 sample3'() {
        given:
        String input ="""FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L"""
        when:
        Integer res = pipeMaze.calculateSurroundedTiles(new StringInput(input))
        then:
        res == 8
    }

    @Ignore
    def 'part2'() {
        given:
        Input input = new FileInput("day10.txt")
        when:
        Integer res = pipeMaze.calculateSurroundedTiles(input)
        then:
        res == 6733
    }


}
