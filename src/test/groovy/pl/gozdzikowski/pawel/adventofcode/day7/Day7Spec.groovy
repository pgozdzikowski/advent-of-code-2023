package pl.gozdzikowski.pawel.adventofcode.day7

import pl.gozdzikowski.pawel.adventofcode.shared.input.FileInput
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input
import pl.gozdzikowski.pawel.adventofcode.shared.input.ListInput
import spock.lang.Specification

class Day7Spec extends Specification {

    CamelCards camelCards = new CamelCards()

    def 'should calculate for sample'() {
        given:
        Input input = new ListInput([
                '32T3K 765',
                'T55J5 684',
                'KK677 28',
                'KTJJT 220',
                'QQQJA 483'
        ])
        when:
        Long res =camelCards.calculateRank(input)
        then:
        res == 6440
    }

    def 'task input'() {
        given:
        Input input = new FileInput('day7.txt')
        when:
        Long res =camelCards.calculateRank(input)
        then:
        res == 6440
    }

    def 'should calculate for sample part2'() {
        given:
        Input input = new ListInput([
                '32T3K 765',
                'T55J5 684',
                'KK677 28',
                'KTJJT 220',
                'QQQJA 483'
        ])
        when:
        Long res =camelCards.calculateRankWithJoker(input)
        then:
        res == 5905
    }
}
