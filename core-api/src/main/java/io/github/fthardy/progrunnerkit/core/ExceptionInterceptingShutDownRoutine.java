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
 * A proxy implementation for a shutdown routine which intercepts any kind of {@link RuntimeException} and validates 
 * the returned status code of the proxied shutdown routine.
 */
public class ExceptionInterceptingShutDownRoutine implements ShutDownRoutine {

    /**
     * The interface definition for an exception handler used by a {@link ExceptionInterceptingShutDownRoutine}.
     *
     * @see ExceptionInterceptingShutDownRoutine
     */
    public interface ExceptionHandler {

        /**
         * Handles any exception thrown by the shutdown routine.
         *
         * @param exception the thrown exception.
         *
         * @return an exit code which might be a {@link ProgramStatusCodeProvider#success()}. In this case {@link DefaultDelegatingMainRoutine} will return the code which was the
         * outcome of the main routine execution. In any other case a failure code has to be returned that must not collide with any of the internal status codes
         * defined by the {@link ProgramStatusCodeProvider} implementation in use execept of {@link ProgramStatusCodeProvider#shutDownFailure()}.
         */
        int handleExceptionFromShutDownRoutine(Exception exception);
    }
    
    private final ShutDownRoutine shutDownRoutine;
    private final ExceptionHandler exceptionHandler;
    private final ProgramStatusCodeProvider statusCodeProvider;

    /**
     * Creates a new instance of this shutdown routine proxy.
     * 
     * @param shutDownRoutine the shutdown routine to run.
     * @param exceptionHandler the exception handler.
     * @param statusCodeProvider the status code provider.
     */
    public ExceptionInterceptingShutDownRoutine(
            ShutDownRoutine shutDownRoutine, ExceptionHandler exceptionHandler, ProgramStatusCodeProvider statusCodeProvider) {
        this.shutDownRoutine = Objects.requireNonNull(shutDownRoutine);
        this.exceptionHandler = Objects.requireNonNull(exceptionHandler);
        this.statusCodeProvider = Objects.requireNonNull(statusCodeProvider);
    }
    
    @Override
    public int execute(List<String> inputArguments, int exitCodeFromMain) {

        int exitCode;
        try {
            exitCode = shutDownRoutine.execute(inputArguments, exitCodeFromMain);
            return isValidShutDownExitCode(exitCode) ?
                    exitCode == statusCodeProvider.success() ? exitCodeFromMain : exitCode
                    : statusCodeProvider.invalidShutDownExitCode();
        } catch (Exception e) {
            exitCode = exceptionHandler.handleExceptionFromShutDownRoutine(e);
            return exitCode == statusCodeProvider.success() ? exitCodeFromMain :
                    isValidShutDownFailureCode(exitCode) ? exitCode : statusCodeProvider.invalidShutDownFailureCode();
        }
    }

    boolean isValidShutDownExitCode(int code) {
        return code == statusCodeProvider.success()
                || code == statusCodeProvider.shutDownFailure()
                || code == statusCodeProvider.mainFailure()
                || code == statusCodeProvider.invalidMainExitCode()
                || code == statusCodeProvider.invalidMainFailureCode()
                || isNotAnInternalFailureCode(code);
    }

    boolean isValidShutDownFailureCode(int code) {
        return code == statusCodeProvider.shutDownFailure() || isNotAnInternalFailureCode(code);
    }

    private boolean isNotAnInternalFailureCode(int code) {
        return !statusCodeProvider.allCodes().contains(code);
    }
}
