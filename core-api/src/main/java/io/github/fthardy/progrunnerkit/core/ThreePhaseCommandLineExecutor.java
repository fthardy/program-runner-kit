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

import java.util.Objects;

/**
 * The implementation of a command line executor for a program which separates the program execution in three phases: startup, main and shutdown.
 * <p>
 * The startup- and shutdown-phases are optional while the main-phase is mandatory. The startup-phase is executed first as long it exists. If it returns
 * the defined status code for success the main phase is going to be executed. If the main-phase returns the defined status code for sucess the shutdown-phase
 * is going to be executed. Then the executor is finished and returns the status code of the shutdown-phase if it existed or the main-phase otherwise. If an
 * exception is thrown by the startup- or main-phase the shutdown-phase is going to be executed as long it exists. However no exception handling is done in this
 * implementation - any runtime exception thrown by either of the phases is passed to the caller of the executor.
 * </p>
 */
public final class ThreePhaseCommandLineExecutor implements CommandLineExecutor {
    
    public static final int DEFAULT_SUCCESS_STATUS_CODE = 0; 

    private final CommandLineExecutor startUpPhase;
    private final CommandLineExecutor mainPhase;
    private final CommandLineExecutor shutDownPhase;
    private final int statusCodeForSuccess;

    /**
     * Creates a new instance of this executor.
     *
     * @param startUpPhase the part which implements the startup phase of the program. Can be null if not needed.
     * @param mainPhase the part which implements the main phase of the program. Is not allowed to be null.
     * @param shutDownPhase the part which implements the shutdown phase of the program. Can be null if not needed.
     */
    public ThreePhaseCommandLineExecutor(CommandLineExecutor startUpPhase,
                                         CommandLineExecutor mainPhase,
                                         CommandLineExecutor shutDownPhase) {
        this(startUpPhase, mainPhase, shutDownPhase, DEFAULT_SUCCESS_STATUS_CODE);
    }

    /**
     * Creates a new instance of this executor.
     *
     * @param startUpPhase the part which implements the startup phase of the program. Can be null if not needed.
     * @param mainPhase the part which implements the main phase of the program. Is not allowed to be null.
     * @param shutDownPhase the part which implements the shutdown phase of the program. Can be null if not needed.
     * @param statusCodeForSuccess the status code that indicates a successful execution.
     */
    public ThreePhaseCommandLineExecutor(CommandLineExecutor startUpPhase,
                                         CommandLineExecutor mainPhase,
                                         CommandLineExecutor shutDownPhase,
                                         int statusCodeForSuccess) {
        this.startUpPhase = startUpPhase;
        this.mainPhase = Objects.requireNonNull(mainPhase, "Undefined implementation for the main phase of the program!");
        this.shutDownPhase = shutDownPhase;
        this.statusCodeForSuccess = statusCodeForSuccess;
    }
    
    @Override
    public int execute(String[] args) {
        int statusCode = this.statusCodeForSuccess;
        
        try {
            if (this.startUpPhase != null) {
                statusCode = this.startUpPhase.execute(args);
            }

            if (statusCode == this.statusCodeForSuccess) {
                statusCode = this.mainPhase.execute(args);
            }
        } finally {
            if (this.shutDownPhase != null) {
                statusCode = this.shutDownPhase.execute(args);
            }
        }

        return statusCode;
    }
}
