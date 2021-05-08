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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShutDownRoutineTest {

    @Mock
    private ShutDownRoutine shutDownRoutineMock;

    @Mock
    private ShutDownRoutineRunner.ExceptionHandler exceptionHandlerMock;

    private ShutDownRoutineRunner runner;

    private final ProgramStatusCodeProvider statusCodeProvider = new DefaultProgramStatusCodeProvider();

    private final List<String> args = Collections.emptyList();

    @BeforeEach
    void setUp() {
        runner = new ShutDownRoutineRunner(shutDownRoutineMock, exceptionHandlerMock, statusCodeProvider);
    }

    @AfterEach
    void checkMocks() {
        verifyNoMoreInteractions(shutDownRoutineMock, exceptionHandlerMock);
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
    void Success() {

        int exitCodeFromMain = 42;
        when(shutDownRoutineMock.execute(args, exitCodeFromMain)).thenReturn(statusCodeProvider.success());

        int statusCode = runner.execute(args, exitCodeFromMain);
        assertThat(statusCode).isEqualTo(exitCodeFromMain);

        verify(shutDownRoutineMock).execute(args, exitCodeFromMain);
    }

    @Test
    void Valid_exit_code() {

        int exitCodeFromMain = 42;
        when(shutDownRoutineMock.execute(args, exitCodeFromMain)).thenReturn(exitCodeFromMain);

        int statusCode = runner.execute(args, exitCodeFromMain);
        assertThat(statusCode).isEqualTo(exitCodeFromMain);

        verify(shutDownRoutineMock).execute(args, exitCodeFromMain);
    }

    @Test
    void Collision_with_internal_code() {

        int exitCodeFromMain = 42;
        when(shutDownRoutineMock.execute(args, exitCodeFromMain)).thenReturn(statusCodeProvider.invalidStartUpExitCode());

        int statusCode = runner.execute(args, exitCodeFromMain);
        assertThat(statusCode).isEqualTo(statusCodeProvider.invalidShutDownExitCode());

        verify(shutDownRoutineMock).execute(args, exitCodeFromMain);
    }

    @Test
    void Exception_is_thrown_Handler_returns_success() {

        int exitCodeFromMain = 42;
        RuntimeException exception = new RuntimeException("TEST!");
        when(shutDownRoutineMock.execute(args, exitCodeFromMain)).thenThrow(exception);

        when(exceptionHandlerMock.handleExceptionFromShutDownRoutine(exception)).thenReturn(statusCodeProvider.success());

        int statusCode = runner.execute(args, exitCodeFromMain);
        assertThat(statusCode).isEqualTo(exitCodeFromMain);

        verify(shutDownRoutineMock).execute(args, exitCodeFromMain);
        verify(exceptionHandlerMock).handleExceptionFromShutDownRoutine(exception);
    }

    @Test
    void Exception_is_thrown_Handler_returns_valid_failure_code() {

        int exitCodeFromMain = 42;
        RuntimeException exception = new RuntimeException("TEST!");
        when(shutDownRoutineMock.execute(args, exitCodeFromMain)).thenThrow(exception);

        when(exceptionHandlerMock.handleExceptionFromShutDownRoutine(exception)).thenReturn(statusCodeProvider.shutDownFailure());

        int statusCode = runner.execute(args, exitCodeFromMain);
        assertThat(statusCode).isEqualTo(statusCodeProvider.shutDownFailure());

        verify(shutDownRoutineMock).execute(args, exitCodeFromMain);
        verify(exceptionHandlerMock).handleExceptionFromShutDownRoutine(exception);
    }

    @Test
    void Exception_is_thrown_Collision_with_internal_code() {

        int exitCodeFromMain = 42;
        RuntimeException exception = new RuntimeException("TEST!");
        when(shutDownRoutineMock.execute(args, exitCodeFromMain)).thenThrow(exception);

        when(exceptionHandlerMock.handleExceptionFromShutDownRoutine(exception)).thenReturn(statusCodeProvider.invalidStartUpExitCode());

        int statusCode = runner.execute(args, exitCodeFromMain);
        assertThat(statusCode).isEqualTo(statusCodeProvider.invalidShutDownFailureCode());

        verify(shutDownRoutineMock).execute(args, exitCodeFromMain);
        verify(exceptionHandlerMock).handleExceptionFromShutDownRoutine(exception);
    }

    @Test
    void isValidShutDownExitCode_All_valid_internal_codes() {
        assertTrue(runner.isValidShutDownExitCode(statusCodeProvider.success()));
        assertTrue(runner.isValidShutDownExitCode(statusCodeProvider.shutDownFailure()));
        assertTrue(runner.isValidShutDownExitCode(statusCodeProvider.mainFailure()));
        assertTrue(runner.isValidShutDownExitCode(statusCodeProvider.invalidMainExitCode()));
        assertTrue(runner.isValidShutDownExitCode(statusCodeProvider.invalidMainFailureCode()));
    }

    @Test
    void isValidShutDownFailureCode_All_valid_interal_codes() {
        assertTrue(runner.isValidShutDownFailureCode(statusCodeProvider.shutDownFailure()));
    }
}