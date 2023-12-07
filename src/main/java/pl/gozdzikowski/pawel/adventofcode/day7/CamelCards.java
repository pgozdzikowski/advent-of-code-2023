package pl.gozdzikowski.pawel.adventofcode.day7;

import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static pl.gozdzikowski.pawel.adventofcode.day7.CamelCards.Type.*;

public class CamelCards {


    static Map<Character, Integer> RANKS = Map.ofEntries(
            Map.entry('A', 13),
            Map.entry('K', 12),
            Map.entry('Q', 11),
            Map.entry('J', 10),
            Map.entry('T', 9),
            Map.entry('9', 8),
            Map.entry('8', 7),
            Map.entry('7', 6),
            Map.entry('6', 5),
            Map.entry('5', 4),
            Map.entry('4', 3),
            Map.entry('3', 2),
            Map.entry('2', 1)
    );

    static Map<Character, Integer> RANKS_WITH_JOKER = Map.ofEntries(
            Map.entry('A', 13),
            Map.entry('K', 12),
            Map.entry('Q', 11),
            Map.entry('T', 9),
            Map.entry('9', 8),
            Map.entry('8', 7),
            Map.entry('7', 6),
            Map.entry('6', 5),
            Map.entry('5', 4),
            Map.entry('4', 3),
            Map.entry('3', 2),
            Map.entry('2', 1),
            Map.entry('J', 0)
    );

    public Long calculateRank(Input input) {
        List<Card> list = input.get().stream()
                .map(this::mapSingleCard)
                .sorted()
                .toList();

        Long sum = 0L;
        for (int i = 0; i < list.size(); ++i) {
            sum += list.get(i).rank * (i + 1);
        }

        return sum;
    }

    public Long calculateRankWithJoker(Input input) {
        List<Card> list = input.get().stream()
                .map(this::mapSingleCard)
                .sorted(new JokerComparator())
                .toList();

        Long sum = 0L;
        for (int i = 0; i < list.size(); ++i) {
            sum += list.get(i).rank * (i + 1);
        }

        return sum;
    }

    private Card mapSingleCard(String s) {
        String[] cardsToRank = s.split("\\s");
        List<Character> cards = Arrays.stream(cardsToRank[0].split(""))
                .map((el) -> el.charAt(0))
                .toList();
        return new Card(cards, Long.valueOf(cardsToRank[1]));
    }

    public record Card(
            List<Character> cards,
            Long rank
    ) implements Comparable<Card> {

        Type determineType() {
            Map<Character, List<Character>> collected = cards.stream()
                    .collect(Collectors.groupingBy(Function.identity()));
            if (collected.values().stream().anyMatch((el) -> el.size() == 4))
                return Type.FOUR_OF_A_KIND;
            else if (collected.values().stream().anyMatch((el) -> el.size() == 5))
                return Type.FIVE_OF_A_KIND;
            else if (collected.values().stream().anyMatch((el) -> el.size() == 3) &&
                    collected.values().stream().anyMatch((el) -> el.size() == 2))
                return Type.FULL_HOUSE;
            else if (collected.values().stream().anyMatch((el) -> el.size() == 3)) {
                return THREE_OF_A_KIND;
            } else if (collected.values().stream().filter((el) -> el.size() == 2).toList().size() == 2) {
                return TWO_PAIR;
            } else if (collected.values().stream().anyMatch((el) -> el.size() == 2)) {
                return ONE_PAIR;
            }

            return Type.HIGH_CARD;
        }


        Type determineTypeWithJoker() {
            Map<Character, List<Character>> collected = cards.stream()
                    .collect(Collectors.groupingBy(Function.identity()));
            List<Character> jokeCards = collected.get('J');

            Integer collectedSizeWithoutJoker = collected.size() - 1;
            if (jokeCards == null) {
                return determineType();
            }


            switch(jokeCards.size()) {
                case 1 -> {
                    if(collectedSizeWithoutJoker == 4) {
                        return ONE_PAIR;
                    } else if(collected.values().stream().filter((el) -> el.size() == 2))
                }
            }


            return Type.HIGH_CARD;
        }

        @Override
        public int compareTo(Card o) {
            Type type = this.determineType();
            Type otherType = o.determineType();
            if (type != otherType) {
                return type.ordinal() < otherType.ordinal() ? 1 : -1;
            }

            for (int i = 0; i < cards.size(); ++i) {
                int rank = RANKS.get(cards.get(i));
                int otherRank = RANKS.get(o.cards().get(i));
                if (rank != otherRank) {
                    return rank > otherRank ? 1 : -1;
                }
            }

            return 0;
        }
    }

    class JokerComparator implements Comparator<Card> {


        @Override
        public int compare(Card o1, Card o2) {
            Type type = o1.determineTypeWithJoker();
            Type otherType = o2.determineTypeWithJoker();
            if (type != otherType) {
                return type.ordinal() < otherType.ordinal() ? 1 : -1;
            }

            for (int i = 0; i < o1.cards().size(); ++i) {
                int rank = RANKS_WITH_JOKER.get(o1.cards().get(i));
                int otherRank = RANKS_WITH_JOKER.get(o2.cards().get(i));
                if (rank != otherRank) {
                    return rank > otherRank ? 1 : -1;
                }
            }

            return 0;
        }
    }

    enum Type {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    }
}


