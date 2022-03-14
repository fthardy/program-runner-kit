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

import java.util.Collection;
import java.util.List;

/**
 * The program task runner is responsible for running a given collection of {@link ProgramPhaseTask} instances.
 * <p>
 * Each instance is run one after the other in the order of the given collection. The phase controller gets informed before a task is going to be run and after
 * a task has been run by calling either {@link ProgramPhaseController#beforeTaskExecution(String)} or {@link ProgramPhaseController#afterTaskExecution(String)}.
 * When a task throws an exception the controller method {@link ProgramPhaseController#onExceptionFromTask(String, RuntimeException)} is called and depending on
 * the returned boolean value the runner goes on with the execution of the following tasks or stops the execution.  
 * </p>
 * 
 * @see ProgramPhaseController
 */
public class ProgramTaskRunner {
    
    private final Collection<? extends ProgramPhaseTask> tasks;
    private final ProgramPhaseController phaseController;
    
    public ProgramTaskRunner(Collection<? extends ProgramPhaseTask> tasks, ProgramPhaseController phaseController) {
        this.tasks = tasks;
        this.phaseController = phaseController;
    }
    
    /**
     * Run the program phase tasks.
     * 
     * @param arguments the arguments from the command line.
     *
     * @return {@code true} when at least one task exists. Otherwise {@code false}.
     */
    public boolean runProgramTasks(List<String> arguments) {

        boolean tasksAvailable = !this.tasks.isEmpty();
        if (tasksAvailable) {
            for (ProgramPhaseTask task : this.tasks) {
                String identifier = task.getIdentifier();
                this.phaseController.beforeTaskExecution(identifier);
                try {
                    task.run(arguments);
                } catch (RuntimeException e) {
                    if (this.phaseController.onExceptionFromTask(identifier, e)) {
                        break; // end the phase here - any other task of this phase is not going to be run
                    }
                } finally {
                    this.phaseController.afterTaskExecution(identifier);
                }
            }
        }
        return tasksAvailable;
    }
}
