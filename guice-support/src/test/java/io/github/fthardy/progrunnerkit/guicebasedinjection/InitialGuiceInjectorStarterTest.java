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
package io.github.fthardy.progrunnerkit.guicebasedinjection;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import io.github.fthardy.progrunnerkit.base.BaseMain;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class InitialGuiceInjectorStarterTest {
    
    static final int RETURN_CODE = 42;

    @Test
    void startGuiceBasedProgram() {

        int exitCode = BaseMain.startProgram(new String[]{});
        assertEquals(RETURN_CODE, exitCode);
    }
    
    @Test
    void No_implemenations_found() {

        InitialGuiceInjectorStarter starter = new InitialGuiceInjectorStarter();
        
        assertThrows(IllegalStateException.class, () -> 
                starter.createInjectorAndStartProgramEntryPoint(Collections.emptyList(), new String[] {}));
    }

    public static class ModuleProviderForTesting implements InitialGuiceModuleProviderService {
        
        public static class SomeServiceOrWhatever {
            
            int returnCode() {
                return RETURN_CODE;
            }
        }
    
        public static final class CommandLineExecutorImpl implements GuiceBasedCommandLineExecutor {
    
            @Inject
            private SomeServiceOrWhatever serviceOrWhatever;
    
            @Override
            public int execute(String[] args) {
                return this.serviceOrWhatever.returnCode();
            }
        }
        
        public static final class TestModule extends AbstractModule {
            
            @Override
            protected void configure() {
                bind(GuiceBasedCommandLineExecutor.class).to(CommandLineExecutorImpl.class).asEagerSingleton();
            }
        }
        
        @Override
        public Module createInitialGuiceModule() {
            return new TestModule();
        }
    }
}