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

import java.util.Objects;

/**
 * The implementation of a root entry point for a program which separates the program execution in three phases: startup, main, shutdown.
 * <p>
 * The startup- and shutdown-phases are optional while the main-phase is mandatory. The startup-phase is executed first as long it exists. If it returns
 * {@link #STATUS_CODE_SUCCESS} the main phase is going to be executed. If the main-phase returns {@link #STATUS_CODE_SUCCESS} the shutdown-phase is going to be
 * executed. Then the entry point is finished and returns the status code of the shutdown-phase if it existed or the main-phase otherwise. 
 * </p>
 */
public final class DefaultProgramRootEntryPoint implements ProgramPartEntryPoint {
    
    public static final int STATUS_CODE_SUCCESS = 0; 

    private final ProgramPartEntryPoint startUpPhase;
    private final ProgramPartEntryPoint mainPhase;
    private final ProgramPartEntryPoint shutDownPhase;

    /**
     * Creates a new instance of this program runner.
     *
     * @param startUpPhase the part which implements the startup phase of the program. Can be null if not needed.
     * @param mainPhase the part which implements the main phase of the program. Is not allowed to be null.
     * @param shutDownPhase the part which implements the shutdown phase of the program. Can be null if not needed.
     */
    public DefaultProgramRootEntryPoint(ProgramPartEntryPoint startUpPhase,
                                        ProgramPartEntryPoint mainPhase,
                                        ProgramPartEntryPoint shutDownPhase) {
        this.startUpPhase = startUpPhase;
        this.mainPhase = Objects.requireNonNull(mainPhase, "Undefined implementation for the main phase of the program!");
        this.shutDownPhase = shutDownPhase;
    }
    
    @Override
    public int execute(ProgramExecutionContext context) {
        int statusCode = STATUS_CODE_SUCCESS;
        
        if (this.startUpPhase != null) {
            statusCode = this.startUpPhase.execute(context);
        }

        if (statusCode == STATUS_CODE_SUCCESS) {
            statusCode = this.mainPhase.execute(context);
        }

        if (this.shutDownPhase != null) {
            statusCode = this.shutDownPhase.execute(context.createNewInstanceWithStatusCode(statusCode));
        }

        return statusCode;
    }
}
