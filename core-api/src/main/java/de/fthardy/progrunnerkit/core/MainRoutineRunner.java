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
package de.fthardy.progrunnerkit.core;

import java.util.List;
import java.util.Objects;

/**
 * A proxy implementation of a main routine which intercepts any kind of {@link RuntimeException} and validates 
 * the returned status code of the proxid main routine.
 *
 * @see StartUpRoutineRunner.ExceptionHandler
 */
public class MainRoutineRunner implements MainRoutine {

    /**
     * The interface definition for an exception handler used by a {@link MainRoutineRunner}.
     * 
     * @see MainRoutineRunner
     */
    public interface ExceptionHandler {

        /**
         * Handles any exception thrown by the main routine.
         *
         * @param exception the thrown exception.
         *
         * @return an exit code which is not allowed to be a {@link ProgramStatusCodeProvider#success()}. A failure code has to be returned that must not collide
         * with any of the internal status codes defined by the {@link ProgramStatusCodeProvider} implementation in use execept of
         * {@link ProgramStatusCodeProvider#mainFailure()}.
         */
        int handleExceptionFromMainRoutine(Exception exception);
    }
    
    private final MainRoutine mainRoutine;
    private final ExceptionHandler exceptionHandler;
    private final ProgramStatusCodeProvider statusCodeProvider;

    /**
     * Creates a new instance of this runner proxy.
     * 
     * @param mainRoutine the main routine to run.
     * @param exceptionHandler the exception handler.
     * @param statusCodeProvider the status code provider.
     */
    public MainRoutineRunner(
            MainRoutine mainRoutine, ExceptionHandler exceptionHandler, ProgramStatusCodeProvider statusCodeProvider) {
        this.mainRoutine = Objects.requireNonNull(mainRoutine);
        this.exceptionHandler = Objects.requireNonNull(exceptionHandler);
        this.statusCodeProvider = Objects.requireNonNull(statusCodeProvider);
    }
    
    @Override
    public int execute(List<String> inputArguments) {
        int exitCode;
        try {
            exitCode = mainRoutine.execute(inputArguments);
            return isValidMainExitCode(exitCode) ? exitCode : statusCodeProvider.invalidMainExitCode();
        } catch (Exception e) {
            exitCode = exceptionHandler.handleExceptionFromMainRoutine(e);
            return exitCode == statusCodeProvider.success() ? statusCodeProvider.invalidMainFailureCode() :
                    isValidMainFailureCode(exitCode) ? exitCode : statusCodeProvider.invalidMainFailureCode();
        }
    }

    boolean isValidMainExitCode(int code) {
        return code == statusCodeProvider.success() || code == statusCodeProvider.mainFailure() || isNotAnInternalFailureCode(code);
    }

    boolean isValidMainFailureCode(int code) {
        return code == statusCodeProvider.mainFailure() || isNotAnInternalFailureCode(code);
    }

    private boolean isNotAnInternalFailureCode(int code) {
        return !statusCodeProvider.allCodes().contains(code);
    }
}
