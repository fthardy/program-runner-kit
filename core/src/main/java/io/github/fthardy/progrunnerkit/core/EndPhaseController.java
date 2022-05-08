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
 * Defines the semantic type interface for an end phase controller. The technical interface is provided by
 * {@link ProgramPhaseController}.
 * <p>
 * Implement this interface to define a particular start phase controller implementation. A {@link ServiceImplProvider} will collect all implementations which
 * are available through a service configuration file.
 * </p>
 *
 * @see Main#main(String[])
 * @see ServiceImplProvider
 */
public interface EndPhaseController extends ProgramPhaseController {

    /**
     * The default implemenation prints a message with the task-identifier and the exception stacktrace and returns
     * {@code false} to make sure all following tasks can execute.
     *
     * @param fromTaskId the identifier of the start phase task which has thrown the exception.
     * @param exception the exception thrown by the task.
     *
     * @return by default {@code false}.
     */
    default boolean onExceptionFromTask(String fromTaskId, RuntimeException exception) {
        System.out.printf("The end phase task with identifier [%s] has thrown an exception:", fromTaskId);
        System.out.println("----------------------------------------------------------------------------------------------------");
        exception.printStackTrace();
        System.out.println("----------------------------------------------------------------------------------------------------");
        return false;
    }

    @Override
    default void noTaskAvailable() {
        System.out.println("No tasks found for end phase!");
        System.out.println();
    }
}
