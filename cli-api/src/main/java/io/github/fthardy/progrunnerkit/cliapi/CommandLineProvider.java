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
package io.github.fthardy.progrunnerkit.cliapi;

import java.util.List;
import java.util.Objects;

/**
 * A helper object, which parses given command line arguments and provides the resulting command line object for access.
 * <p>
 * Before the command line object is available {@link #parseCommandLineFrom(List)} has to be called first. Once the command line is parsed a 
 * </p>
 */
public class CommandLineProvider {
    
    private final CommandLineParser parser;

    private CommandLine commandLine;

    /**
     * Creates a new instance of this provider.
     * 
     * @param parser the command line parser for creating the command line object.
     */
    public CommandLineProvider(CommandLineParser parser) {
        this.parser = Objects.requireNonNull(parser);
    }

    /**
     * Parse the command line arguments.
     *
     * @param args the arguments to be parsed.
     *
     * @throws CommandLineParseException when parsing fails for some reason.
     * @throws IllegalStateException when parsing has already been done and a command line object is already available.
     */
    public void parseCommandLineFrom(List<String> args) throws CommandLineParseException {
        if (this.commandLine == null) {
            this.commandLine = this.parser.parseArguments(args);
        } else {
            throw new IllegalStateException("Command line has already been parsed and set!");
        }
    }

    /**
     * Provides access to the command line object.
     *
     * @return the command line object.
     *
     * @throws IllegalStateException when the command line object is not available. This might be because the parsing has not yet been done or failed with an
     * exception.
     */
    public CommandLine getCommandLine() {
        if (this.commandLine == null) {
            throw new IllegalStateException("No command line has been set!");
        }
        return this.commandLine;
    }
}