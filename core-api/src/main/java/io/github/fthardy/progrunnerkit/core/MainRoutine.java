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
 * Interface definition for the program main routine.
 * <p>
 * Provides an entrypoint for the main functionality of a program.
 * </p>
 */
public interface MainRoutine {

    /**
     * Executes the main routine of the program.
     * 
     * @param inputArguments an immutable list of the input arguments.
     *
     * @return a status code which must be equal to {@link ProgramStatusCodeProvider#success()} to indicate a 
     * successful execution. If the main routine execution fails a status code which is not equal to
     * {@link ProgramStatusCodeProvider#success()} must be returned. However, the status code can be equal to 
     * {@link ProgramStatusCodeProvider#mainFailure()} but can be any other code thatdoesn't collide with any other code
     * from {@link ProgramStatusCodeProvider}.
     */
    int execute(List<String> inputArguments);
}
