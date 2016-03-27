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

import java.util.Arrays;
import java.util.Objects;

/**
 * An {@code InstructionLine} is a container for a {@link Path} and arguments.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see Path
 */
class InstructionLine {

    private final Path path;
    private final Object[] parameters;

    InstructionLine(Path path, Object... parameters) {
        this.path = path;
        this.parameters = Arrays.copyOf(parameters, parameters.length);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(path.toString());
        for (int i = 0; i < parameters.length; ++i)
            builder.append(", ").append(parameters[i]);
        return builder.toString();
    }

    String getPath() {
        return path.toString();
    }

    String getNamespace() {
        return path.getNamespace();
    }

    String getKeyword() {
        return path.getKeyword();
    }

    Object[] getArguments() {
        return Arrays.copyOf(parameters, parameters.length);
    }

    @Override
    public int hashCode() {
        return 48223 + 83 * Objects.hashCode(this.path) + Arrays.deepHashCode(this.parameters);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.path, ((InstructionLine) obj).path)
                && Arrays.deepEquals(this.parameters, ((InstructionLine) obj).parameters);
    }
}
