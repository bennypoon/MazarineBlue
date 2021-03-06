/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.keyworddriven;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.FetchLibrariesEvent;
import org.mazarineblue.keyworddriven.events.InstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;
import org.mazarineblue.keyworddriven.exceptions.DeclaringMethodClassNotEqualToCalleeException;
import org.mazarineblue.keyworddriven.exceptions.KeywordConflictException;
import org.mazarineblue.keyworddriven.util.GracefullConvertor;

/**
 * A {@code library} is group of {@link Instruction Instructions} and listens
 * to {@code Events}.
 * <p>
 * Instructions are defined within the library class. An instruction can be
 * declared using one or more {@code @Keyword} annotations. The minimal
 * required amount of arguments can be specified using the {@code @Parameters}
 * annotation. The maximum amount of parameters is extracted from the method
 * signature. When the annotation {@code @PassInvoker} is used then the first
 * parameter must be an {@code Invoker} parameter.
 * <p>
 * An library can also be added to the event bus, though the use of the
 * {@code AddLibraryEvent}. Any method with an {@code @EventHandler} annotation
 * will receive events. The only parameter allowed must be an {@code Event} of
 * child.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see Instruction
 */
public abstract class Library {

    public static final String NO_NAMESPACE = "";
    private final String namespace;
    private final Map<String, Instruction> keywords = new HashMap<>(16);

    /**
     * Creates a object that contains each instruction within.
     *
     * @param namespace the namespace to resolve conflicts between identical keywords.
     */
    public Library(String namespace) {
        this.namespace = GracefullConvertor.degraceNamespace(namespace);
        registerInstructions();
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for Library()">
    private void registerInstructions() {
        getAllMethods().stream().forEach(this::registerMethod);
    }

    private List<Method> getAllMethods() {
        List<Method> list = new ArrayList<>(16);
        for (Method method : getClass().getDeclaredMethods())
            if (libraryIsTheDeclaringClass(method))
                list.add(method);
        return list;
    }

    private boolean libraryIsTheDeclaringClass(Method method) {
        return Library.class.isAssignableFrom(method.getDeclaringClass());
    }

    private void registerMethod(Method method) {
        for (Keyword keyword : getAllKeywords(method))
            registerInstruction(keyword.value(), method);
    }

    private static Keyword[] getAllKeywords(Method method) {
        return method.getAnnotationsByType(Keyword.class);
    }

    private void registerInstruction(String keyword, Method method) {
        Instruction instruction = new Instruction(method);
        instruction.checkInstructionOnInitializion(namespace, keyword);
        registerInstruction(keyword, instruction);
    }
    // </editor-fold>

    /**
     * Registers instruction that is not a member of this class. Executing an
     * instruction requires that is a member of the callee. Therefor mixed
     * libraries, where some instruction method live in side the library and
     * others live outside, will not work.
     *
     * @param keyword                  the keyword to register the method under.
     * @param method                   the method to register under the specified keyword.
     * @param minimumRequiredArguments the minimum required arguments of this method.
     * @see #getCaller()
     */
    public void registerInstruction(String keyword, Method method, int minimumRequiredArguments) {
        if (!method.getDeclaringClass().equals(getCaller().getClass()))
            throw new DeclaringMethodClassNotEqualToCalleeException(method, getCaller());
        Instruction instruction = new Instruction(method, minimumRequiredArguments);
        instruction.checkInstructionOnInitializion(namespace, keyword);
        registerInstruction(keyword, instruction);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for Library() and registerInstruction()">
    private void registerInstruction(String keyword, Instruction instruction) {
        String key = GracefullConvertor.degraceKeyword(keyword);
        if (keywords.containsKey(key))
            throw new KeywordConflictException(key);
        keywords.put(key, instruction);
    }
    // </editor-fold>

    @Override
    public String toString() {
        String keys = Arrays.toString(orderedKeywords().toArray());
        return namespace.isEmpty() ? keys : namespace + ' ' + keys;
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for toString()">
    private Set<String> orderedKeywords() {
        return new TreeSet<>(keywords());
    }

    private Set<String> keywords() {
        return keywords.keySet();
    }
    // </editor-fold>

    public String getNamespace() {
        return namespace;
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     */
    @EventHandler
    public void fetchLibraries(FetchLibrariesEvent event) {
        event.addLibrary(this);
    }

    boolean containsInstruction(String namespace, String keyword) {
        return namespaceMatches(namespace) && libraryContainsInstruction(keyword);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for fetchLibraries()">
    private boolean namespaceMatches(String namespace) {
        return namespace == null
                || namespace.isEmpty()
                || this.namespace.equals(namespace);
    }

    private boolean libraryContainsInstruction(String keyword) {
        return keyword != null
                && !keyword.isEmpty()
                && keywords.containsKey(keyword);
    }
    // </editor-fold>

    void validateInstruction(ValidateInstructionLineEvent event) {
        if (canProcessInstruction(event)) {
            Instruction instruction = getInstruction(event.getKeyword());
            instruction.validate(event);
        }
        doValidate(event);
    }

    void executeInstruction(ExecuteInstructionLineEvent event) {
        if (canProcessInstruction(event)) {
            Instruction instruction = getInstruction(event.getKeyword());
            instruction.checkBeforeExecution(getCaller(), event);
            doBeforeExecution(event);
            instruction.execute(getCaller(), event);
            doAfterExecution(event);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for validateInsruction() and executeInstruction()">
    private boolean canProcessInstruction(InstructionLineEvent event) {
        return keywords.containsKey(event.getKeyword());
    }

    private Instruction getInstruction(final String keyword) {
        return keywords.get(keyword);
    }
    // </editor-fold>

    /**
     * Returns the caller that is used to receive a method call.
     *
     * @return this library by default.
     */
    protected Object getCaller() {
        return this;
    }

    /**
     * This method allows for library specific validation of an instruction
     * line.
     */
    protected void doValidate(ValidateInstructionLineEvent event) {
        // Do nothing by default.
    }

    /**
     * This method allows to perform some kind of action <i>before</i> the
     * execution of an instruction line. An good example is the throwing of
     * runtime exception, if the instruction does not comply to
     * {@code doValidate(ValidateInstructionLineEvent)}.
     */
    protected void doBeforeExecution(ExecuteInstructionLineEvent event) {
        // Do nothing by default.
    }

    /**
     * This method allows to perform some kind of action <i>after</i> the
     * execution of instruction line. An good example is reporting.
     */
    protected void doAfterExecution(ExecuteInstructionLineEvent event) {
        // Do nothing by default.
    }
}
