/*
MIT License

Copyright (c) 2019 Frank Hardy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package io.github.fthardy.progrunnerkit.core;

import java.util.*;

/**
 * This class provides a generic and extendable main-rountine-implementation. It can be used as entry-point for any kind of JavaSE program.
 */
public final class Main {
    
    interface ProgramTaskRunnerFactory {
        
        ProgramTaskRunner createInstance(Class<? extends ProgramPhaseTask> taskTypeClass, ProgramPhaseController phaseController);
    }
    
    interface ProgramPhaseControllerFactory {

        ProgramPhaseController createInstance(Class<? extends ProgramPhaseController> semanticTypeClass);

    }
    
    static ProgramTaskRunnerFactory _progamTaskRunnerFactory = (taskTypeClass, phaseController) -> 
            new ProgramTaskRunner(new ServiceImplProvider<>(taskTypeClass).provideImpls(), phaseController);
    
    static ProgramPhaseControllerFactory _programPhaseControllerFactory = semanticTypeClass ->
            new AggregateProgramPhaseController(new ServiceImplProvider<>(semanticTypeClass).provideImpls());

    /**
     * The main-routine providing the entry point for the JVM-Process.
     * <p>
     * Implements the main process execution procedure which is separated in two phases: the start phase and the end phase. Each phase comprises a collection of
     * tasks which the process is going run one after the other. The end phase is guaranteed to be processed after the start phase even if the start phase is
     * interrupted by an exception. The only exception to this rule is when no start phase exists because no task is available for the start phase - then the
     * program ends without processing the end phase.
     * </p>
     * <p>
     * Each of the two program phases (start- and end-phase) have a {@link ProgramPhaseController} instance of type {@link AggregateProgramPhaseController}.
     * A {@link ProgramPhaseController} provides a set of call back methods. These call back methods are going to be invoked during the program execution
     * procedure. There are specific semantic sub type definitions of {@link ProgramPhaseController} for each of the two program phases:
     * {@link StartPhaseController} and {@link EndPhaseController}. It is possible to provide none or many implementations for each of the two semantic sub
     * types. All implementation instances are collected by a {@link ServiceImplProvider}. The {@link AggregateProgramPhaseController} instance coordinates the
     * delegation to all available controller instances.
     * </p>
     * <p>
     * For each program phase the collection of tasks is collected by a {@link ServiceImplProvider}. The execution of the tasks is implemented by
     * {@link ProgramTaskRunner}. 
     * </p>
     * 
     * @param args the arguments given at the command line.
     */
    public static void main(String[] args) {
        
        final List<String> arguments = Arrays.asList(args);
        
        boolean startPhaseTasksAvailable = true;
        
        ProgramPhaseController startPhaseController = _programPhaseControllerFactory.createInstance(StartPhaseController.class);
        try {
            startPhaseController.onPhaseBegin();
            
            ProgramTaskRunner startPhaseRunner = _progamTaskRunnerFactory.createInstance(StartPhaseTask.class, startPhaseController);
            startPhaseTasksAvailable = startPhaseRunner.runProgramTasks(arguments);
            if (!startPhaseTasksAvailable) {
                startPhaseController.noTaskAvailable();
            }
        } finally {
            startPhaseController.onPhaseEnd();
            
            if (startPhaseTasksAvailable) {
                
                ProgramPhaseController endPhaseController = _programPhaseControllerFactory.createInstance(EndPhaseController.class);
                
                endPhaseController.onPhaseBegin();
                
                try {
                    ProgramTaskRunner endPhaseRunner = _progamTaskRunnerFactory.createInstance(EndPhaseTask.class, endPhaseController);
                    if (!endPhaseRunner.runProgramTasks(arguments)) {
                        endPhaseController.noTaskAvailable();
                    }
                } finally {
                    endPhaseController.onPhaseEnd();
                }
            }
        }
    }
    
    // No instances 
    private Main() {
        // intentionally empty
    }
}
