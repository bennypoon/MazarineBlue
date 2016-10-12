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
package org.mazarineblue.mbt2.model.tests;

import java.util.Set;
import java.util.TreeSet;
import org.mazarineblue.mbt2.criteria.TestGoal;
import org.mazarineblue.mbt2.criteria.TracePattern;
import org.mazarineblue.mbt2.model.Graph;
import org.mazarineblue.mbt2.model.State;
import org.mazarineblue.mbt2.model.Transition;

public class TestSuite {
    
    public TestCase createTestCase(TraceExtension te) {
        State s = te.getLastTargetState();
        TestCase tc = searchBackwardsFromNoe(s, te);
        return tc.isValidTestCase() ? tc : null;
    }
    
    private TestCase searchBackwardsFromNoe(State s, TraceExtension te) {
        // if (n is intial state and all expressions satisified)
        // return testcase that contains the current path information.
        TestCase tc = null;
        Graph g;
        g.getTransitions().stream()
                .filter(t -> t.)
        // if s has a transtion t that is part of te
        // p. 82
        
        {
            tc = traverseTransition(t, te);
            if (tc != null)
                return tc;
        } else {
            for each incoming transion t of n {
                tc = traverseTransiton(t, te);
                if (tc != null)
                    return tc;
            }
        }
        return null;
    }
    
    TestCase traverseTranstion(Transition t, TraceExtension te) {
        // transform all one-dimesional expressions with t's postcodition.
        // classify precondition of t and add it to the one-dimensional or multidiminsional expressions;
        Graph g = null;
        TestCase tc = searchBackwardsFromNoe(t.sourceNode, te);
    }

    public Set<TraceExtension> extendsPathForTstGoal(TestGoal tg) {
        Set<TraceExtension> set = new TreeSet<>();
        for (TracePattern tp : tg.getTracePatterns())
            set.addAll(buildTraceExtension(tp, 0, new TraceExtension()));
        return set;
    }

    private Set<TraceExtension> buildTraceExtension(TracePattern tp, int index, TraceExtension te) {
        Set<TraceExtension> traceExtensions = new TreeSet<>();
        Set<Transition> transitions = index < tp.size() ? allTransitionThatMatch(te) : allCompletedTransitions(te);
        for (Transition t : transitions) {
            te.add(t);
            traceExtensions.addAll(buildTraceExtension(tp, index + 1, te));
            te.remove(t);
        }
        if (index >= tp.size() && transitions.isEmpty())
            traceExtensions.add(te);
        return traceExtensions;
    }

    private Set<Transition> allTransitionThatMatch(TraceExtension te) {
        Graph g = null;
        return g.getTransitions().stream()
                .filter(t -> (transitionMatch(t, te)))
                .collect(TreeSet::new, Set::add, Set::addAll);
    }

    private boolean transitionMatch(Transition t, TraceExtension te) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Set<Transition> allCompletedTransitions(State s) {
        Graph g = null;
        return g.getOutgoing(s).stream()
                .filter(Transition::isCompleted)
                .collect(TreeSet::new, Set::add, Set::addAll);
    }
}
