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

import java.util.*;

/**
 * The program runner is intended to be used as the coordinator for running a program.
 * <p>
 * This class provides the {@link #runProgram()} method which implements the top level coordination of a program
 * execution process. The intention is to create and use an instance of this class from a static main-method by invoking 
 * the {@link #runProgram()} method.
 * </p>
 * <p>
 * To create an instance of this class a list of input arguments and implementations of the routines must be provided.
 * Optionally an instance of a {@link ProgramStatusCodeProvider} implementation can be provided as well. If no status
 * code provider is provided the {@link DefaultProgramStatusCodeProvider} implementation is used by default.
 * </p>
 * <p>
 * The {@link #runProgram()} method of this class implements the main concept of the top level program execution process
 * which is the key concept of this API. The concept separates a program execution process into three distinct phases
 * named startup, main and shutdown. Each phase is represented by a routine object which has its own dedicated interface
 * definition. During a program run each of these routines is executed once, one after the other in the previously given 
 * order. 
 * </p>
 * <p>
 * The startup- and shutdown routines are optional and can be omitted by passing {@code null} in. The main routine is 
 * mandatory because its intention is to implement the main functionality of the program.
 * </p>
 * <p>
 * If a startup routine is provided and fails neither the main nor the shutdown routine won't be run. Instead the 
 * program ends directly. If the startup routine is successful the main and finally the shutdown routine (as long as it
 * exists) will be run even if the main routine fails.
 * </p>
 * <p>
 * The program runner does not make any outputs. Neither on the console nor to the error stream. The only communication
 * to the caller is via a status code which is returned by the {@link #runProgram()} method. As previously mentioned 
 * there is a provider for the status codes which makes it possible to define your own set of internal status codes in 
 * case the default status codes are not usable for you.
 * </p>
 */
public class ProgramRunner {

    private final List<String> inputArguments;
    private final StartUpRoutine startUpRoutine;
    private final MainRoutine mainRoutine;
    private final ShutDownRoutine shutDownRoutine;
    private final ProgramStatusCodeProvider statusCodeProvider;

    public ProgramRunner(List<String> args,
                         StartUpRoutine startUpRoutine,
                         MainRoutine mainRoutine,
                         ShutDownRoutine shutDownRoutine,
                         ProgramStatusCodeProvider statusCodeProvider) {
        inputArguments = Collections.unmodifiableList(new ArrayList<>(args));
        
        this.startUpRoutine = startUpRoutine;
        this.mainRoutine = mainRoutine;
        if (this.mainRoutine == null) {
            throw new IllegalStateException("Routine provider must provide a main routine! null is not allowed.");
        }
        this.shutDownRoutine = shutDownRoutine;

        this.statusCodeProvider = Objects.requireNonNull(statusCodeProvider);
    }

    /**
     * Run the program.
     * 
     * @return the status code.
     */
    public int runProgram() {
        if (startUpRoutine != null) {
            int exitCode = startUpRoutine.execute(inputArguments);
            if (exitCode != statusCodeProvider.success()) {
                return exitCode;
            }
        }

        int exitCode = mainRoutine.execute(inputArguments);

        if (shutDownRoutine != null) {
            exitCode = shutDownRoutine.execute(inputArguments, exitCode);
        }

        return exitCode;
    }
}
