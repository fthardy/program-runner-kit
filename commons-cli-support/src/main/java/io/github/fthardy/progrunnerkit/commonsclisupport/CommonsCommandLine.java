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
package io.github.fthardy.progrunnerkit.commonsclisupport;

import io.github.fthardy.progrunnerkit.cliapi.CommandLine;

import java.util.*;

/**
 * The implementation of the commons-cli command line adapter. 
 */
public final class CommonsCommandLine implements CommandLine {
    
    private final List<String> commandLineArguments;
    private final org.apache.commons.cli.CommandLine commandLineObject;

    /**
     * Create a new instance of this command line implementation.
     * 
     * @param commandLineObject the command line object returned by the commons-cli parser.
     */
    public CommonsCommandLine(String[] args, org.apache.commons.cli.CommandLine commandLineObject) {
        this.commandLineArguments = Arrays.asList(Objects.requireNonNull(args));
        this.commandLineObject = Objects.requireNonNull(commandLineObject);
    }
    
    @Override
    public boolean isSet(String id) {
        return this.commandLineObject.hasOption(id);
    }

    @Override
    public List<String> getArguments() {
        return this.commandLineArguments;
    }

    @Override
    public List<String> getUnparsedArguments() {
        return this.commandLineObject.getArgList();
    }

    @Override
    public Optional<String> getParameterValue(String id) {
        return Optional.ofNullable(this.commandLineObject.getOptionValue(id));
    }

    @Override
    public List<String> getParameterList(String id) {
        // Parameter value lists are not supported by commons-cli
        Optional<String> parameterValue = this.getParameterValue(id);
        return parameterValue.map(Collections::singletonList).orElse(Collections.emptyList());
    }

    @Override
    public <T> Optional<T> getParameterValueAsType(Class<T> type, String id) {
        if (this.isSet(id)) {
            if (Properties.class.equals(type)) {
                // even tho the api says that a non-null is returned we do not rely on this
                return Optional.ofNullable(type.cast(this.commandLineObject.getOptionProperties(id)));
            } else {
                throw new ClassCastException(String.format("The type [%s] is not supported by commons-cli. Only the type [%s] is supported.",
                        type.getName(), Properties.class.getName()));
            }
        }
        return Optional.empty();
    }
}
