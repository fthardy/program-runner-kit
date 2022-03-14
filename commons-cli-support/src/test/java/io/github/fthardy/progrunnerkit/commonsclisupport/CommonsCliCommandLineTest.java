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
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CommonsCliCommandLineTest {

    @Test
    void Option_switch_with_short_name_present_no_further_args() throws CommandLineParseException {

        Options options = new Options();
        options.addOption("t", "test", false,"Test option.");

        CommandLineParser commandLineParser = new CommonsCliCommandLineParser(options);

        CommandLine commandLine = commandLineParser.parseArguments(new String[]{"-t"});
        
        assertTrue(commandLine.isSet("t"));
        assertTrue(commandLine.isSet("test"));
        assertFalse(commandLine.isSet("foo"));
        
        assertTrue(commandLine.getUnparsedArguments().isEmpty());
        
        List<String> arguments = commandLine.getArguments();
        assertEquals(1, arguments.size());
        assertEquals("-t", arguments.get(0));
    }

    @Test
    void Option_switch_with_long_name_present_with_additional_args() throws CommandLineParseException {

        Options options = new Options();
        options.addOption("t", "test", false,"Test option.");

        CommandLineParser commandLineParser = new CommonsCliCommandLineParser(options);

        CommandLine commandLine = commandLineParser.parseArguments(new String[]{"--test", "foo", "bar"});

        assertTrue(commandLine.isSet("t"));
        assertTrue(commandLine.isSet("test"));
        assertFalse(commandLine.isSet("foo"));
        
        assertFalse(commandLine.getUnparsedArguments().isEmpty());
        List<String> unparsedArguments = commandLine.getUnparsedArguments();
        assertEquals(2, unparsedArguments.size());
        assertThat(unparsedArguments).containsExactly("foo", "bar");
    }
    
    @Test
    void Option_with_argument() throws CommandLineParseException {
        
        Options options = new Options();
        options.addOption("p", "param", true, "Parameter.");

        CommandLineParser commandLineParser = new CommonsCliCommandLineParser(options);

        CommandLine commandLine = commandLineParser.parseArguments(new String[]{"-p", "foo"});
        
        assertThat(commandLine.getParameterValue("p")).contains("foo");
        assertThat(commandLine.getParameterList("p")).contains("foo");
        assertTrue(commandLine.getUnparsedArguments().isEmpty());
    }
    
    @Test
    void Option_with_Properties() throws CommandLineParseException {
        
        Options options = new Options();
        options.addOption(Option.builder("D").hasArgs().valueSeparator('=').build());
        
        CommandLineParser commandLineParser = new CommonsCliCommandLineParser(options);
        
        CommandLine commandLine = commandLineParser.parseArguments(new String[]{"-D", "foo=ping", "-D", "bar=pong"});

        Optional<Properties> optionalProperties = commandLine.getParameterValueAsType(Properties.class, "D");
        assertTrue(optionalProperties.isPresent());

        Properties properties = optionalProperties.get();
        assertThat(properties).hasSize(2);
        assertThat(properties).containsKey("foo");
        assertThat(properties).containsKey("bar");
        assertThat(properties.get("foo")).isEqualTo("ping");
        assertThat(properties.get("bar")).isEqualTo("pong");
    }
    
    @Test
    void Option_with_Properties_not_present() throws CommandLineParseException {

        Options options = new Options();
        options.addOption(Option.builder("D").hasArgs().valueSeparator('=').build());
        options.addOption("h", "Help");

        CommandLineParser commandLineParser = new CommonsCliCommandLineParser(options);

        CommandLine commandLine = commandLineParser.parseArguments(new String[]{"-h"});

        assertTrue(commandLine.isSet("h"));
        assertFalse(commandLine.isSet("D"));
        
        Optional<Properties> optionalProperties = commandLine.getParameterValueAsType(Properties.class, "D");
        assertFalse(optionalProperties.isPresent());
    }
    
    @Test
    void Get_unsupported_parameter_type() throws CommandLineParseException {

        Options options = new Options();
        options.addOption(Option.builder("D").hasArgs().valueSeparator('=').build());
        options.addOption("h", "Help");

        CommandLineParser commandLineParser = new CommonsCliCommandLineParser(options);

        CommandLine commandLine = commandLineParser.parseArguments(new String[]{"-D", "foo=ping", "-D", "bar=pong"});
        
        assertThrows(ClassCastException.class, () -> commandLine.getParameterValueAsType(Map.class, "D"));
    }
}