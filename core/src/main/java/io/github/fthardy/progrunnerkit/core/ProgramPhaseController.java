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

/**
 * Generic interface definition for a program phase controller.
 * <p>
 * This generic type defines call back methods for a program phase.
 * </p>
 * 
 * @see Main#main(String[]) 
 */
public interface ProgramPhaseController {

    /**
     * Is called when the program phase starts.
     */
    default void onPhaseBegin() {}

    /**
     * Is called when the program phase ends.
     */
    default void onPhaseEnd() {}

    /**
     * Is called before a particular program phase task is going to be executed.
     * 
     * @param taskID the identifier of the task to be run.
     */
    default void beforeTaskExecution(String taskID) {}

    /**
     * Is called after a particular program phase task has been executed.
     * 
     * @param taskID the identifier of the finished task.
     */
    default void afterTaskExecution(String taskID) {}
    
    /**
     * Is called when a program phase task throws an exception.
     *
     * @param fromTaskId the identifier of the task which has thrown the exception.
     * @param exception the exception thrown by the task.
     *                     
     * @return {@code true} to end the phase immediately. Otherwise {@code false} to continue with the next task of the phase (if there is one).
     * 
     * @see ProgramPhaseTask#getIdentifier()
     */
    default boolean onExceptionFromTask(String fromTaskId, RuntimeException exception){
        System.out.printf("The task with identifier [%s] has thrown an exception:", fromTaskId);
        System.out.println();
        exception.printStackTrace();
        System.out.println();
        return false;
    }

    /**
     * Is called when no task is available for the phase.
     */
    default void noTaskAvailable(){
        System.out.println("No tasks found for program phase!");
        System.out.println();
    }
}
