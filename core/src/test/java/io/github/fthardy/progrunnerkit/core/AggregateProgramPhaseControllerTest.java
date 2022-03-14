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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AggregateProgramPhaseControllerTest {
    
    @Test
    public void No_controllers_in_given_collection() {
        
        ProgramPhaseController defaultBehaviourMock = mock(ProgramPhaseController.class);
        
        AggregateProgramPhaseController controller = new AggregateProgramPhaseController(Collections.emptyList(), defaultBehaviourMock);
        
        RuntimeException exception = new RuntimeException("TEST");
        String taskID = "task";
        controller.onPhaseBegin();
        controller.onPhaseEnd();
        controller.beforeTaskExecution(taskID);
        controller.afterTaskExecution(taskID);
        assertFalse(controller.onExceptionFromTask(taskID, exception));
        controller.noTaskAvailable();

        verify(defaultBehaviourMock).onPhaseBegin();
        verify(defaultBehaviourMock).onPhaseEnd();
        verify(defaultBehaviourMock).beforeTaskExecution(taskID);
        verify(defaultBehaviourMock).afterTaskExecution(taskID);
        verify(defaultBehaviourMock).onExceptionFromTask(taskID, exception);
        verify(defaultBehaviourMock).noTaskAvailable();
        
        verifyNoMoreInteractions(defaultBehaviourMock);
    }
    
    @Test
    public void Every_Method_is_delegated_to_every_controller_in_the_given_collection() {
        
        StartPhaseController controllerA = mock(StartPhaseController.class);
        StartPhaseController controllerB = mock(StartPhaseController.class);
        
        AggregateProgramPhaseController controller = new AggregateProgramPhaseController(Arrays.asList(controllerA, controllerB));
        
        RuntimeException exception = new RuntimeException("TEST");
        String taskID = "task";
        controller.onPhaseBegin();
        controller.onPhaseEnd();
        controller.beforeTaskExecution(taskID);
        controller.afterTaskExecution(taskID);
        assertFalse(controller.onExceptionFromTask(taskID, exception));
        controller.noTaskAvailable();

        InOrder inOrder = inOrder(controllerA, controllerB);
        
        inOrder.verify(controllerA).onPhaseBegin();
        inOrder.verify(controllerB).onPhaseBegin();
        inOrder.verify(controllerA).onPhaseEnd();
        inOrder.verify(controllerB).onPhaseEnd();
        inOrder.verify(controllerA).beforeTaskExecution(taskID);        
        inOrder.verify(controllerB).beforeTaskExecution(taskID);        
        inOrder.verify(controllerA).afterTaskExecution(taskID);        
        inOrder.verify(controllerB).afterTaskExecution(taskID);
        inOrder.verify(controllerA).onExceptionFromTask(taskID, exception);
        inOrder.verify(controllerB).onExceptionFromTask(taskID, exception);
        inOrder.verify(controllerA).noTaskAvailable();
        inOrder.verify(controllerB).noTaskAvailable();
        
        verifyNoMoreInteractions(controllerA, controllerB);
    }
    
    @Test
    public void Controller_ends_phase() {

        StartPhaseController controllerA = mock(StartPhaseController.class);
        StartPhaseController controllerB = mock(StartPhaseController.class);
        StartPhaseController controllerC = mock(StartPhaseController.class);

        AggregateProgramPhaseController controller = new AggregateProgramPhaseController(Arrays.asList(controllerA, controllerB, controllerC));

        RuntimeException exception = new RuntimeException("TEST");
        String taskID = "task";
        
        when(controllerB.onExceptionFromTask(taskID, exception)).thenReturn(true);

        assertTrue(controller.onExceptionFromTask(taskID, exception));

        InOrder inOrder = inOrder(controllerA, controllerB, controllerC);

        inOrder.verify(controllerA).onExceptionFromTask(taskID, exception);
        inOrder.verify(controllerB).onExceptionFromTask(taskID, exception);
        inOrder.verify(controllerC, never()).onExceptionFromTask(taskID, exception);

        verifyNoMoreInteractions(controllerA, controllerB);
    }
}