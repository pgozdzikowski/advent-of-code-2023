package pl.gozdzikowski.pawel.adventofcode.day1


import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import pl.gozdzikowski.pawel.adventofcode.shared.input.ListInput
import spock.lang.Specification

class Day1Spec extends Specification {

    Trebuchet trebuchet = new Trebuchet()

    def 'should resolve day'() {
        given:
        String sample = "pqr3stu8vwx"
        when:
        Long result = trebuchet.findFirstAndLastNumber(sample)
        then:
        result == 38
    }

    def 'find sum of numbers'() {
        given:
        Input input = new ListInput([
                "1abc2",
                "pqr3stu8vwx",
                "a1b2c3d4e5f",
                "treb7uchet"
        ])
        when:
        Long result = trebuchet.findSum(input, trebuchet::findFirstAndLastNumber)
        then:
        result == 142
    }

    def 'results part1'() {
        given:
        Input input = new FileInput('day1.txt')
        when:
        Long sum = trebuchet.findSum(input, trebuchet::findFirstAndLastNumber)
        then:
        sum == 55621
    }

    def 'should find num representation of words'() {
        given:
        String input = "3369eightnine89"
        when:
        Long result = trebuchet.findFirstAndLastSpelled(input)
        then:
        result == 39
    }

    def 'find sum of spelled numbers'() {
        given:
        Input input = new ListInput([
                "two1nine",
                "eightwothree",
                "abcone2threexyz",
                "xtwone3four",
                "4nineeightseven2",
                "zoneight234",
                "7pqrstsixteen"
        ])
        when:
        Long result = trebuchet.findSum(input, trebuchet::findFirstAndLastSpelled)
        then:
        result == 281
    }

    def 'results part2'() {
        given:
        Input input = new FileInput('day1.txt')
        when:
        Long sum = trebuchet.findSum(input, trebuchet::findFirstAndLastSpelled)
        then:
        sum == 53592
    }

}
