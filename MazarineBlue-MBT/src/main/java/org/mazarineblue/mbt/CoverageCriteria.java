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
package org.mazarineblue.mbt;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.TreeSet;

public class CoverageCriteria {

    public static List<TestGoal> allConfigurations(StateMachine sm) {
        return sm.getConfigurations().stream()
                .map(sc -> new StepPattern(sc, null, null, null))
                .map(sp -> new TracePattern(sp))
                .map(tp -> new AtomicTestGoal(tp))
                .collect(ArrayList::new, List::add, List::addAll);
    }

    public static List<TestGoal> allTransitionPairs(StateMachine sm) {
        List<TestGoal> list = allTransitions(sm);
        for (Transition t1 : sm.getTransitions()) {
            List<Transition> l1 = asList(t1);
            StepPattern sp1 = new StepPattern(null, null, null, new TreeSet<>(l1));
            TracePattern tp1 = new TracePattern(sp1);
            for (Transition t2 : t1.getTarget().getOutgoing()) {
                List<Transition> l2 = asList(t2);
                StepPattern sp2 = new StepPattern(null, null, null, new TreeSet<>(l2));
                TracePattern tp2 = new TracePattern(sp2);
                AtomicTestGoal atg = new AtomicTestGoal(tp1, tp2);
                list.add(atg);
            }
        }
        return list;
    }

    public static List<TestGoal> allTransitions(StateMachine sm) {
        return sm.getTransitions().stream()
                .map(t -> asList(t))
                .map(l -> new StepPattern(null, null, null, new TreeSet<>(l)))
                .map(sp -> new TracePattern(sp))
                .map(tp -> new AtomicTestGoal(tp))
                .collect(() -> allStates(sm), List::add, List::addAll);
    }

    public static List<TestGoal> allStates(StateMachine sm) {
        return sm.getStates().stream()
                .map(s -> new StateConfiguration(s))
                .map(sc -> new StepPattern(sc, null, null, null))
                .map(sp -> new TracePattern(sp))
                .map(tp -> new AtomicTestGoal(tp))
                .collect(ArrayList::new, List::add, List::addAll);
    }
    
    public static List<TestGoal> decisionCoverage(StateMachine sm) {
        List<TestGoal> list = allTransitions(sm);
        for (Transition t: sm.getTransitions()) {
            Expression positive = null;
            Expression negative = null;
            Guard g = t.getGuard();
            for (ValueAssigment va : g.getValueAssigments()) {
                ConditionValueAssignement cva = va.convertToLogicalFormula();
                // p. 47 
            }
        }
    }

    private CoverageCriteria() {
    }
}
