package com.rutine.troubleshoot;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LearnPattern {

    public static void test() {
        Pattern pattern = Pattern.compile("d|(a(b))");
        System.out.println(pattern.pattern());

        Matcher matcher = pattern.matcher("dabcabc");
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

    public static void test1() {
        Pattern GLOB_PATTERN = Pattern.compile("\\?|\\*|\\{((?:\\{[^/]+?\\}|[^/{}]|\\\\[{}])+?)\\}");
        String DEFAULT_VARIABLE_PATTERN = "(.*)";
        List<String> variableNames = new LinkedList<String>();

        String pattern = "/rutine/{ru1:tine1}/**/b/?";
        StringBuilder patternBuilder = new StringBuilder();
        Matcher matcher = GLOB_PATTERN.matcher(pattern);
        int end = 0;
        while (matcher.find()) {
            patternBuilder.append(quote(pattern, end, matcher.start()));
            String match = matcher.group();
            if ("?".equals(match)) {
                patternBuilder.append('.');
            } else if ("*".equals(match)) {
                patternBuilder.append(".*");
            } else if (match.startsWith("{") && match.endsWith("}")) {
                int colonIdx = match.indexOf(':');
                if (colonIdx == -1) {
                    patternBuilder.append(DEFAULT_VARIABLE_PATTERN);
                    variableNames.add(matcher.group(1));
                } else {
                    String variablePattern = match.substring(colonIdx + 1, match.length() - 1);
                    patternBuilder.append('(');
                    patternBuilder.append(variablePattern);
                    patternBuilder.append(')');
                    String variableName = match.substring(1, colonIdx);
                    variableNames.add(variableName);
                }
            }
            end = matcher.end();
        }
        patternBuilder.append(quote(pattern, end, pattern.length()));

        System.out.println(patternBuilder);
        System.out.println("----");
        for (String str : variableNames) {
            System.out.println(str);
        }
    }

    private static String quote(String s, int start, int end) {
        if (start == end) {
            return "";
        }

        // return Pattern.quote(s.substring(start, end));
        return s.substring(start, end);
    }

}
