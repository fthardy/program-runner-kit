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
package io.github.fthardy.progrunnerkit.base;

import io.github.fthardy.progrunnerkit.core.CommandLineExecutor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseMainTest {

    public static final class CommandLineExecutorProviderServiceTestImpl implements CommandLineExecutorProviderService {
        
        static int statusCodeToReturn = 0;
        
        @Override
        public CommandLineExecutor getCommandLineExecutorImpl() {
            return context -> statusCodeToReturn;
        }
    }

    @Test
    void Should_load_implemenation_instance() {
        CommandLineExecutorProviderServiceTestImpl.statusCodeToReturn = 42;
        int exitCode = BaseMain.startProgram(new String[] {"foo"});
        assertEquals(CommandLineExecutorProviderServiceTestImpl.statusCodeToReturn, exitCode);
    }
}