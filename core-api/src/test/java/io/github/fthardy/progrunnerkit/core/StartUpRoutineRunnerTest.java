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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StartUpRoutineRunnerTest {
    
    @Mock
    private StartUpRoutine startUpRoutineMock;
    
    @Mock
    private ExceptionInterceptingStartupRoutine.ExceptionHandler exceptionHandlerMock;
    
    private ExceptionInterceptingStartupRoutine runner;

    private final ProgramStatusCodeProvider statusCodeProvider = new DefaultProgramStatusCodeProvider();

    private final List<String> args = Collections.emptyList();
    
    @BeforeEach
    void setUp() {
        runner = new ExceptionInterceptingStartupRoutine(startUpRoutineMock, exceptionHandlerMock, statusCodeProvider);
    }
    
    @AfterEach
    void checkMocks() {
        verifyNoMoreInteractions(startUpRoutineMock, exceptionHandlerMock);
    }
    
    @Test
    void Null_for_StartUpRoutine_is_not_allowed() {
        assertThrows(NullPointerException.class, () -> 
                new ExceptionInterceptingStartupRoutine(null, null, null));
    }
    
    @Test
    void Null_for_ExceptionHandler_is_not_allowed() {
        assertThrows(NullPointerException.class, () ->
                new ExceptionInterceptingStartupRoutine(mock(StartUpRoutine.class), null, null));
    }
    
    @Test
    void Null_for_StatusCodeProvider_is_not_allowed() {
        assertThrows(NullPointerException.class, () ->
                new ExceptionInterceptingStartupRoutine(mock(StartUpRoutine.class), mock(ExceptionInterceptingStartupRoutine.ExceptionHandler.class),
                        null));
    }

    @Test
    void Valid_exit_code() {

        when(startUpRoutineMock.execute(args)).thenReturn(statusCodeProvider.success());

        int statusCode = runner.execute(args);
        assertThat(statusCode).isEqualTo(statusCodeProvider.success());

        verify(startUpRoutineMock).execute(args);
    }

    @Test
    void Collision_with_internal_code() {

        when(startUpRoutineMock.execute(args)).thenReturn(statusCodeProvider.mainFailure());

        int statusCode = runner.execute(args);
        assertThat(statusCode).isEqualTo(statusCodeProvider.invalidStartUpExitCode());

        verify(startUpRoutineMock).execute(args);
    }

    @Test
    void Exception_is_thrown_Handler_returns_success() {

        RuntimeException exception = new RuntimeException("TEST!");
        when(startUpRoutineMock.execute(args)).thenThrow(exception);

        when(exceptionHandlerMock.handleExceptionFromStartUpRoutine(exception)).thenReturn(statusCodeProvider.success());

        int statusCode = runner.execute(args);
        assertThat(statusCode).isEqualTo(statusCodeProvider.success());

        verify(startUpRoutineMock).execute(args);
        verify(exceptionHandlerMock).handleExceptionFromStartUpRoutine(exception);
    }

    @Test
    void runStartUpRoutine_Exception_is_thrown_Handler_returns_valid_failure_code() {

        RuntimeException exception = new RuntimeException("TEST!");
        when(startUpRoutineMock.execute(args)).thenThrow(exception);

        int resultCode = 42;
        when(exceptionHandlerMock.handleExceptionFromStartUpRoutine(exception)).thenReturn(resultCode);

        int statusCode = runner.execute(args);
        assertThat(statusCode).isEqualTo(resultCode);

        verify(startUpRoutineMock).execute(args);
        verify(exceptionHandlerMock).handleExceptionFromStartUpRoutine(exception);
    }

    @Test
    void runStartUpRoutine_Exception_is_thrown_Handler_Collision_with_internal_code() {

        RuntimeException exception = new RuntimeException("TEST!");
        when(startUpRoutineMock.execute(args)).thenThrow(exception);

        when(exceptionHandlerMock.handleExceptionFromStartUpRoutine(exception)).thenReturn(statusCodeProvider.mainFailure());

        int statusCode = runner.execute(args);
        assertThat(statusCode).isEqualTo(statusCodeProvider.invalidStartUpFailureCode());

        verify(startUpRoutineMock).execute(args);
        verify(exceptionHandlerMock).handleExceptionFromStartUpRoutine(exception);
    }

    @Test
    void isValidStartUpExitCode_All_valid_internal_codes() {
        assertTrue(runner.isValidStartUpExitCode(statusCodeProvider.success()));
        assertTrue(runner.isValidStartUpExitCode(statusCodeProvider.startUpFailure()));
    }

    @Test
    void isValidStartUpFailureCode_All_valid_internal_codes() {
        assertTrue(runner.isValidStartUpFailureCode(statusCodeProvider.startUpFailure()));
    }
}