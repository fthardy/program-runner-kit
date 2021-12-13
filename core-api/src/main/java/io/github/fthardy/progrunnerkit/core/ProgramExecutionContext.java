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
import java.util.Optional;

/**
 * Interface definition for a program execution context.
 * <p>
 * Provides the status code provider, the input arguments from the command line and the status code from a previously executed part. 
 * </p>
 */
public interface ProgramExecutionContext {

    /**
     * @return the immutable list of the input arguments from the command line.
     */
    List<String> getCommandLineInputArguments();

    /**
     * @return an optional which contains the status code returned by a previously executed program part. If there is no status code the optional is empty.
     */
    Optional<Integer> getLastStatusCode();

    /**
     * Creates a new context instance from the receiving context instance with a new last status code.
     * 
     * @param statusCode the new status code.
     *                   
     * @return the new context instance.
     */
    ProgramExecutionContext createNewInstanceWithStatusCode(Integer statusCode);
}
