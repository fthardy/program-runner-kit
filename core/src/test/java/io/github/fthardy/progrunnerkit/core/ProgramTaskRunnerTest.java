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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgramTaskRunnerTest {
    
    @Mock
    private ProgramPhaseController controllerMock;
    
    @AfterEach
    public void checkMocks() {
        verifyNoMoreInteractions(this.controllerMock);
    }

    @Test
    public void No_tasks_available() {
        
        ProgramTaskRunner runner = new ProgramTaskRunner(Collections.emptyList(), this.controllerMock);
        
        assertFalse(runner.runProgramTasks(Collections.emptyList()));
    }
    
    @Test
    public void Tasks_executed_in_given_order() {
        String task1ID = "task1";
        String task2ID = "task2";
        
        StartPhaseTask task1Mock = mock(StartPhaseTask.class, task1ID);
        StartPhaseTask task2Mock = mock(StartPhaseTask.class, task2ID);
        
        when(task1Mock.getIdentifier()).thenReturn(task1ID);
        when(task2Mock.getIdentifier()).thenReturn(task2ID);

        ProgramTaskRunner runner = new ProgramTaskRunner(Arrays.asList(task1Mock, task2Mock), this.controllerMock);

        List<String> arguments = Collections.emptyList();
        assertTrue(runner.runProgramTasks(arguments));

        InOrder inOrder = inOrder(task1Mock, task2Mock, this.controllerMock);
        
        inOrder.verify(task1Mock).getIdentifier();
        inOrder.verify(this.controllerMock).beforeTaskExecution(task1ID);
        inOrder.verify(task1Mock).run(arguments);
        inOrder.verify(this.controllerMock).afterTaskExecution(task1ID);
        
        inOrder.verify(task2Mock).getIdentifier();
        inOrder.verify(this.controllerMock).beforeTaskExecution(task2ID);
        inOrder.verify(task2Mock).run(arguments);
        inOrder.verify(this.controllerMock).afterTaskExecution(task2ID);
    }
    
    @Test
    public void Controller_continues_phase_on_exception() {
        List<String> arguments = Collections.emptyList();
        
        String task1ID = "task1";
        String task2ID = "task2";

        StartPhaseTask task1Mock = mock(StartPhaseTask.class, task1ID);
        StartPhaseTask task2Mock = mock(StartPhaseTask.class, task2ID);

        when(task1Mock.getIdentifier()).thenReturn(task1ID);
        when(task2Mock.getIdentifier()).thenReturn(task2ID);

        RuntimeException exception = new RuntimeException("TEST");
        doThrow(exception).when(task1Mock).run(arguments);
        
        when(this.controllerMock.onExceptionFromTask(task1ID, exception)).thenReturn(false);

        ProgramTaskRunner runner = new ProgramTaskRunner(Arrays.asList(task1Mock, task2Mock), this.controllerMock);

        assertTrue(runner.runProgramTasks(arguments));

        InOrder inOrder = inOrder(task1Mock, task2Mock, this.controllerMock);

        inOrder.verify(task1Mock).getIdentifier();
        inOrder.verify(this.controllerMock).beforeTaskExecution(task1ID);
        inOrder.verify(task1Mock).run(arguments);
        inOrder.verify(this.controllerMock).onExceptionFromTask(task1ID, exception);
        inOrder.verify(this.controllerMock).afterTaskExecution(task1ID);

        inOrder.verify(task2Mock).getIdentifier();
        inOrder.verify(this.controllerMock).beforeTaskExecution(task2ID);
        inOrder.verify(task2Mock).run(arguments);
        inOrder.verify(this.controllerMock).afterTaskExecution(task2ID);
    }
    
    @Test
    public void Controller_ends_phase_on_exception() {
        List<String> arguments = Collections.emptyList();

        String task1ID = "task1";
        String task2ID = "task2";

        StartPhaseTask task1Mock = mock(StartPhaseTask.class, task1ID);
        StartPhaseTask task2Mock = mock(StartPhaseTask.class, task2ID);

        when(task1Mock.getIdentifier()).thenReturn(task1ID);

        RuntimeException exception = new RuntimeException("TEST");
        doThrow(exception).when(task1Mock).run(arguments);

        when(this.controllerMock.onExceptionFromTask(task1ID, exception)).thenReturn(true);

        ProgramTaskRunner runner = new ProgramTaskRunner(Arrays.asList(task1Mock, task2Mock), this.controllerMock);

        assertTrue(runner.runProgramTasks(arguments));

        InOrder inOrder = inOrder(task1Mock, task2Mock, this.controllerMock);

        inOrder.verify(task1Mock).getIdentifier();
        inOrder.verify(this.controllerMock).beforeTaskExecution(task1ID);
        inOrder.verify(task1Mock).run(arguments);
        inOrder.verify(this.controllerMock).onExceptionFromTask(task1ID, exception);
        inOrder.verify(this.controllerMock).afterTaskExecution(task1ID);
        
        verifyNoInteractions(task2Mock);
    }
}