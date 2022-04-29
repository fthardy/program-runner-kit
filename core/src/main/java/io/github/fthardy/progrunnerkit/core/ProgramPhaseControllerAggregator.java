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
import java.util.Objects;

/**
 * A generic phase controller implementation which aggregates a collection of phase controllers to represent them as one.
 * <p>
 * Delegates each call to the controller instances in the order they appear in the collection. If no controller 
 * instances have been provided the call is delegated to a default behaviour implementation. 
 * </p>
 * 
 * @see Main
 */
public class ProgramPhaseControllerAggregator implements ProgramPhaseController {

    private final Collection<? extends ProgramPhaseController> controllers;
    private final ProgramPhaseController defaultBehaviour;

    /**
     * Creates a new instance of this program phase controller.
     *
     * @param controllers the controller instances.
     * @param defaultBehaviour the implementation for the default behaviour. Can be {@code null}.
     */
    public ProgramPhaseControllerAggregator(Collection<? extends ProgramPhaseController> controllers, ProgramPhaseController defaultBehaviour) {
        this.controllers = controllers;
        this.defaultBehaviour = Objects.requireNonNull(defaultBehaviour);
    }

    @Override
    public void onPhaseBegin() {
        if (this.controllers.isEmpty()) {
            this.defaultBehaviour.onPhaseBegin();
        } else for (ProgramPhaseController controller : this.controllers) {
            controller.onPhaseBegin();
        }
    }

    @Override
    public void onPhaseEnd() {
        if (this.controllers.isEmpty()) {
            this.defaultBehaviour.onPhaseEnd();
        } else for (ProgramPhaseController controller : this.controllers) {
            controller.onPhaseEnd();
        }
    }

    @Override
    public void beforeTaskExecution(String taskID) {
        if (this.controllers.isEmpty()) {
            this.defaultBehaviour.beforeTaskExecution(taskID);
        } else for (ProgramPhaseController controller : this.controllers) {
            controller.beforeTaskExecution(taskID);
        }
    }

    @Override
    public void afterTaskExecution(String taskID) {
        if (this.controllers.isEmpty()) {
            this.defaultBehaviour.afterTaskExecution(taskID);
        } else for (ProgramPhaseController controller : this.controllers) {
            controller.afterTaskExecution(taskID);
        }
    }

    /**
     * If one of the controllers returns {@code true} then the delegation process is stopped immediately. No more controller after this is invoked.
     * 
     * @param fromTaskId the identifier of the task which has thrown the exception.
     * @param exception the exception thrown by the task.
     *
     * @return {@code false} if all controllers return {@code false}. {@code true} if one controller returns {@code true}.
     */
    @Override
    public boolean onExceptionFromTask(String fromTaskId, RuntimeException exception) {
        boolean endPhase = false;
        if (this.controllers.isEmpty()) {
            endPhase = this.defaultBehaviour.onExceptionFromTask(fromTaskId, exception);
        } else {
            for (ProgramPhaseController controller : this.controllers) {
                if (controller.onExceptionFromTask(fromTaskId, exception)) {
                    endPhase = true;
                    break;
                }
            }
        }
        return endPhase;
    }

    @Override
    public void noTaskAvailable() {
        if (this.controllers.isEmpty()) {
            this.defaultBehaviour.noTaskAvailable();
        } else {
            this.controllers.forEach(ProgramPhaseController::noTaskAvailable);
        }
    }
}

