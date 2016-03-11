/*
 * Copyright (c) 2011-2013 Alex de Kruijff
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
package org.mazarineblue.parser.precedenceclimbing.tokens;

import java.util.Objects;

/**
 *
 * @author Alex de Kruijff {@literal <akruijff@dds.nl>}
 */
public class Token {

    private final int index;
    private String token;

    public Token(int index) {
        this.index = index;
        this.token = "";
    }

    public Token(String token) {
        index = -1;
        this.token = token;
    }

    public Token(String token, int index) {
        this.token = token;
        this.index = index;
    }

    @Override
    public String toString() {
        return index < 0
                ? token
                : String.format("%s (%d)", token, index);
    }

    public int length() {
        return token.length();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Token
                ? equals((Token) obj)
                : false;
    }

    public boolean equals(Token other) {
        return Objects.equals(token, other.token);
    }

    @Override
    public int hashCode() {
        return 623 + Objects.hashCode(this.token);
    }

    public int index() {
        return index;
    }

    public String getToken() {
        return token;
    }

    public boolean isEmpty() {
        return token.isEmpty();
    }

    public void trim() {
        token = token.trim();
    }
}