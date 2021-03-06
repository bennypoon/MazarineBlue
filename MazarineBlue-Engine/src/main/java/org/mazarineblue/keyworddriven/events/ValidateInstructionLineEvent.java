/*
 * Copyright (c) 2012-2014 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * Copyright (c) 2014-2015 Specialisterren
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.mazarineblue.keyworddriven.events;

/**
 * Validates an instruction, within an library object with the specified
 * namespace, that listens to the specified keyword.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see org.mazarineblue.eventdriven.Feed
 * @see org.mazarineblue.eventdriven.Invoker
 * @see org.mazarineblue.keyworddriven.Library
 */
public class ValidateInstructionLineEvent
        extends InstructionLineEvent {

    private static final int INSTRUCTION_NOT_FOUND = 0x01;
    private static final int MULTIPLE_INSTRUCTIONS_FOUND = 0x02;
    private static final int TO_FEW_ARGUMENTS = 0x04;
    private static final int ARGUMENTS_ARE_INCOMPATIBLE = 0x08;
    public static final int CUSTOM_VALIDATION_FAILED = 0x10;

    private int flags = 0;
    private int customFlags = 0;

    /**
     * A copy constructor that copies all values from another event.
     *
     * @param e the event to copy all values from.
     */
    public ValidateInstructionLineEvent(ValidateInstructionLineEvent e) {
        super(e.getPath(), e.getArguments());
    }

    /**
     * Constructs an {@code ValidateInstructionLineEvent} with an specified path
     * and arguments.
     *
     * @param path      the path to select the instruction with.
     * @param arguments the argument for the validate to the instruction.
     */
    public ValidateInstructionLineEvent(String path, Object... arguments) {
        super(path, arguments);
    }

    @Override
    public String toString() {
        return super.toString() + ", flags=" + flags + ", custom=" + customFlags;
    }

    @Override
    public Status status() {
        Status status = super.status();
        return status != Status.OK ? status
                : isValid() ? Status.OK
                : Status.WARNING;
    }

    @Override
    public String responce() {
        return isInstructionNotFound() ? "Instruction not found"
                : isMultipleInstructionsFound() ? "Multiple instructions found"
                : isLineHasToFewArguments() ? "To few arguments"
                : isArgumentsAreIncompatible() ? "Argument are incmopatible"
                : getUserErrorFlags() != 0 ? "Custom error"
                : "";
    }

    /**
     * Reports if the instruction is valid. The instruction is not valid if:
     * - an library with the specified namespace doesn't exists;
     * - a instruction with the specified keyword doesn't exists;
     * - multiple instruction with the specified keyword exists;
     * - the given arguments are fewer than the specified minimum;
     * - the type of the arguments are incompatible; or
     * - the library reports that instruction is invalid.
     *
     * @return true if the instruction was deemed valid.
     */
    public boolean isValid() {
        return flags == 0 && customFlags == 0;
    }

    /**
     * Changes the state of this {@code Event} to indicate that the
     * instruction was not found.
     */
    public void setInstructionsNotFound() {
        flags |= INSTRUCTION_NOT_FOUND;
    }

    /**
     * Test if there was no instruction found.
     *
     * @return {@code true} if there was no instruction found.
     */
    public boolean isInstructionNotFound() {
        return (flags & INSTRUCTION_NOT_FOUND) != 0;
    }

    /**
     * Changes the state of this {@code Event} to indicate that the
     * multiple instructions where found.
     */
    public void setMultipleInstructionsFound() {
        flags |= MULTIPLE_INSTRUCTIONS_FOUND;
    }

    /**
     * Test if there where multiple instructions found.
     *
     * @return {@code true} if there where multiple instructions found.
     */
    public boolean isMultipleInstructionsFound() {
        return (flags & MULTIPLE_INSTRUCTIONS_FOUND) != 0;
    }

    /**
     * Changes the state of this {@code Event} to indicate that there where to
     * few argument for the instruction.
     */
    public void setToFewArguments() {
        flags |= TO_FEW_ARGUMENTS;
    }

    /**
     * Test if there where to few arguments found.
     *
     * @return {@code true} if the number of arguments was to low.
     */
    public boolean isLineHasToFewArguments() {
        return (flags & TO_FEW_ARGUMENTS) != 0;
    }

    /**
     * Changes the state of this {@code Event} to indicate that the arguments
     * where incompatible for the instruction.
     */
    public void setArgumentsAreIncompatible() {
        flags |= ARGUMENTS_ARE_INCOMPATIBLE;
    }

    /**
     * Test if the arguments where found to be incompatible.
     *
     * @return {@code true} if the arguments where found to be incompatible.
     */
    public boolean isArgumentsAreIncompatible() {
        return (flags & ARGUMENTS_ARE_INCOMPATIBLE) != 0;
    }

    public void setUserErrorFlags(int customFlags) {
        this.customFlags |= customFlags;
    }

    public int getUserErrorFlags() {
        return customFlags;
    }

    @Override
    public int hashCode() {
        return 1805 + 19 * this.flags + this.customFlags;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass() && super.equals(obj)
                && this.flags == ((ValidateInstructionLineEvent) obj).flags
                && this.customFlags == ((ValidateInstructionLineEvent) obj).customFlags;
    }
}
