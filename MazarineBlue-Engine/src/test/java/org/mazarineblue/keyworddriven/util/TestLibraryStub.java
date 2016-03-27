/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.keyworddriven.util;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TestLibraryStub
        extends Library {

    private final Object callee;

    public TestLibraryStub(String namespace) {
        super(namespace);
        this.callee = new WeakReference<>(this);
    }

    public TestLibraryStub(String namespace, Object callee) {
        super(namespace);
        this.callee = callee;
    }

    @Override
    protected Object getCaller() {
        return callee instanceof Reference
                ? ((Reference) callee).get()
                : callee;
    }

    @Override
    protected void doValidate(ValidateInstructionLineEvent event) {
    }

    @Override
    protected void doBeforeExecution(ExecuteInstructionLineEvent event) {
    }

    @Override
    protected void doAfterExecution(ExecuteInstructionLineEvent event) {
    }
}
