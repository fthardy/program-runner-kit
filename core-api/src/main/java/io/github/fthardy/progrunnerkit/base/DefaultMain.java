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

import java.util.*;

/**
 * A class with a default implemenation of a main-method.
 * <p>
 * The implementation is based on the {@link ServiceLoader} facility of the JDK. A ServiceLoader is used to load an implementation instance of type 
 * {@link InitialProgramProviderService}. It is expected that exactly one implementation is bound by a service-provider configuration. If no implementation or
 * more than one implementation is found the program ends immediately by throwing an {@link IllegalStateException}.
 * </p>
 * <p>
 * A {@link ProgramPartEntryPoint} implementation as the root entry point for the program is obtained from the {@link InitialProgramProviderService} instance.
 * Then the
 * {@link ProgramPartEntryPoint#execute(ProgramExecutionContext)} methode is invoked. The returned status code is used as the exit code while the program is
 * ended by calling {@link System#exit(int)}. 
 * </p>
 * <p>
 * No exception handling is done in this implementation. Any runtime exception will cause the program to end immediately throwing this exception.
 * </p>
 * 
 * @see InitialProgramProviderService
 */
public final class DefaultMain {

    public static void main(String[] args) {
        System.exit(startProgram(args));
    }
    
    public static int startProgram(String[] args) {
        InitialProgramProviderService providerService = ServiceLoaderUtil.expectAndRetrieveExactlyOneServiceImplInstanceOfType(
                InitialProgramProviderService.class, ServiceLoader.load(InitialProgramProviderService.class));
        return providerService.getProgramRootEntryPointImpl().execute(providerService.createInitialProgramExecutionContext(args));
    }
    
    // Creating instances of this class is not allowed 
    private DefaultMain() {
        // intentionally empty
    }
}
