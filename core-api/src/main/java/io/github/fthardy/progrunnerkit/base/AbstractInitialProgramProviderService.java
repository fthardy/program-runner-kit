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

import io.github.fthardy.progrunnerkit.core.BasicProgramExecutionContext;
import io.github.fthardy.progrunnerkit.core.ProgramExecutionContext;
import io.github.fthardy.progrunnerkit.core.ProgramStatusCodes;

/**
 * An abstract base implemenation for a {@link InitialProgramProviderService}.
 * <p>
 * The method {@link #createInitialProgramExecutionContext(String[])} is implemented as a template method which uses 
 * {@link #createInitialProgramExecutionContext(String[], ProgramStatusCodes)} and {@link #createProgramStatusCodeProvider()}.
 * </p>
 */
public abstract class AbstractInitialProgramProviderService implements InitialProgramProviderService {

    @Override
    public ProgramExecutionContext createInitialProgramExecutionContext(String[] commandLineArguments) {
        return this.createInitialProgramExecutionContext(commandLineArguments, this.createProgramStatusCodeProvider());
    }

    /**
     * Invoked by {@link #createInitialProgramExecutionContext(String[])} to create a context instance.
     * <p>
     * Can be overidden to provide a different implemenation of a program execution context. 
     * </p>
     * 
     * @param commandLineArguments the command line arguments from {@link #createInitialProgramExecutionContext(String[])}.
     * @param statusCodeProvider the status code provider provided by {@link #createProgramStatusCodeProvider()}. 
     * 
     * @return by default an instance of {@link BasicProgramExecutionContext}.
     */
    protected ProgramExecutionContext createInitialProgramExecutionContext(String[] commandLineArguments, ProgramStatusCodes statusCodeProvider) {
        return new BasicProgramExecutionContext(statusCodeProvider, commandLineArguments, null);
    }

    /**
     * Invoked by {@link #createInitialProgramExecutionContext(String[])} to create the program status code provider.
     * <p>
     * Can be overidden to provide a different implemenation of a program status code provider. 
     * </p>
     * 
     * @return by default an anonymous instance of {@link ProgramStatusCodes}.
     */
    protected ProgramStatusCodes createProgramStatusCodeProvider() {
        return new ProgramStatusCodes() {};
    }
}
