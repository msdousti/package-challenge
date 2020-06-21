package io.sadeq.utils;

import net.jcip.annotations.Immutable;

import java.util.regex.Pattern;

@Immutable
public final class RegexPatterns {
    // private constructor to prevent instantiation
    private RegexPatterns() {
    }

    // regular expression for a positive integer
    private static final String INT_REGEX = "[1-9]\\d*";

    // regular expression for a non-negative double
    private static final String DECIMAL_REGEX = "(?:\\d+(?:\\.\\d+)?|\\.\\d+)";

    // regular expression for a pair of parentheses, and their contents
    private static final String PARENTHESIS_REGEX = "(?:\\((.*?)\\))";

    public static final Pattern first = Pattern.compile("^" + INT_REGEX + "$");
    public static final Pattern second = Pattern.compile("^" + DECIMAL_REGEX + "$");
    public static final Pattern third = Pattern.compile("^€" + DECIMAL_REGEX + "$");

    public static final Pattern singleItemPattern = Pattern.compile(PARENTHESIS_REGEX);
    public static final Pattern allItemsPattern = Pattern.compile("^" + PARENTHESIS_REGEX + "+$");
    public static final Pattern decimalPattern = Pattern.compile("^" + DECIMAL_REGEX + "$");
}
