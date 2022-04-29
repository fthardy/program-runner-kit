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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MainTest {
    
    private final static Main.ProgramPhaseControllerFactory programPhaseControllerFactoryMock = mock(Main.ProgramPhaseControllerFactory.class);
    private final static Main.ProgramTaskRunnerFactory programTaskRunnerFactoryMock = mock(Main.ProgramTaskRunnerFactory.class);
    
    @Mock
    private ProgramPhaseController startPhaseControllerMock;
    
    @Mock
    private ProgramPhaseController endPhaseControllerMock;
    
    @Mock
    private ProgramTaskRunner startPhaseProgramTaskRunnerMock;
    
    @Mock
    private ProgramTaskRunner endPhaseProgramTaskRunnerMock;
    
    @BeforeAll
    static void substituteFactoriesWithMocks() {
        Main._programPhaseControllerFactory = programPhaseControllerFactoryMock;
        Main._progamTaskRunnerFactory = programTaskRunnerFactoryMock;
    }
    
    @BeforeEach
    void initMocks() {
        reset(programPhaseControllerFactoryMock, programTaskRunnerFactoryMock);
        
        when(programPhaseControllerFactoryMock.createInstance(
                eq(StartPhaseController.class), any(StartPhaseController.class))).thenReturn(this.startPhaseControllerMock);
        when(programPhaseControllerFactoryMock.createInstance(
                eq(EndPhaseController.class), any(EndPhaseController.class))).thenReturn(this.endPhaseControllerMock);
        when(programTaskRunnerFactoryMock.createInstance(StartPhaseTask.class, this.startPhaseControllerMock)).thenReturn(this.startPhaseProgramTaskRunnerMock);
        when(programTaskRunnerFactoryMock.createInstance(EndPhaseTask.class, this.endPhaseControllerMock)).thenReturn(this.endPhaseProgramTaskRunnerMock);
    }
    
    @AfterEach
    void checkMocks() {
        verifyNoMoreInteractions(programPhaseControllerFactoryMock, programTaskRunnerFactoryMock);
        verifyNoMoreInteractions(
                this.startPhaseControllerMock,
                this.endPhaseControllerMock,
                this.startPhaseProgramTaskRunnerMock,
                this.endPhaseProgramTaskRunnerMock);
    }

    @Test
    public void No_start_phase_tasks_available() {
        
        when(this.startPhaseProgramTaskRunnerMock.runProgramTasks(anyList())).thenReturn(false);
        
        Main.main(new String[] {});
        
        verify(programPhaseControllerFactoryMock).createInstance(eq(StartPhaseController.class), any(StartPhaseController.class));
        verify(this.startPhaseControllerMock).onPhaseBegin();
        verify(programTaskRunnerFactoryMock).createInstance(StartPhaseTask.class, this.startPhaseControllerMock);
        verify(this.startPhaseProgramTaskRunnerMock).runProgramTasks(anyList());
        verify(this.startPhaseControllerMock).noTaskAvailable();
        verify(this.startPhaseControllerMock).onPhaseEnd();
        verify(this.endPhaseProgramTaskRunnerMock, never()).runProgramTasks(anyList());
    }

    @Test
    public void No_end_phase_tasks_available() {

        when(this.startPhaseProgramTaskRunnerMock.runProgramTasks(anyList())).thenReturn(true);
        when(this.endPhaseProgramTaskRunnerMock.runProgramTasks(anyList())).thenReturn(false);

        Main.main(new String[] {});

        verify(programPhaseControllerFactoryMock).createInstance(eq(StartPhaseController.class), any(StartPhaseController.class));
        verify(this.startPhaseControllerMock).onPhaseBegin();
        verify(programTaskRunnerFactoryMock).createInstance(StartPhaseTask.class, this.startPhaseControllerMock);
        verify(this.startPhaseProgramTaskRunnerMock).runProgramTasks(anyList());
        verify(this.startPhaseControllerMock).onPhaseEnd();
        
        verify(programPhaseControllerFactoryMock).createInstance(eq(EndPhaseController.class), any(EndPhaseController.class));
        verify(this.endPhaseControllerMock).onPhaseBegin();
        verify(programTaskRunnerFactoryMock).createInstance(EndPhaseTask.class, this.endPhaseControllerMock);
        verify(this.endPhaseProgramTaskRunnerMock).runProgramTasks(anyList());
        verify(this.endPhaseControllerMock).noTaskAvailable();
        verify(this.endPhaseControllerMock).onPhaseEnd();
    }
    
    @Test
    public void Start_phase_interrupted_by_exception() {

        RuntimeException exception = new RuntimeException("TEST");
        when(this.startPhaseProgramTaskRunnerMock.runProgramTasks(anyList())).thenThrow(exception);
        when(this.endPhaseProgramTaskRunnerMock.runProgramTasks(anyList())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> Main.main(new String[] {}));

        verify(programPhaseControllerFactoryMock).createInstance(eq(StartPhaseController.class), any(StartPhaseController.class));
        verify(this.startPhaseControllerMock).onPhaseBegin();
        verify(programTaskRunnerFactoryMock).createInstance(StartPhaseTask.class, this.startPhaseControllerMock);
        verify(this.startPhaseProgramTaskRunnerMock).runProgramTasks(anyList());
        verify(this.startPhaseControllerMock).onPhaseEnd();

        verify(programPhaseControllerFactoryMock).createInstance(eq(EndPhaseController.class), any(EndPhaseController.class));
        verify(this.endPhaseControllerMock).onPhaseBegin();
        verify(programTaskRunnerFactoryMock).createInstance(EndPhaseTask.class, this.endPhaseControllerMock);
        verify(this.endPhaseProgramTaskRunnerMock).runProgramTasks(anyList());
        verify(this.endPhaseControllerMock).onPhaseEnd();
    }
}