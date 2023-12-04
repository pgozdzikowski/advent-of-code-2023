package pl.gozdzikowski.pawel.adventofcode.day4

import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import pl.gozdzikowski.pawel.adventofcode.shared.input.ListInput
import spock.lang.Specification

class Day4Spec extends Specification {

    Scratchcards scratchcards = new Scratchcards()

    def 'sample of day part1'() {
        given:
        def input = ["Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53",
                     "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19",
                     "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1",
                     "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83",
                     "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36",
                     "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"]
        when:
        Integer res = scratchcards.getSumOfWiningPoints(new ListInput(input))
        then:
        res == 13
    }

    def 'solution part 1 '() {
        given:
        Input input = new FileInput("day4.txt")
        when:
        Integer result = scratchcards.getSumOfWiningPoints(input)
        then:
        result == 21105
    }

    def 'sample of day part2'() {
        given:
        def input = ["Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53",
                     "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19",
                     "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1",
                     "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83",
                     "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36",
                     "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"]
        when:
        Integer res = scratchcards.getTotalCard(new ListInput(input))
        then:
        res == 30
    }

    def 'solution part 2 '() {
        given:
        Input input = new FileInput("day4.txt")
        when:
        Integer result = scratchcards.getTotalCard(input)
        then:
        result == 5329815
    }
}
