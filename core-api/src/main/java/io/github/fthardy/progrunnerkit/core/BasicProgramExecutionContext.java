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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * An extendable base implemenation of the program execution context.
 */
public class BasicProgramExecutionContext implements ProgramExecutionContext {
    
    private final ProgramStatusCodes statusCodeProvider;
    private final List<String> commandLineInputArguments;
    private final Integer statusCode;

    /**
     * Create a new instance of this execution context.
     *
     * @param statusCodeProvider the status code provider implementation.
     * @param commandLineArguments the command line arguments.
     * @param statusCode a status code or {@code null}.
     */
    public BasicProgramExecutionContext(ProgramStatusCodes statusCodeProvider, List<String> commandLineArguments, Integer statusCode) {
        this.statusCodeProvider = Objects.requireNonNull(statusCodeProvider, "Undefined status code provider!");
        this.commandLineInputArguments = commandLineArguments;
        this.statusCode = statusCode;
    }
    
    /**
     * Create a new instance of this execution context.
     * 
     * @param statusCodeProvider the status code provider implementation.
     * @param commandLineArguments the command line arguments.
     * @param statusCode a status code or {@code null}.
     */
    public BasicProgramExecutionContext(ProgramStatusCodes statusCodeProvider, String[] commandLineArguments, Integer statusCode) {
        this(
                statusCodeProvider, 
                Arrays.asList(Objects.requireNonNull(commandLineArguments, "Undefined command line arguments! Provide at least an empty array.")),
                statusCode);
    }

    @Override
    public ProgramStatusCodes getStatusCodeProvider() {
        return this.statusCodeProvider;
    }

    @Override
    public List<String> getCommandLineInputArguments() {
        return this.commandLineInputArguments;
    }

    @Override
    public Optional<Integer> getStatusCode() {
        return Optional.ofNullable(this.statusCode);
    }

    @Override
    public ProgramExecutionContext createNewWithStatusCode(Integer statusCode) {
        return new BasicProgramExecutionContext(this.statusCodeProvider, this.commandLineInputArguments, statusCode);
    }
}
