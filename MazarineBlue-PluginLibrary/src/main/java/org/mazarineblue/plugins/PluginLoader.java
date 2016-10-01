/*
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
package org.mazarineblue.plugins;

import java.util.ArrayList;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

/**
 * A {@code PluginLoader} is a manager of (plugin) objects load dynamically.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> the type of objects to maintain.
 */
public class PluginLoader<T> {

    private final List<T> plugins;

    /**
     * Constructs a {@code PluginLoader} that loads objects of the specified
     * type.
     *
     * @param type the type of objects to maintain.
     */
    public PluginLoader(Class<T> type) {
        ServiceLoader<T> load = ServiceLoader.load(type);
        plugins = new ArrayList<>(4);
        for (T plugin : load)
            plugins.add(plugin);
    }

    PluginLoader(List<T> list) {
        plugins = new ArrayList<>(list);
    }

    public List<T> getSupportedFeeds() {
        return unmodifiableList(plugins);
    }

    @Override
    public String toString() {
        return "size=" + plugins.size();
    }

    @Override
    public int hashCode() {
        return 371 + Objects.hashCode(plugins);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.plugins, ((PluginLoader<?>) obj).plugins);
    }
}
