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
import java.util.Set;

public class CoverageCriteria {

    public static List<TestGoal> decisionConditionCoverage(StateMachine sm) {
        List<TestGoal> list = allTransitions(sm);
        list.addAll(conditionCoverage(sm));
        return list;
    }

    public static List<TestGoal> conditionCoverage(StateMachine sm) {
        List<TestGoal> list = new ArrayList<>();
        sm.getTransitions().stream()
                .forEach(t -> {
                    t.getGuard().getAtomicConditions().stream()
                            .forEach(ac -> {
                                StateConfiguration c = new StateConfiguration(t.getSource());
                                Set<Event> e = t.getEvents();
                                list.add(new AtomicTestGoal(new TracePattern(new StepPattern(c, e, ac, null))));
                                list.add(new AtomicTestGoal(new TracePattern(new StepPattern(c, e, new Not(ac), null))));
                            });
                });
        return list;
    }

    public static List<TestGoal> decisionCoverage(StateMachine sm) {
        List<TestGoal> list = allTransitions(sm);
        for (Transition t : sm.getTransitions()) {
            Expression positive = new FalseExpression();
            Expression negative = new FalseExpression();
            Guard g = t.getGuard();
            g.getValueAssigments().stream()
                    .map(va -> va.convertToLogicalFormula())
                    .forEach(cva -> {
                        if (g.satisfied(cva))
                            positive.addOr(cva);
                        else
                            negative.addOr(cva);
                    });
            StateConfiguration c = new StateConfiguration(t.getSource());
            Set<Event> e = t.getEvents();
            StepPattern sp1 = new StepPattern(c, e, positive, null);
            TracePattern tp1 = new TracePattern(sp1);
            AtomicTestGoal atg1 = new AtomicTestGoal(tp1);
            list.add(atg1);

            StepPattern sp2 = new StepPattern(c, e, negative, null);
            TracePattern tp2 = new TracePattern(sp2);
            AtomicTestGoal atg2 = new AtomicTestGoal(tp2);
            list.add(atg2);
        }
        return list;
    }

    public static List<TestGoal> allConfigurations(StateMachine sm) {
        return sm.getConfigurations().stream()
                .map(sc -> new StepPattern(sc, null, null, null))
                .map(sp -> new TracePattern(sp))
                .map(tp -> new AtomicTestGoal(tp))
                .collect(ArrayList::new, List::add, List::addAll);
    }

    public static List<TestGoal> allTransitionPairs(StateMachine sm) {
        List<TestGoal> testgoals = allTransitions(sm);
        sm.getTransitions().stream()
                .forEach((t1) -> {
                    StepPattern sp1 = new StepPattern(null, null, null, asList(t1));
                    TracePattern tp1 = new TracePattern(sp1);
                    t1.getTarget().getOutgoing().stream()
                            .map(t2 -> new StepPattern(null, null, null, asList(t2)))
                            .map((sp2) -> new TracePattern(sp2)).forEach(tp2 -> {
                        testgoals.add(new AtomicTestGoal(tp1, tp2));
                    });
                });
        return testgoals;
    }

    public static List<TestGoal> allTransitions(StateMachine sm) {
        return sm.getTransitions().stream()
                .map(t -> new StepPattern(null, null, null, asList(t)))
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

    private CoverageCriteria() {
    }
}
