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

import io.github.fthardy.progrunnerkit.core.BasicProgramExecutionContext;
import io.github.fthardy.progrunnerkit.core.ProgramExecutionContext;
import io.github.fthardy.progrunnerkit.core.ProgramPartEntryPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class DefaultProgramRootEntryPointTest {
    
    @Mock
    private ProgramPartEntryPoint startUpPhaseMock;
    @Mock
    private ProgramPartEntryPoint mainPhaseMock;
    @Mock
    private ProgramPartEntryPoint shutDownPhaseMock;
    
    private DefaultProgramRootEntryPoint entryPoint;
    
    @BeforeEach
    void setUp() {
        this.entryPoint = new DefaultProgramRootEntryPoint(this.startUpPhaseMock, this.mainPhaseMock, this.shutDownPhaseMock);
    }

    @AfterEach
    void checkMocks() {
        verifyNoMoreInteractions(this.startUpPhaseMock, this.mainPhaseMock, this.shutDownPhaseMock);
    }

    @Test
    void Input_arguments_list_is_mandatory() {
        assertThrows(NullPointerException.class, () -> new DefaultProgramRootEntryPoint(
                null, null, null));
    }
    
    @Test
    void Main_routine_is_mandatory() {
        assertThrows(NullPointerException.class, () -> new DefaultProgramRootEntryPoint(
                null, null, null));
    }
    
    @Test
    void runProgram_no_startup_no_shutdown() {

        ProgramExecutionContext context = new BasicProgramExecutionContext(Collections.emptyList(), null);
        
        int resultCode = 42;
        when(this.mainPhaseMock.execute(context)).thenReturn(resultCode);

        int statusCode = new DefaultProgramRootEntryPoint(null, this.mainPhaseMock, null).execute(context);
        assertThat(statusCode).isEqualTo(resultCode);

        verify(this.mainPhaseMock).execute(context);
    }

    @Test
    void runProgram_successful_startup_no_shutdown() {

        ProgramExecutionContext context = new BasicProgramExecutionContext(Collections.emptyList(), null);

        int resultCode = 42;
        when(this.startUpPhaseMock.execute(context)).thenReturn(0);
        when(this.mainPhaseMock.execute(context)).thenReturn(resultCode);

        int statusCode = new DefaultProgramRootEntryPoint(this.startUpPhaseMock, this.mainPhaseMock, null).execute(context);
        assertThat(statusCode).isEqualTo(resultCode);

        InOrder callOrder = inOrder(this.startUpPhaseMock, this.mainPhaseMock);
        callOrder.verify(this.startUpPhaseMock).execute(context);
        callOrder.verify(this.mainPhaseMock).execute(context);
    }

    @Test
    void runProgram_failed__at_startup() {

        ProgramExecutionContext context = new BasicProgramExecutionContext(Collections.emptyList(), null);

        int startupResultCode = 42;
        int shutdownResultCode = 99;
        when(this.startUpPhaseMock.execute(context)).thenReturn(startupResultCode);
        when(this.shutDownPhaseMock.execute(any(ProgramExecutionContext.class))).thenReturn(shutdownResultCode);

        int statusCode = this.entryPoint.execute(context);
        assertThat(statusCode).isEqualTo(shutdownResultCode);

        InOrder callOrder = inOrder(this.startUpPhaseMock, this.mainPhaseMock, this.shutDownPhaseMock);
        callOrder.verify(this.startUpPhaseMock).execute(context);
        callOrder.verify(this.mainPhaseMock, never()).execute(context);
        ArgumentCaptor<ProgramExecutionContext> contextCaptor = ArgumentCaptor.forClass(ProgramExecutionContext.class);
        callOrder.verify(this.shutDownPhaseMock).execute(contextCaptor.capture());
        Optional<Integer> previousCode = contextCaptor.getValue().getLastStatusCode();
        assertThat(previousCode).isPresent();
        assertThat(previousCode.get()).isEqualTo(startupResultCode);
    }

    @Test
    void runProgram_successful() {

        ProgramExecutionContext context = new BasicProgramExecutionContext(Collections.emptyList(), null);

        when(this.startUpPhaseMock.execute(context)).thenReturn(DefaultProgramRootEntryPoint.STATUS_CODE_SUCCESS);
        int resultCode = 42;
        when(this.mainPhaseMock.execute(context)).thenReturn(resultCode);
        when(this.shutDownPhaseMock.execute(any(ProgramExecutionContext.class))).thenReturn(DefaultProgramRootEntryPoint.STATUS_CODE_SUCCESS);

        int statusCode = this.entryPoint.execute(context);
        assertThat(statusCode).isEqualTo(DefaultProgramRootEntryPoint.STATUS_CODE_SUCCESS);

        InOrder callOrder = inOrder(this.mainPhaseMock, this.shutDownPhaseMock);
        callOrder.verify(this.mainPhaseMock).execute(context);
        ArgumentCaptor<ProgramExecutionContext> contextCaptor = ArgumentCaptor.forClass(ProgramExecutionContext.class);
        callOrder.verify(this.shutDownPhaseMock).execute(contextCaptor.capture());
        Optional<Integer> previousCode = contextCaptor.getValue().getLastStatusCode();
        assertThat(previousCode).isPresent();
        assertThat(previousCode.get()).isEqualTo(resultCode);
    }
}