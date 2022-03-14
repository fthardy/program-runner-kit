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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandLineProviderTest {
    
    @Mock
    private CommandLineParser parserMock;
    
    @AfterEach
    public void checkMocks() {
        verifyNoMoreInteractions(this.parserMock);
    }

    @Test
    public void Parsing_of_command_line_succeeds() {
        
        List<String> arguments = Collections.emptyList();
        
        CommandLine commandLineMock = mock(CommandLine.class);
        
        CommandLineProvider provider = new CommandLineProvider(this.parserMock);
        
        when(this.parserMock.parseArguments(arguments)).thenReturn(commandLineMock);
        
        provider.parseCommandLineFrom(arguments);
        assertSame(commandLineMock, provider.getCommandLine());
        
        verify(this.parserMock).parseArguments(arguments);
        verifyNoInteractions(commandLineMock);
    }

    @Test
    public void Parse_can_only_be_called_once() {
        
        List<String> arguments = Collections.emptyList();
        
        CommandLine commandLineMock = mock(CommandLine.class);
        
        CommandLineProvider provider = new CommandLineProvider(this.parserMock);
        
        when(this.parserMock.parseArguments(arguments)).thenReturn(commandLineMock);
        
        provider.parseCommandLineFrom(arguments);
        assertSame(commandLineMock, provider.getCommandLine());
        
        assertThrows(IllegalStateException.class, () -> provider.parseCommandLineFrom(arguments));
        
        verify(this.parserMock).parseArguments(arguments);
        verifyNoInteractions(commandLineMock);
    }
    
    @Test
    public void Parsing_of_command_line_fails() {

        List<String> arguments = Collections.emptyList();

        CommandLine commandLineMock = mock(CommandLine.class);

        CommandLineProvider provider = new CommandLineProvider(this.parserMock);

        CommandLineParseException exception = new CommandLineParseException("TEST");
        doThrow(exception).when(this.parserMock).parseArguments(arguments);

        assertThrows(CommandLineParseException.class, () -> provider.parseCommandLineFrom(arguments));
        
        assertThrows(IllegalStateException.class, provider::getCommandLine);

        verify(this.parserMock).parseArguments(arguments);
        verifyNoInteractions(commandLineMock);
    }
}