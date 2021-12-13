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
package io.github.fthardy.progrunnerkit.base;

import io.github.fthardy.progrunnerkit.core.ProgramExecutionContext;
import io.github.fthardy.progrunnerkit.core.ProgramPartEntryPoint;

import java.util.Objects;

/**
 * A decorator implementation for a program part which separates exception handling by intercepting any kind of {@link RuntimeException} passing it to an
 * implemenation of {@link ExceptionHandler}.
 * <p>
 * If the exception handler itself throws an exception it is not handled but thrown to the caller/owner of the decorator.
 * </p>
 * 
 * @see ExceptionHandler
 */
public final class ExceptionInterceptingProgramPartEntryPoint implements ProgramPartEntryPoint {

    /**
     * The interface definition for an exception handler used by a {@link ExceptionInterceptingProgramPartEntryPoint}.
     * 
     * @see ExceptionInterceptingProgramPartEntryPoint
     */
    public interface ExceptionHandler {

        /**
         * Handles any exception thrown by the delegate entry point.
         *
         * @param exception the thrown exception.
         *
         * @return a status code.
         */
        int handleExceptionFromMainRoutine(Exception exception);
    }
    
    private final ProgramPartEntryPoint programPartEntryPoint;
    private final ExceptionHandler exceptionHandler;

    /**
     * Creates a new instance of this runner proxy.
     * 
     * @param programPart the program part to delegate to.
     * @param exceptionHandler the exception handler.
     */
    public ExceptionInterceptingProgramPartEntryPoint(ProgramPartEntryPoint programPart, ExceptionHandler exceptionHandler) {
        this.programPartEntryPoint = Objects.requireNonNull(programPart);
        this.exceptionHandler = Objects.requireNonNull(exceptionHandler);
    }

    @Override
    public int execute(ProgramExecutionContext context) {
        try {
            return this.programPartEntryPoint.execute(context);
        } catch (Exception e) {
            return this.exceptionHandler.handleExceptionFromMainRoutine(e);
        }
    }
}
