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
 * The default implementation of a main routine.
 * <p>
 * When the {@link #execute(List)} (List)} method is invoked the startup routine it is going to be executed if it exists. If
 * the startup routine returns with a status code that doesn't equal {@link ProgramStatusCodeProvider#success()} then
 * the method ends and that status code is returned. If the startup routine returns with a status code that is equal to
 * {@link ProgramStatusCodeProvider#success()} then the main routine is executed next. If no shutdown routine exists 
 * the method ends and the status code of the main routine is returned. If a shutdown routine exists it is going to 
 * be executed by passing in the status code from the main routine. When the shutdown routine returns its status code
 * is finally returned.
 * </p>
 * <p>
 * There is no exception handling in {@link #execute(List)} (List)}. If any kind of runtime exception is thrown during the
 * execution process the method ends and the exception is passed to the caller. 
 * </p>
 */
public class DefaultDelegatingMainRoutine implements MainRoutine {
    
    private final StartUpRoutine startUpRoutine;
    private final MainRoutine mainRoutine;
    private final ShutDownRoutine shutDownRoutine;
    private final ProgramStatusCodeProvider statusCodeProvider;

    /**
     * Creates a new instance of this program runner.
     * 
     * @param startUpRoutine a startup routine. Can be null if not needed.
     * @param mainRoutine the main routine. Is not allowed to be null.
     * @param shutDownRoutine a shutdown routine. Can be null if not needed.
     * @param statusCodeProvider a status code provider implementation. Is not allowed to be null.
     */
    public DefaultDelegatingMainRoutine(StartUpRoutine startUpRoutine,
                                        MainRoutine mainRoutine,
                                        ShutDownRoutine shutDownRoutine,
                                        ProgramStatusCodeProvider statusCodeProvider) {
        this.startUpRoutine = startUpRoutine;
        this.mainRoutine = Objects.requireNonNull(
                mainRoutine, "Routine provider must provide a main routine! null is not allowed.");
        this.shutDownRoutine = shutDownRoutine;
        this.statusCodeProvider = Objects.requireNonNull(statusCodeProvider);
    }
    
    @Override
    public int execute(List<String> inputArguments) {
        if (startUpRoutine != null) {
            int exitCode = startUpRoutine.execute(inputArguments);
            if (exitCode != statusCodeProvider.success()) {
                return exitCode;
            }
        }

        int exitCode = mainRoutine.execute(inputArguments);

        if (shutDownRoutine != null) {
            exitCode = shutDownRoutine.execute(inputArguments, exitCode);
        }

        return exitCode;
    }
}
