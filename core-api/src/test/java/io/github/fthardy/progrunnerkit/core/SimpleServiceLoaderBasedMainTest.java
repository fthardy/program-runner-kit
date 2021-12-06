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

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleServiceLoaderBasedMainTest {
    
    @Mock
    private Iterable<MainRoutine> mainRoutinesIterableMock;
    
    @Mock
    private Iterator<MainRoutine> mainRoutineIteratorMock;
    
    @Mock
    private MainRoutine mainRoutineMock;
    
    @Mock
    private List<String> inputArgumentsMock;
    
    @BeforeEach
    void setUp() {
        when(this.mainRoutinesIterableMock.iterator()).thenReturn(this.mainRoutineIteratorMock);
    }
    
    @AfterEach
    void checkMocks() {
        verify(this.mainRoutinesIterableMock).iterator();
        
        verifyNoMoreInteractions(this.mainRoutinesIterableMock, this.mainRoutineIteratorMock, this.mainRoutineMock,
                this.inputArgumentsMock);
    }
    
    @Test
    void No_main_routine_implementation_is_found() {
        
        when(this.mainRoutineIteratorMock.hasNext()).thenReturn(false);
        
        assertThrows(IllegalStateException.class,
                () -> SimpleServiceLoaderBasedMain.loadAndStartMainRoutine(this.inputArgumentsMock, this.mainRoutinesIterableMock));
        
        verify(this.mainRoutineIteratorMock).hasNext();
    }
    
    @Test
    void More_than_one_main_routine_implementation_is_found() {
        when(this.mainRoutineIteratorMock.hasNext()).thenReturn(true, true);
        when(this.mainRoutineIteratorMock.next()).thenReturn(this.mainRoutineMock);

        assertThrows(IllegalStateException.class,
                () -> SimpleServiceLoaderBasedMain.loadAndStartMainRoutine(this.inputArgumentsMock, this.mainRoutinesIterableMock));
        
        verify(this.mainRoutineIteratorMock, times(2)).hasNext();
    }
    
    @Test
    void Exactly_one_main_routine_implementation_is_found_and_executed() {
        int expectedExitCode = 42;
        when(this.mainRoutineIteratorMock.hasNext()).thenReturn(true, false);
        when(this.mainRoutineIteratorMock.next()).thenReturn(this.mainRoutineMock);
        when (this.mainRoutineMock.execute(this.inputArgumentsMock)).thenReturn(expectedExitCode);

        int returnedExitCode = SimpleServiceLoaderBasedMain.loadAndStartMainRoutine(this.inputArgumentsMock,
                this.mainRoutinesIterableMock);
        assertEquals(expectedExitCode, returnedExitCode);

        verify(this.mainRoutineIteratorMock, times(2)).hasNext();
        verify(this.mainRoutineMock).execute(this.inputArgumentsMock);
    }
}