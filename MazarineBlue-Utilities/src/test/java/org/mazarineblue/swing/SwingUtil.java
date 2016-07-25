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
package org.mazarineblue.swing;

import java.awt.Component;
import java.awt.Window;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import javax.swing.AbstractButton;
import org.mazarineblue.swing.exceptions.ChildNotFoundException;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SwingUtil {

    @SuppressWarnings("SleepWhileInLoop")
    public static <T> T waitFor(Supplier<T> s, long timeout)
            throws TimeoutException {
        long end = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < end)
            try {
                Thread.sleep(100);
                return s.get();
            } catch (ChildNotFoundException | InterruptedException ex) {
            }
        throw new TimeoutException();
    }

    public static <T extends Window> T fetchWindowTitled(Window parent, String title, Class<T> type) {
        T found = searchChilderen(parent, new WindowFetcher<>(), new TitleMatcher<>(title, type));
        if (found == null)
            throw new ChildNotFoundException(title);
        return found;
    }

    public static <T extends Component> T fetchWindowIndexed(Window parent, int index, Class<T> type) {
        T found = searchChilderen(parent, new WindowFetcher<>(), new IndexMatcher<>(index, type));
        if (found == null)
            throw new ChildNotFoundException(index);
        return found;
    }

    public static void clickButton(Component parent, String name) {
        fetchChildNamed(parent, name, AbstractButton.class).doClick();
    }

    public static <T extends Component> T fetchChildNamed(Component parent, String name, Class<T> type) {
        T found = searchChilderen(parent, new DefaultChilderenFetcher<>(), new NameMatcher<>(name, type));
        if (found == null)
            throw new ChildNotFoundException(name);
        return found;
    }

    public static <T extends Component> T fetchChildIndexed(Component parent, int index, Class<T> type) {
        T found = searchChilderen(parent, new DefaultChilderenFetcher<>(), new IndexMatcher<>(index, type));
        if (found == null)
            throw new ChildNotFoundException(index);
        return found;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Component> T searchChilderen(Component parent, Fetcher<T> fetcher, Matcher<T> matcher) {
        for (Component c : fetcher.getChilderen(parent)) {
            if (matcher.childMatches(c))
                return (T) c;
            T found = searchChilderen(c, fetcher, matcher);
            if (found != null)
                return found;
        }
        return null;
    }

    @SuppressWarnings("PublicInnerClass")
    public static interface Fetcher<T extends Component> {

        Component[] getChilderen(Component parent);
    }

    @SuppressWarnings("PublicInnerClass")
    public static interface Matcher<T extends Component> {

        public boolean childMatches(Component c);
    }

    private SwingUtil() {
    }
}
