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
package org.mazarineblue.mbt2.criteria;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;
import org.mazarineblue.mbt2.model.Graph;
import org.mazarineblue.mbt2.model.State;
import org.mazarineblue.mbt2.model.Transition;

public interface CoverageCriteria {

    public static List<TestGoal> allStates(Graph g) {
        return g.getStates().stream()
                .map(s -> new StateConfiguration(s))
                .map(sc -> new StepPattern(sc, null, null, null))
                .map(sp -> new TracePattern(sp))
                .map(tp -> new AtomicTestGoal(tp))
                .collect(ArrayList::new, List::add, List::addAll);
    }

    public static List<TestGoal> allTransitions(Graph g) {
        return g.getTransitions().stream()
                .map(t -> new StepPattern(null, null, null, asList(t)))
                .map(sp -> new TracePattern(sp))
                .map(tp -> new AtomicTestGoal(tp))
                .collect(() -> allStates(g), List::add, List::addAll);
    }

    public static List<TestGoal> allTransitionPairs(Graph g) {
        List<TestGoal> testgoals = allTransitions(g);
        for (Transition t1 : g.getTransitions()) {
            StepPattern sp1 = new StepPattern(null, null, null, asList(t1));
            TracePattern tp1 = new TracePattern(sp1);
            State s = g.getTarget(t1);
            for (Transition t2 : g.getOutgoing(s)) {
                StepPattern sp2 = new StepPattern(null, null, null, asList(t2));
                TracePattern tp2 = new TracePattern(sp2);
                testgoals.add(new AtomicTestGoal(tp1, tp2));
            }
        }
        return testgoals;
    }
}
