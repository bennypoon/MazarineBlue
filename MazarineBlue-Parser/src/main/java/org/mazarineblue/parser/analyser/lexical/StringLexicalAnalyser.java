/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.mazarineblue.parser.analyser.lexical;

import java.util.ArrayList;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import org.mazarineblue.parser.tokens.Token;

/**
 * A {@code StringLexicalAnalyser} is a {@code LexicalAnalyser} that converts
 * a input string into a set of {@link Token Tokens} using a number of
 * matchers.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class StringLexicalAnalyser
        implements LexicalAnalyser<String> {

    private final List<Matcher<String>> matchers = new ArrayList<>(4);
    private final Matcher<String> literalMatcher;

    /**
     * Constructs a {@code StringLexicalAnalyser}.
     */
    public StringLexicalAnalyser() {
        literalMatcher = new DefaultStringMatcher();
    }

    /**
     * Adds an {@code Matcher} to use parsing the input.
     *
     * @param matcher the matcher to use for parsing.
     */
    public void add(Matcher<String> matcher) {
        matchers.add(matcher);
    }

    @Override
    public List<Token<String>> breakdown(String input) {
        return new Parser(input).getResult();
    }

    private class Parser {

        private final String input;
        private final List<Token<String>> tokens = new ArrayList<>(4);

        private Parser(String input) {
            this.input = input;
        }

        private List<Token<String>> getResult() {
            parseInput();
            processLeftOvers();
            return unmodifiableList(tokens);
        }

        private void parseInput() {
            char[] arr = input.toCharArray();
            for (int i = 0; i < arr.length; ++i)
                processChar(arr, i);
        }

        private void processChar(char[] arr, int index) {
            sendChar(arr[index], index);
            Matcher<String> m = searchMatchersForMatch();
            if (m == null)
                return;
            createTokensForParsedData(m);
            resetMatchers();
            if (m.resendRequired())
                sendChar(arr[index], index);
        }

        private void processLeftOvers() {
            sendChar(Matcher.EOL, input.length());
            Matcher<String> m = searchMatchersForMatch();
            if (m != null)
                createTokensForParsedData(m);
            else if (literalMatcher.isMatch())
                createTokensForParsedData(literalMatcher);
        }

        private void sendChar(char c, int index) {
            matchers.stream().forEach(m -> m.processChar(c, index));
            literalMatcher.processChar(c, index);
        }

        private Matcher<String> searchMatchersForMatch() {
            for (Matcher<String> m : matchers)
                if (m.isMatch())
                    return m;
            return null;
        }

        private void createTokensForParsedData(Matcher<String> m) {
            int s = literalMatcher.getStart();
            int start = m.getStart();
            if (s < start) {
                Token<String> t = literalMatcher.createToken(input, s, start);
                if (isTokenValueMeaningfull(t))
                    tokens.add(t);
            }
            tokens.add(m.createToken(input, start, m.getEnd()));
        }

        private boolean isTokenValueMeaningfull(Token<String> token) {
            return !token.getValue().trim().isEmpty();
        }

        private void resetMatchers() {
            matchers.stream().forEach(Matcher::reset);
            literalMatcher.reset();
        }
    }
}
