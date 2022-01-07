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
import io.github.fthardy.progrunnerkit.cliapi.CommandLineParseException;
import io.github.fthardy.progrunnerkit.cliapi.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Objects;

/**
 * The implementation of the commons-cli command line parser adapter. 
 */
public final class CommonsCliCommandLineParser implements CommandLineParser {
    
    private final org.apache.commons.cli.CommandLineParser parser;
    private final Options parserOptions;
    private final HelpTextWriter helpTextWriter;

    /**
     * Creates a new instance of this command line parser implementation.
     *
     * @param parser the commons-cli parser instance to use for parsing the command line.
     * @param options the options for parsing.
     * @param helpTextWriter the help text writer.
     */
    public CommonsCliCommandLineParser(org.apache.commons.cli.CommandLineParser parser, Options options, HelpTextWriter helpTextWriter) {
        this.parser = Objects.requireNonNull(parser);
        this.parserOptions = Objects.requireNonNull(options);
        this.helpTextWriter = Objects.requireNonNull(helpTextWriter);
    }
    
    @Override
    public CommandLine parseArguments(String[] arguments) throws CommandLineParseException {
        Objects.requireNonNull(arguments);
        try {
            return new CommonsCommandLine(arguments, this.parser.parse(this.parserOptions, arguments));
        } catch (ParseException e) {
            throw new CommandLineParseException(e); 
        }
    }
    
    @Override
    public void writeHelpTextToSystemOut() {
        this.helpTextWriter.writeHelpTextToSystemOut();
    }
}
