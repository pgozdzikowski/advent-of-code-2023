package pl.gozdzikowski.pawel.adventofcode.shared.collections;

public record Pair<K, V>(
        K left,
        V right
) {
    public static <K, V> Pair<K, V> of(K left, V right) {
        return new Pair<>(left, right);
    }
}
