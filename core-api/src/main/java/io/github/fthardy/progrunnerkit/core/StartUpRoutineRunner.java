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
 * A proxy implementation of a startup routine which intercepts any kind of {@link RuntimeException} and validates 
 * the returned status code of the proxid startup routine.
 * 
 * @see ExceptionHandler
 */
public class StartUpRoutineRunner implements StartUpRoutine {

    /**
     * The interface definition for an exception handler used by a {@link StartUpRoutineRunner}.
     * 
     * @see StartUpRoutineRunner
     */
    public interface ExceptionHandler {

        /**
         * Handles any exception thrown by the startup routine.
         *
         * @param exception the thrown exception.
         *
         * @return an exit code which might be a {@link ProgramStatusCodeProvider#success()} to indicate that the program can proceed to the {@link MainRoutine}. In
         * any other case a failure code has to be returned that must not collide with any of the internal status codes defined by the
         * {@link ProgramStatusCodeProvider} implementation in use execept of {@link ProgramStatusCodeProvider#startUpFailure()}.
         */
        int handleExceptionFromStartUpRoutine(Exception exception);
    }
    
    private final StartUpRoutine startUpRoutine;
    private final ExceptionHandler exceptionHandler;
    private final ProgramStatusCodeProvider statusCodeProvider;

    /**
     * Create a new instance of this runner proxy.
     * 
     * @param startUpRoutine the startup routine to run.
     * @param exceptionHandler the exception handler instance.
     * @param statusCodeProvider the status code provider.
     */
    public StartUpRoutineRunner(
            StartUpRoutine startUpRoutine,
            ExceptionHandler exceptionHandler,
            ProgramStatusCodeProvider statusCodeProvider) {
        this.startUpRoutine = Objects.requireNonNull(startUpRoutine);
        this.exceptionHandler = Objects.requireNonNull(exceptionHandler);
        this.statusCodeProvider = Objects.requireNonNull(statusCodeProvider);
    }
    
    @Override
    public int execute(List<String> inputArguments) {
        int exitCode;
        try {
            exitCode = startUpRoutine.execute(inputArguments);
            return isValidStartUpExitCode(exitCode) ? exitCode : statusCodeProvider.invalidStartUpExitCode();
        } catch (Exception e) {
            exitCode = exceptionHandler.handleExceptionFromStartUpRoutine(e);
            return exitCode == statusCodeProvider.success() ? exitCode :
                    isValidStartUpFailureCode(exitCode) ? exitCode : statusCodeProvider.invalidStartUpFailureCode();
        }
    }

    boolean isValidStartUpExitCode(int code) {
        return code == statusCodeProvider.success()
                || code == statusCodeProvider.startUpFailure()
                || isNotAnInternalFailureCode(code);
    }

    boolean isValidStartUpFailureCode(int code) {
        return code == statusCodeProvider.startUpFailure() || isNotAnInternalFailureCode(code);
    }

    private boolean isNotAnInternalFailureCode(int code) {
        return !statusCodeProvider.allCodes().contains(code);
    }
}
