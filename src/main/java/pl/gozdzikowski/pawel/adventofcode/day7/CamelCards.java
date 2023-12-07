package pl.gozdzikowski.pawel.adventofcode.day7;

import pl.gozdzikowski.pawel.adventofcode.shared.collections.Pair;
import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
            Map.entry('T', 10),
            Map.entry('9', 9),
            Map.entry('8', 8),
            Map.entry('7', 7),
            Map.entry('6', 6),
            Map.entry('5', 5),
            Map.entry('4', 4),
            Map.entry('3', 3),
            Map.entry('2', 2),
            Map.entry('J', 1)
    );

    public Long calculateRank(Input input) {
        List<Card> list = input.get().stream()
                .map(this::mapSingleCard)
                .sorted(new NormalComparator())
                .toList();

        return calculateSumOfMultiplyRank(list);
    }

    public Long calculateRankWithJoker(Input input) {
        List<Card> list = input.get().stream()
                .map(this::mapSingleCard)
                .sorted(new JokerComparator())
                .toList();

        return calculateSumOfMultiplyRank(list);
    }

    private static Long calculateSumOfMultiplyRank(List<Card> list) {
        return IntStream.range(0, list.size())
                .mapToObj((idx) -> Pair.of(idx, list.get(idx)))
                .map((el) -> (el.left() + 1) * el.right().rank())
                .reduce(0L, Long::sum);
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
    ) {

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
            List<Character> jokerCards = collected.get('J');

            if (jokerCards == null || jokerCards.size() == 5) {
                return determineType();
            }

            List<Character> withoutJoker = cards.stream().filter((c) -> !c.equals('J')).toList();
            Card cardWithoutJoker = new Card(withoutJoker, 0L);

            return cardWithoutJoker.determineType().determineTypeBaseOnJokerCount(jokerCards.size());
        }
    }

    class NormalComparator implements Comparator<Card> {

        @Override
        public int compare(Card o1, Card o2) {
            Type type = o1.determineType();
            Type otherType = o2.determineType();
            if (type != otherType) {
                return type.ordinal() < otherType.ordinal() ? 1 : -1;
            }

            for (int i = 0; i < o1.cards().size(); ++i) {
                int rank = RANKS.get(o1.cards().get(i));
                int otherRank = RANKS.get(o2.cards().get(i));
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
        FIVE_OF_A_KIND {
            @Override
            Type determineTypeBaseOnJokerCount(Integer jokerCount) {
                return FIVE_OF_A_KIND;
            }
        },
        FOUR_OF_A_KIND {
            @Override
            Type determineTypeBaseOnJokerCount(Integer jokerCount) {
                return switch(jokerCount) {
                    case 1 -> FIVE_OF_A_KIND;
                    default -> FOUR_OF_A_KIND;
                };
            }
        },
        FULL_HOUSE {
            @Override
            Type determineTypeBaseOnJokerCount(Integer jokerCount) {
                return FULL_HOUSE;
            }
        },
        THREE_OF_A_KIND {
            @Override
            Type determineTypeBaseOnJokerCount(Integer jokerCount) {
                return switch(jokerCount) {
                    case 2 -> FIVE_OF_A_KIND;
                    case 1 -> FOUR_OF_A_KIND;
                    default -> THREE_OF_A_KIND;
                };
            }
        },
        TWO_PAIR {
            @Override
            Type determineTypeBaseOnJokerCount(Integer jokerCount) {
                return switch (jokerCount) {
                    case 1 -> FULL_HOUSE;
                    default -> TWO_PAIR;
                };
            }
        },
        ONE_PAIR {
            @Override
            Type determineTypeBaseOnJokerCount(Integer jokerCount) {
                return switch (jokerCount) {
                    case 3 -> FIVE_OF_A_KIND;
                    case 2 -> FOUR_OF_A_KIND;
                    case 1 -> THREE_OF_A_KIND;
                    default -> ONE_PAIR;
                };
            }
        },
        HIGH_CARD {
            @Override
            Type determineTypeBaseOnJokerCount(Integer jokerCount) {
                return switch (jokerCount) {
                    case 4 -> FIVE_OF_A_KIND;
                    case 3 -> FOUR_OF_A_KIND;
                    case 2 -> THREE_OF_A_KIND;
                    case 1 -> ONE_PAIR;
                    default -> HIGH_CARD;
                };
            }
        };

        abstract Type determineTypeBaseOnJokerCount(Integer jokerCount);
    }
}


