package io.sadeq.datastructures;

import io.sadeq.exceptions.ItemException;
import io.sadeq.exceptions.LineFormatException;
import lombok.Getter;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static io.sadeq.Configs.MAX_ITEMS_PER_LINE;
import static io.sadeq.Configs.MAX_PACKAGE_WEIGHT;
import static io.sadeq.utils.RegexPatterns.*;

@Immutable
@Getter
public final class ProblemInstance implements Serializable {
    private static final long serialVersionUID = -5615641040835621539L;

    private static final transient Logger logger = LoggerFactory.getLogger(ProblemInstance.class);

    private final BigDecimal maxWeight;
    private final List<Item> items;
    private final Map<Integer, Item> map;

    private final transient int maxWeightScale;

    public ProblemInstance(final int lineNo, final String line) throws LineFormatException {
        String[] sections = getSections(lineNo, line);

        maxWeight = new BigDecimal(sections[0]);
        int scale = maxWeight.scale();
        logger.trace("Line #{}: maxWeight = {}", lineNo, maxWeight);

        Matcher matcher = getMatcher(lineNo, sections);
        items = new ArrayList<>();
        int cnt;

        for (cnt = 0; matcher.find(); cnt++) {
            Item item;
            try {
                item = new Item(cnt + 1, matcher.group(1));
            } catch (ItemException e) {
                throw new LineFormatException(lineNo, e);
            }
            if (item.getWeight().compareTo(maxWeight) <= 0)
                items.add(item);

            scale = Math.max(scale, item.getWeight().scale());
        }

        if (cnt > MAX_ITEMS_PER_LINE)
            throw new LineFormatException(lineNo,
                    String.format("At most %d items are allowed per line, but received %d",
                            MAX_ITEMS_PER_LINE, cnt));

        logger.trace("Line #{}: Items = {}", lineNo, items);

        maxWeightScale = scale;
        logger.trace("maxWeightScale = {}", maxWeightScale);

        map = items.stream()
                .collect(Collectors.toMap(Item::getNumber, item -> item));
    }

    @NotNull
    private Matcher getMatcher(int lineNo, String[] sections) throws LineFormatException {
        if (maxWeight.compareTo(MAX_PACKAGE_WEIGHT) > 0)
            throw new LineFormatException(
                    lineNo, String.format("Max package weight %s exceeds %s",
                    maxWeight.toPlainString(), MAX_PACKAGE_WEIGHT.toPlainString()));

        if (!allItemsPattern.matcher(sections[1]).matches())
            throw new LineFormatException(
                    lineNo, "Items must be separated by matching pairs of parentheses");

        return singleItemPattern.matcher(sections[1]);
    }

    @NotNull
    private String[] getSections(int lineNo, String line) throws LineFormatException {
        if (line == null) throw new LineFormatException(lineNo, "Line cannot be null");

        String strippedLine = line.replaceAll("\\s+", "");
        if (strippedLine.isEmpty()) throw new LineFormatException(lineNo, "Line cannot be blank");

        String[] sections = strippedLine.split(":");
        if (sections.length != 2)
            throw new LineFormatException(lineNo, "The line is not in a:b format");

        if (!decimalPattern.matcher(sections[0]).matches())
            throw new LineFormatException(lineNo, "Maximum weight must be a positive number");
        return sections;
    }

    public Map<Integer, Item> getMap() {
        return Collections.unmodifiableMap(map);
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }
}
