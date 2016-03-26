/*
 * Copyright (c) 2015 Alex de Kruijff
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
package org.mazarineblue.parser.variable;

import org.mazarineblue.parser.simple.CharacterContainer;
import org.mazarineblue.parser.simple.CharacterProcessor;
import org.mazarineblue.parser.simple.DataMediator;
import org.mazarineblue.parser.simple.exceptions.IllegalVariableException;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
class ComplexVariableProcessor
        implements CharacterProcessor {

    @Override
    public boolean canProcess(CharacterContainer c) {
        return true;
    }

    @Override
    public void process(CharacterContainer c, DataMediator mediator) {
        if (c.matches('}')) {
            mediator.getProcessorStack().remove(this);
            mediator.appendVariableContentToOutput(c);
        } else
            mediator.appendToVariable(c);
        c.consume();
    }

    @Override
    public void finish(CharacterContainer c, DataMediator mediator) {
        int index = mediator.calculateIndex(c, 1);
        throw new IllegalVariableException(index);
    }
}
