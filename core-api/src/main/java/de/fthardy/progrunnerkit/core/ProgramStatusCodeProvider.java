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

import java.util.Set;

/**
 * The definition of the interface for the provider of the internally used status codes.
 * <p>
 * Make sure that only one particular implementation of this status code provider is used in context of a program 
 * runner execution. 
 * </p>
 * 
 * @see ProgramRunner
 */
public interface ProgramStatusCodeProvider {

    /**
     * @return the status code for success which is (and should be always) by default 0. 
     */
    default int success() {
        return 0;
    }

    /**
     * @return the status code which indicates that the startup-routine has failed.
     */
    int startUpFailure();

    /**
     * @return the status code which indicates that the startup-routine has returned an invalid exit code.
     */
    int invalidStartUpExitCode();

    /**
     * @return the status code which indicates that startup-routine has thrown an exception and the exception handler
     * has returned an invalid exit code. 
     */
    int invalidStartUpFailureCode();

    /**
     * @return the status code which indicates that the main-routine has failed.
     */
    int mainFailure();

    /**
     * @return the status code which indicates that the main-routine has returned an invalid exit code.
     */
    int invalidMainExitCode();

    /**
     * @return the status code which indicates that main-routine has thrown an exception and the exception handler has
     * returned an invalid exit code. 
     */
    int invalidMainFailureCode();

    /**
     * @return the status code which indicates that the shutdown-routine has failed.
     */
    int shutDownFailure();

    /**
     * @return the status code which indicates that the shutdown-routine has returned an invalid exit code.
     */
    int invalidShutDownExitCode();

    /**
     * @return the status code which indicates that shutdown-routine has thrown an exception and the exception handler
     * has returned an invalid exit code. 
     */
    int invalidShutDownFailureCode();

    /**
     * @return a set with all codes that the receiving instance is providing.
     */
    Set<Integer> allCodes();
}
