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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExceptionInterceptingCommandLineExecutorTest {
    
    @Mock
    private CommandLineExecutor delegateMock;

    @Mock
    private ExceptionInterceptingCommandLineExecutor.ExceptionHandler exceptionHandlerMock;

    private ExceptionInterceptingCommandLineExecutor proxy;

    @BeforeEach
    void setUp() {
        this.proxy = new ExceptionInterceptingCommandLineExecutor(this.delegateMock, this.exceptionHandlerMock);
    }

    @AfterEach
    void checkMocks() {
        verifyNoMoreInteractions(this.delegateMock, this.exceptionHandlerMock);
    }
    
    @Test
    void Delegate_returns_with_no_exception() {
        String[] args = new String[] {};
        int exitCodeToReturn = 0;
        when(this.delegateMock.execute(args)).thenReturn(exitCodeToReturn);
        assertThat(this.proxy.execute(args)).isEqualTo(exitCodeToReturn);
    }
    
    @Test
    void Delegate_throws_exception() {
        String[] args = new String[] {};
        int exitCodeToReturn = 0;
        RuntimeException exception = new RuntimeException("TEST");
        when(this.delegateMock.execute(args)).thenThrow(exception);
        when(this.exceptionHandlerMock.handleException(exception)).thenReturn(exitCodeToReturn);
        assertThat(this.proxy.execute(args)).isEqualTo(exitCodeToReturn);
    } 
}