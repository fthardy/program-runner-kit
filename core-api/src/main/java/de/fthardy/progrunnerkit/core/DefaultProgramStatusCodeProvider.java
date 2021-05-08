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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The implementation for the default set of status codes.
 */
public class DefaultProgramStatusCodeProvider implements ProgramStatusCodeProvider {

    @Override
    public int startUpFailure() {
        return 200;
    }

    @Override
    public int invalidStartUpExitCode() {
        return 201;
    }

    @Override
    public int invalidStartUpFailureCode() {
        return 202;
    }

    @Override
    public int mainFailure() {
        return 300;
    }

    @Override
    public int invalidMainExitCode() {
        return 301;
    }

    @Override
    public int invalidMainFailureCode() {
        return 302;
    }

    @Override
    public int shutDownFailure() {
        return 400;
    }

    @Override
    public int invalidShutDownExitCode() {
        return 401;
    }

    @Override
    public int invalidShutDownFailureCode() {
        return 402;
    }

    @Override
    public Set<Integer> allCodes() {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                success(),
                startUpFailure(),
                invalidStartUpExitCode(),
                invalidStartUpFailureCode(),
                mainFailure(),
                invalidMainExitCode(),
                invalidMainFailureCode(),
                shutDownFailure(),
                invalidShutDownExitCode(),
                invalidShutDownFailureCode()
        )));
    }
}
