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

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ProgramRunnerTest {
    
    @Mock
    private StartUpRoutine startUpRoutineMock;
    @Mock
    private MainRoutine mainRoutineMock;
    @Mock
    private ShutDownRoutine shutDownRoutineMock;

    private final List<String> args = Collections.emptyList();
    
    private final ProgramStatusCodeProvider statusCodeProvider = new DefaultProgramStatusCodeProvider();

    private ProgramRunner runner;
    
    @BeforeEach
    void setUp() {
        this.runner = new ProgramRunner(
                args, startUpRoutineMock, mainRoutineMock, shutDownRoutineMock, statusCodeProvider);
    }

    @AfterEach
    void checkMocks() {
        verifyNoMoreInteractions(startUpRoutineMock, mainRoutineMock, shutDownRoutineMock);
    }

    @Test
    void Input_arguments_list_is_mandatory() {
        assertThrows(NullPointerException.class, () -> new ProgramRunner(
                null, null, null, null, null));
    }
    
    @Test
    void Main_routine_is_mandatory() {
        assertThrows(IllegalStateException.class, () -> new ProgramRunner(
                args, null, null, null,null));
    }
    
    @Test
    void Status_code_provider_is_mandatory() {
        assertThrows(NullPointerException.class, () -> new ProgramRunner(
                args, null, mainRoutineMock, null, null));
    }
    
    @Test
    void runProgram_no_startup_no_shutdown() {
        
        int resultCode = 42;
        when(mainRoutineMock.execute(args)).thenReturn(resultCode);

        int statusCode = new ProgramRunner(
                args, null, mainRoutineMock, null, statusCodeProvider).runProgram();
        assertThat(statusCode).isEqualTo(resultCode);

        verify(mainRoutineMock).execute(args);
    }

    @Test
    void runProgram_successful_startup_no_shutdown() {
        
        int resultCode = 42;
        when(startUpRoutineMock.execute(args)).thenReturn(statusCodeProvider.success());
        when(mainRoutineMock.execute(args)).thenReturn(resultCode);

        int statusCode =
                new ProgramRunner(args, startUpRoutineMock, mainRoutineMock, null, statusCodeProvider).runProgram();
        assertThat(statusCode).isEqualTo(resultCode);

        InOrder callOrder = inOrder(startUpRoutineMock, mainRoutineMock);
        callOrder.verify(startUpRoutineMock).execute(args);
        callOrder.verify(mainRoutineMock).execute(args);
    }

    @Test
    void runProgram_failed_startup() {

        int resultCode = 42;
        when(startUpRoutineMock.execute(args)).thenReturn(resultCode);

        int statusCode = runner.runProgram();
        assertThat(statusCode).isEqualTo(resultCode);

        InOrder callOrder = inOrder(startUpRoutineMock, mainRoutineMock);
        callOrder.verify(startUpRoutineMock).execute(args);
        callOrder.verify(mainRoutineMock, never()).execute(args);
    }

    @Test
    void runProgram_successful_shutdown() {

        when(startUpRoutineMock.execute(args)).thenReturn(statusCodeProvider.success());
        int resultCode = 42;
        when(mainRoutineMock.execute(args)).thenReturn(resultCode);
        when(shutDownRoutineMock.execute(args, resultCode)).thenReturn(statusCodeProvider.success());

        int statusCode = runner.runProgram();
        assertThat(statusCode).isEqualTo(statusCodeProvider.success());

        InOrder callOrder = inOrder(mainRoutineMock, shutDownRoutineMock);
        callOrder.verify(mainRoutineMock).execute(args);
        callOrder.verify(shutDownRoutineMock).execute(args, resultCode);
    }
}