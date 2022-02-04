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

import io.github.fthardy.progrunnerkit.cliapi.CommandLineParseException;
import io.github.fthardy.progrunnerkit.cliapi.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonsCliCommandLineParserTest {

    @Test
    void Parser_cannot_be_null() {
        assertThrows(NullPointerException.class, () -> new CommonsCliCommandLineParser(null, null));
    }

    @Test
    void Options_cannot_be_null() {
        assertThrows(NullPointerException.class, () -> new CommonsCliCommandLineParser(new DefaultParser(), null));
    }
    
    @Test
    void Description_printer_cannot_be_null() {
        assertThrows(NullPointerException.class, () -> 
                new CommonsCliCommandLineParser(new DefaultParser(), new Options(), (CommonsCliCommandLineParser.CommandLineDescriptionPrinter) null));
    }

    @Test
    void Parsing_null_argument_list_is_not_allowed() {
        
        Options options = new Options();
        options.addOption("test", "Test option.");
        org.apache.commons.cli.CommandLineParser parser = new DefaultParser();

        CommandLineParser commandLineParser = new CommonsCliCommandLineParser(
                parser, options, new DefaultCommandLineDescriptionPrinter(""));
        
        assertThrows(NullPointerException.class, () -> commandLineParser.parseArguments(null));
    }
    
    @Test
    void Exception_from_CommonsCLIParser() {
        Options options = new Options();
        options.addOption("test", "Test option.");
        org.apache.commons.cli.CommandLineParser parser = new DefaultParser();

        CommandLineParser commandLineParser = new CommonsCliCommandLineParser(
                parser, options, new DefaultCommandLineDescriptionPrinter(""));

        assertThrows(CommandLineParseException.class, () -> commandLineParser.parseArguments(new String[] {"-bla"}));
    }
}