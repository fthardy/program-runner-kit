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

/**
 * Interface definition for the program shutdown routine.
 * <p>
 * Provides an entrypoint for any kind of tear down or cleanup functionality for a program.
 * </p>
 */
public interface ShutDownRoutine {

    /**
     * Execute the shutdown routine of the program. 
     * 
     * @param inputArguments an immutable list of the input arguments.
     * @param exitCodeFromMain the exit code from the main routine.
     *
     * @return a status code which must be equal to {@link ProgramStatusCodeProvider#success()} to indicate that the 
     * shutdown routine execution was successful. If the shutdown routine execution fails a status code unequal to
     * {@link ProgramStatusCodeProvider#success()} must be returned. However, the status code can be equal to
     * {@link ProgramStatusCodeProvider#shutDownFailure()} ()} but can be any other code that doesn't collide with any
     * other code from {@link ProgramStatusCodeProvider}
     */
    int execute(List<String> inputArguments, int exitCodeFromMain);
}
