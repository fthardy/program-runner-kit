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

import io.github.fthardy.progrunnerkit.cliapi.CommandLineHelpTextPrinter;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.util.Objects;

/**
 * Default implementation for a help text printer.
 */
public class DefaultCommandLineHelpTextPrinter implements CommandLineHelpTextPrinter {

    private final Options options;
    private final String commandLineSyntaxDescriptionText;
    private final String header;
    private final String footer;

    /**
     * Create a new instance of this printer.
     * 
     * @param options the option for the parser.
     * @param commandLineSyntaxDescriptionText the text of the command line syntax description line.
     *                                        
     */
    public DefaultCommandLineHelpTextPrinter(Options options, String commandLineSyntaxDescriptionText) {
        this(options, commandLineSyntaxDescriptionText, "", "");
    }

    /**
     * Create a new instance of this printer.
     * 
     * @param options the option for the printer
     * @param commandLineSyntaxDescriptionText the text of the command line syntax description line.
     * @param header a header text.
     * @param footer a footer text.
     */
    public DefaultCommandLineHelpTextPrinter(Options options, String commandLineSyntaxDescriptionText, String header,
                                             String footer) {
        this.options = Objects.requireNonNull(options);
        this.commandLineSyntaxDescriptionText = Objects.requireNonNull(commandLineSyntaxDescriptionText);
        this.header = Objects.requireNonNull(header);
        this.footer = Objects.requireNonNull(footer);
    }

    @Override
    public void printCommandLineHelpText() {
        new HelpFormatter().printHelp(this.commandLineSyntaxDescriptionText, this.header, this.options, this.footer);
    }
}
