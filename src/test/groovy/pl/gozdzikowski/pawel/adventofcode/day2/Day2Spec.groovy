package pl.gozdzikowski.pawel.adventofcode.day2

import pl.gozdzikowski.pawel.adventofcode.day1.CubeConundrum
import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import pl.gozdzikowski.pawel.adventofcode.shared.input.ListInput
import spock.lang.Specification

class Day2Spec extends Specification {

    CubeConundrum cubeConundrum = new CubeConundrum()

    def 'should calculate result'() {
        given:
        String game = "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"
        when:
        CubeConundrum.GameResults gameResults = cubeConundrum.parseString(game)
        then:
        with(gameResults) {
            green() == 4
            blue() == 9
            red() == 5
        }
    }

    def 'results'() {
        given:
        Input input = new ListInput([
                "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
                "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
                "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
                "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
                "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"
        ])
        when:
        Integer results = cubeConundrum.sumOfGames(input, new CubeConundrum.Limits(12, 14, 13))
        then:
        results == 8
    }

    def 'day1 spec'() {
        given:
        Input input = new FileInput("day2.txt")
        when:
        Integer result = cubeConundrum.sumOfGames(input, new CubeConundrum.Limits(12, 14, 13))
        then:
        result == 0
    }

    def 'power'() {
        given:
        Input input = new ListInput([
                "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
                "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
                "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
                "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
                "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"
        ])
        when:
        Integer results = cubeConundrum.powerOfGame(input)
        then:
        results == 2286
    }

    def 'day2 spec'() {
        given:
        Input input = new FileInput("day2.txt")
        when:
        Integer results = cubeConundrum.powerOfGame(input)
        then:
        results == 0
    }
}
