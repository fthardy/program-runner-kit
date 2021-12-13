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
 * An extendable base implemenation of the program execution context.
 */
public class BasicProgramExecutionContext implements ProgramExecutionContext {
    
    private final List<String> commandLineInputArguments;
    private final Integer statusCode;

    /**
     * Create a new instance of this execution context.
     *
     * @param commandLineArguments the command line arguments.
     * @param statusCode a status code or {@code null}.
     */
    public BasicProgramExecutionContext(List<String> commandLineArguments, Integer statusCode) {
        this.commandLineInputArguments = Collections.unmodifiableList(new ArrayList<>(commandLineArguments));
        this.statusCode = statusCode;
    }
    
    /**
     * Create a new instance of this execution context.
     * 
     * @param commandLineArguments the command line arguments.
     * @param statusCode a status code or {@code null}.
     */
    public BasicProgramExecutionContext(String[] commandLineArguments, Integer statusCode) {
        this(Arrays.asList(Objects.requireNonNull(commandLineArguments, "Undefined command line arguments! Provide at least an empty array.")), statusCode);
    }

    @Override
    public List<String> getCommandLineInputArguments() {
        return this.commandLineInputArguments;
    }

    @Override
    public Optional<Integer> getLastStatusCode() {
        return Optional.ofNullable(this.statusCode);
    }

    @Override
    public ProgramExecutionContext createNewInstanceWithStatusCode(Integer statusCode) {
        return new BasicProgramExecutionContext(this.commandLineInputArguments, statusCode);
    }
}
