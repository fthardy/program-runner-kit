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

import java.util.List;

/**
 * Interface definition for the programm startup routine.
 * <p>
 * It is intended to implement any kind of initialisation and/or setup functionality for a program. 
 * </p>
 * <p>
 * {@link ProgramRunner} executes the startup routine at first. If the startup routine ends with a failure the program
 * ends directly returning the status code of the startup routine. If the startup routine is successful the program 
 * flow proceeds to the {@link MainRoutine}. 
 * </p>
 */
public interface StartUpRoutine {

    /**
     * Executes the startup routine of the program.
     * 
     * @param inputArguments an immutable list of the input arguments.
     *                       
     * @return an exit code which must be {@link ProgramStatusCodeProvider#success()} to indicate that the startup was
     * successful. If the startup fails an exit code unequal to {@link ProgramStatusCodeProvider#success()} must be
     * returned which must not collide with any of the codes from the {@link ProgramStatusCodeProvider}
     * implementation in use except {@link ProgramStatusCodeProvider#startUpFailure()} which is allowed to be returned.
     */
    int execute(List<String> inputArguments);
}
