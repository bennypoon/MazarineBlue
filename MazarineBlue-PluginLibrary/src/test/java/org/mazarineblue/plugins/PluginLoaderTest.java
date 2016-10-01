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

import static java.util.Arrays.asList;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.plugins.util.FeedPluginSpy1;
import org.mazarineblue.plugins.util.FeedPluginSpy2;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class PluginLoaderTest {

    private PluginLoader<FeedPlugin> loader;

    @Before
    public void setup() {
        loader = new PluginLoader<>(FeedPlugin.class);
    }

    @After
    public void teardown() {
        loader = null;
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void equals_Null() {
        assertFalse(loader.equals(null));
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void equals_DifferentClass() {
        assertFalse(loader.equals(""));
    }

    @Test
    public void equals_DifferentContent() {
        PluginLoader<FeedPlugin> expected = new PluginLoader<>(asList(new FeedPluginSpy2()));
        assertNotEquals(expected, loader);
    }

    @Test
    public void constructor_ReturnsTwoSpies() {
        PluginLoader<FeedPlugin> expected = new PluginLoader<>(asList(new FeedPluginSpy1(), new FeedPluginSpy2()));
        assertEquals(expected, loader);
        assertEquals(expected.hashCode(), loader.hashCode());
    }

    @Test
    public void getSupportedFeeds_ReturnsTwoSpies() {
        PluginLoader<FeedPlugin> l = new PluginLoader<>(asList(new FeedPluginSpy1(), new FeedPluginSpy2()));
        List<FeedPlugin> expected = l.getSupportedFeeds();
        List<FeedPlugin> actual = loader.getSupportedFeeds();
        assertEquals(expected, actual);
        assertEquals(expected.hashCode(), actual.hashCode());
    }
}
