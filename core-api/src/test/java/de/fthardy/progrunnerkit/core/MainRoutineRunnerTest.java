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
package de.fthardy.progrunnerkit.core;

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
class MainRoutineRunnerTest {
    
    @Mock
    private MainRoutine mainRoutineMock;

    @Mock
    private MainRoutineRunner.ExceptionHandler exceptionHandlerMock;

    private MainRoutineRunner runner;

    private final ProgramStatusCodeProvider statusCodeProvider = new DefaultProgramStatusCodeProvider();

    private final List<String> args = Collections.emptyList();

    @BeforeEach
    void setUp() {
        runner = new MainRoutineRunner(mainRoutineMock, exceptionHandlerMock, statusCodeProvider);
    }

    @AfterEach
    void checkMocks() {
        verifyNoMoreInteractions(mainRoutineMock, exceptionHandlerMock);
    }

    @Test
    void Null_for_MainRoutine_is_not_allowed() {
        assertThrows(NullPointerException.class, () ->
                new MainRoutineRunner(null, null, null));
    }

    @Test
    void Null_for_ExceptionHandler_is_not_allowed() {
        assertThrows(NullPointerException.class, () ->
                new MainRoutineRunner(mock(MainRoutine.class), null, null));
    }

    @Test
    void Null_for_StatusCodeProvider_is_not_allowed() {
        assertThrows(NullPointerException.class, () ->
                new MainRoutineRunner(mock(MainRoutine.class), mock(MainRoutineRunner.ExceptionHandler.class),
                        null));
    }

    @Test
    void Valid_exit_code() {

        when(mainRoutineMock.execute(args)).thenReturn(statusCodeProvider.success());

        int statusCode = runner.execute(args);
        assertThat(statusCode).isEqualTo(statusCodeProvider.success());

        verify(mainRoutineMock).execute(args);
    }

    @Test
    void Collision_with_internal_code() {

        when(mainRoutineMock.execute(args)).thenReturn(statusCodeProvider.startUpFailure());

        int statusCode = runner.execute(args);
        assertThat(statusCode).isEqualTo(statusCodeProvider.invalidMainExitCode());

        verify(mainRoutineMock).execute(args);
    }

    @Test
    void Exception_is_thrown_Handler_returns_success_which_is_not_allowed() {

        RuntimeException exception = new RuntimeException("TEST!");
        when(mainRoutineMock.execute(args)).thenThrow(exception);

        when(exceptionHandlerMock.handleExceptionFromMainRoutine(exception)).thenReturn(statusCodeProvider.success());

        int statusCode = runner.execute(args);
        assertThat(statusCode).isEqualTo(statusCodeProvider.invalidMainFailureCode());

        verify(mainRoutineMock).execute(args);
        verify(exceptionHandlerMock).handleExceptionFromMainRoutine(exception);
    }

    @Test
    void Exception_is_thrown_Handler_returns_valid_failure_code() {

        RuntimeException exception = new RuntimeException("TEST!");
        when(mainRoutineMock.execute(args)).thenThrow(exception);

        int resultCode = 42;
        when(exceptionHandlerMock.handleExceptionFromMainRoutine(exception)).thenReturn(resultCode);

        int statusCode = runner.execute(args);
        assertThat(statusCode).isEqualTo(resultCode);

        verify(mainRoutineMock).execute(args);
        verify(exceptionHandlerMock).handleExceptionFromMainRoutine(exception);
    }

    @Test
    void Exception_is_thrown_Collision_with_internal_code() {

        RuntimeException exception = new RuntimeException("TEST!");
        when(mainRoutineMock.execute(args)).thenThrow(exception);

        when(exceptionHandlerMock.handleExceptionFromMainRoutine(exception)).thenReturn(statusCodeProvider.invalidStartUpExitCode());

        int statusCode = runner.execute(args);
        assertThat(statusCode).isEqualTo(statusCodeProvider.invalidMainFailureCode());

        verify(mainRoutineMock).execute(args);
        verify(exceptionHandlerMock).handleExceptionFromMainRoutine(exception);
    }

    @Test
    void isValidMainExitCode_All_valid_internal_codes() {
        assertTrue(runner.isValidMainExitCode(statusCodeProvider.success()));
        assertTrue(runner.isValidMainExitCode(statusCodeProvider.mainFailure()));
    }

    @Test
    void isValidMainFailureCode_All_valid_interal_codes() {
        assertTrue(runner.isValidMainFailureCode(statusCodeProvider.mainFailure()));
    }
}