package pl.gozdzikowski.pawel.adventofcode.day4;

import pl.gozdzikowski.pawel.adventofcode.shared.input.Input;

import java.util.Arrays;
import java.util.List;

public class Scratchcards {

    public int getSumOfWiningPoints(Input input) {
        return input.get()
                .stream()
                .map(this::parseSingleCard)
                .map(Card::pointsOfCard)
                .mapToInt(Integer::intValue)
                .sum();
    }

    public Integer getTotalCard(Input input) {
        List<Card> cards = input.get()
                .stream()
                .map(this::parseSingleCard)
                .toList();

        return getTotalCard(cards);
    }
    private int getTotalCard(List<Card> cards) {
        int[] totalCards = new int[cards.size()];
        for (int i = 0; i < cards.size(); ++i) {
            Card card = cards.get(i);
            totalCards[i]++;
            List<Integer> winingPoints = card.findWiningPoints();
            Integer totalCardOfIndex = totalCards[i];
            for (int j = 1; j <= winingPoints.size(); ++j) {
                totalCards[i + j] += totalCardOfIndex;
            }
        }

        return Arrays.stream(totalCards)
                .sum();
    }


    private Card parseSingleCard(String line) {
        String restOfLine = line.split(":")[1].trim();
        String[] cardPointSplit = restOfLine.split("\\|");

        List<Integer> winingPoints = Arrays.stream(cardPointSplit[0].trim().split("\\s"))
                .filter((el) -> !el.isEmpty())
                .map(Integer::parseInt)
                .toList();

        List<Integer> pointsOnCard = Arrays.stream(cardPointSplit[1].trim().split("\\s"))
                .filter((el) -> !el.isEmpty())
                .map(Integer::parseInt)
                .toList();

        return new Card(winingPoints, pointsOnCard);
    }

    public record Card(
            List<Integer> winingPoints,
            List<Integer> pointOnCard
    ) {
        List<Integer> findWiningPoints() {
            return winingPoints.stream().filter(pointOnCard::contains).toList();
        }

        Integer pointsOfCard() {
            List<Integer> winingPoints = findWiningPoints();
            return (int) Math.pow(2, winingPoints.size() - 1);
        }
    }
}
