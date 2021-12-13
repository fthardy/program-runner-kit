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

import io.github.fthardy.progrunnerkit.core.ProgramExecutionContext;
import io.github.fthardy.progrunnerkit.core.ProgramPartEntryPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExceptionInterceptingProgramPartEntryPointTest {
    
    @Mock
    private ProgramPartEntryPoint delegateMock;

    @Mock
    private ExceptionInterceptingProgramPartEntryPoint.ExceptionHandler exceptionHandlerMock;
    
    @Mock
    private ProgramExecutionContext contextMock;

    private ExceptionInterceptingProgramPartEntryPoint proxy;

    @BeforeEach
    void setUp() {
        this.proxy = new ExceptionInterceptingProgramPartEntryPoint(this.delegateMock, this.exceptionHandlerMock);
    }

    @AfterEach
    void checkMocks() {
        verifyNoMoreInteractions(this.delegateMock, this.exceptionHandlerMock, this.contextMock);
    }
    
    @Test
    void Delegate_returns_with_no_exception() {
        int exitCodeToReturn = 0;
        when(this.delegateMock.execute(this.contextMock)).thenReturn(exitCodeToReturn);
        assertThat(this.proxy.execute(this.contextMock)).isEqualTo(exitCodeToReturn);
    }
    
    @Test
    void Delegate_throws_exception() {
        int exitCodeToReturn = 0;
        RuntimeException exception = new RuntimeException("TEST");
        when(this.delegateMock.execute(this.contextMock)).thenThrow(exception);
        when(this.exceptionHandlerMock.handleExceptionFromMainRoutine(exception)).thenReturn(exitCodeToReturn);
        assertThat(this.proxy.execute(this.contextMock)).isEqualTo(exitCodeToReturn);
    } 
}