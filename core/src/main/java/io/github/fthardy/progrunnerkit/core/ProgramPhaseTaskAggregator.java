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

import java.util.List;
import java.util.Objects;

/**
 * A special program phase task which aggregates other program phase tasks to run them in a given order.
 * <p>
 * Any subclass extending this base class must provide a no-args-constructor and must implement either {@link StartPhaseTask} or {@link EndPhaseTask}.  
 * </p>
 * 
 * @param <T> the semantic type of the tasks to aggregate. This type must also be implemented for a subclass.  
 *           
 * @see ProgramTaskRunner
 */
public abstract class ProgramPhaseTaskAggregator<T extends ProgramPhaseTask> implements ProgramPhaseTask {
    
    private final List<T> tasks;
    private ProgramPhaseController phaseController;

    /**
     * Initialise this instance with some tasks.
     * 
     * @param tasks the list of tasks to be run by this aggregator. The order of the tasks is taken as the order for execution.
     */
    protected ProgramPhaseTaskAggregator(List<T> tasks) {
        if (Objects.requireNonNull(tasks).isEmpty()) {
            throw new IllegalArgumentException("Undefined tasks!");
        }
        this.tasks = tasks;
    } 
    
    @Override
    public void run(List<String> arguments) {
        if (this.phaseController == null) {
            throw new IllegalStateException("Phase controller has not been set!");
        }
        new ProgramTaskRunner(this.tasks, this.phaseController).runProgramTasks(arguments);
    }

    void setProgramPhaseController(ProgramPhaseController controller) {
        this.phaseController = controller;
    }
}
