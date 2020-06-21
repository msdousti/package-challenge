package io.sadeq.algorithms;

import io.sadeq.datastructures.Item;

import java.util.Comparator;

public final class ItemComparators {

    private ItemComparators() {
    }

    // If their price is equal, the lighter one wins
    public static final Comparator<Item> priceWeight =
            Comparator.comparing(Item::getPrice).thenComparing(Item::getWeight, Comparator.reverseOrder());

    // If their efficiency is equal, the expensive one wins
    public static final Comparator<Item> efficiencyPrice =
            Comparator.comparing(Item::getEfficiency).thenComparing(Item::getPrice);

}
