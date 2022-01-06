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
package io.github.fthardy.progrunnerkit.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ThreePhaseCommandLineExecutorTest {
    
    @Mock
    private CommandLineExecutor startUpPhaseMock;
    @Mock
    private CommandLineExecutor mainPhaseMock;
    @Mock
    private CommandLineExecutor shutDownPhaseMock;
    
    private ThreePhaseCommandLineExecutor executor;
    
    @BeforeEach
    void setUp() {
        this.executor = new ThreePhaseCommandLineExecutor(this.startUpPhaseMock, this.mainPhaseMock, this.shutDownPhaseMock);
    }

    @AfterEach
    void checkMocks() {
        verifyNoMoreInteractions(this.startUpPhaseMock, this.mainPhaseMock, this.shutDownPhaseMock);
    }

    @Test
    void Input_arguments_list_is_mandatory() {
        assertThrows(NullPointerException.class, () -> new ThreePhaseCommandLineExecutor(
                null, null, null));
    }
    
    @Test
    void Main_routine_is_mandatory() {
        assertThrows(NullPointerException.class, () -> new ThreePhaseCommandLineExecutor(
                null, null, null));
    }
    
    @Test
    void No_startup_no_shutdown() {
        
        String[] args = new String[] {};
        
        int resultCode = 42;
        when(this.mainPhaseMock.execute(args)).thenReturn(resultCode);

        int statusCode = new ThreePhaseCommandLineExecutor(null, this.mainPhaseMock, null).execute(args);
        assertThat(statusCode).isEqualTo(resultCode);

        verify(this.mainPhaseMock).execute(args);
    }

    @Test
    void Successful_startup_no_shutdown() {

        String[] args = new String[] {};

        int resultCode = 42;
        when(this.startUpPhaseMock.execute(args)).thenReturn(0);
        when(this.mainPhaseMock.execute(args)).thenReturn(resultCode);

        int statusCode = new ThreePhaseCommandLineExecutor(this.startUpPhaseMock, this.mainPhaseMock, null).execute(args);
        assertThat(statusCode).isEqualTo(resultCode);

        InOrder callOrder = inOrder(this.startUpPhaseMock, this.mainPhaseMock);
        callOrder.verify(this.startUpPhaseMock).execute(args);
        callOrder.verify(this.mainPhaseMock).execute(args);
    }

    @Test
    void Failed_at_startup() {

        String[] args = new String[] {};

        int startupResultCode = 42;
        int shutdownResultCode = 99;
        when(this.startUpPhaseMock.execute(args)).thenReturn(startupResultCode);
        when(this.shutDownPhaseMock.execute(args)).thenReturn(shutdownResultCode);

        int statusCode = this.executor.execute(args);
        assertThat(statusCode).isEqualTo(shutdownResultCode);

        InOrder callOrder = inOrder(this.startUpPhaseMock, this.mainPhaseMock, this.shutDownPhaseMock);
        callOrder.verify(this.startUpPhaseMock).execute(args);
        callOrder.verify(this.mainPhaseMock, never()).execute(args);
        callOrder.verify(this.shutDownPhaseMock).execute(args);
    }

    @Test
    void Successful_execution() {

        String[] args = new String[] {};

        when(this.startUpPhaseMock.execute(args)).thenReturn(ThreePhaseCommandLineExecutor.DEFAULT_SUCCESS_STATUS_CODE);
        int resultCode = 42;
        when(this.mainPhaseMock.execute(args)).thenReturn(resultCode);
        when(this.shutDownPhaseMock.execute(args)).thenReturn(ThreePhaseCommandLineExecutor.DEFAULT_SUCCESS_STATUS_CODE);

        int statusCode = this.executor.execute(args);
        assertThat(statusCode).isEqualTo(ThreePhaseCommandLineExecutor.DEFAULT_SUCCESS_STATUS_CODE);

        InOrder callOrder = inOrder(this.mainPhaseMock, this.shutDownPhaseMock);
        callOrder.verify(this.mainPhaseMock).execute(args);
        callOrder.verify(this.shutDownPhaseMock).execute(args);
    }
    
    @Test
    void Shutdown_is_executed_when_exception_is_thrown_by_startup() {

        String[] args = new String[] {};
        
        RuntimeException exception = new RuntimeException("TEST");
        when(this.startUpPhaseMock.execute(args)).thenThrow(exception);

        RuntimeException theException = assertThrows(RuntimeException.class, () -> this.executor.execute(args));
        assertSame(exception, theException);
        
        InOrder callOrder = inOrder(this.startUpPhaseMock, this.shutDownPhaseMock);
        callOrder.verify(this.startUpPhaseMock).execute(args);
        callOrder.verify(this.shutDownPhaseMock).execute(args);
    }
    
    @Test
    void Shutdown_is_executed_when_exception_is_thrown_by_main() {

        String[] args = new String[] {};

        when(this.startUpPhaseMock.execute(args)).thenReturn(ThreePhaseCommandLineExecutor.DEFAULT_SUCCESS_STATUS_CODE);
        RuntimeException exception = new RuntimeException("TEST");
        when(this.mainPhaseMock.execute(args)).thenThrow(exception);
        

        RuntimeException theException = assertThrows(RuntimeException.class, () -> this.executor.execute(args));
        assertSame(exception, theException);

        InOrder callOrder = inOrder(this.startUpPhaseMock, this.mainPhaseMock, this.shutDownPhaseMock);
        callOrder.verify(this.startUpPhaseMock).execute(args);
        callOrder.verify(this.mainPhaseMock).execute(args);
        callOrder.verify(this.shutDownPhaseMock).execute(args);
        
    }
}