package pl.gozdzikowski.pawel.adventofcode.shared.collections;

import java.util.ArrayList;
import java.util.List;

public class ListExt {
    public static <T> List<List<T>> partition(List<T> toPartition, int window) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < toPartition.size(); i += window) {
            partitions.add(toPartition.subList(i, Math.min(i + window, toPartition.size())));
        }
        return partitions;
    }
}
