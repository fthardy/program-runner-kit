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
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.List;
import java.util.Objects;

/**
 * The implementation of the commons-cli command line parser adapter. 
 */
public class CommonsCliCommandLineParser implements CommandLineParser {

    /**
     * Represents the printer interface for printing the command line description to the console.
     */
    public interface CommandLineDescriptionPrinter {

        /**
         * Print the command line description (help text) to the console.
         * 
         * @param options the options of the command line.
         */
        void printCommandLineDescriptionForOptions(Options options);
    }
    
    private final org.apache.commons.cli.CommandLineParser parser;
    private final Options parserOptions;

    /**
     * Creates a new instance of this command line parser implementation.
     *
     * @param parser the commons-cli parser instance to use for parsing the command line.
     * @param options the options for parsing.
     */
    public CommonsCliCommandLineParser(org.apache.commons.cli.CommandLineParser parser, Options options) {
        this.parser = Objects.requireNonNull(parser);
        this.parserOptions = Objects.requireNonNull(options);
    }

    /**
     * Creates a new instance of this command line parser implementation.
     * <p>
     * A {@link DefaultCommandLineHelpTextPrinter} is created implicitly.
     * </p>
     * 
     * @param parser the commons-cli parser instance to use for parsing the command line.
     * @param options the options for parsing.
     * @param commandLineSyntaxDescriptionText the syntax description of the command line call.
     */
    public CommonsCliCommandLineParser(org.apache.commons.cli.CommandLineParser parser, Options options, String commandLineSyntaxDescriptionText) {
        this(parser, options);
    }

    /**
     * Creates a new instance of this command line parser implementation.
     * <p>
     * A default parser instance with no partial matching is created implicitly.
     * </p>
     *
     * @param options the options for parsing.
     */
    public CommonsCliCommandLineParser(Options options) {
        this(new DefaultParser(false), options);
    }
    
    @Override
    public CommandLine parseArguments(String[] arguments) throws CommandLineParseException {
        Objects.requireNonNull(arguments);
        try {
            return new CommonsCliCommandLine(arguments, this.parser.parse(this.parserOptions, arguments));
        } catch (ParseException e) {
            throw new CommandLineParseException(e); 
        }
    }

    @Override
    public CommandLine parseArguments(List<String> arguments) throws CommandLineParseException {
        return this.parseArguments(arguments.toArray(new String[0]));
    }
}
