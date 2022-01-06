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

import io.github.fthardy.progrunnerkit.core.CommandLineExecutor;

import java.util.*;

/**
 * A class with a default implemenation of a main-method.
 * <p>
 * The implementation is based on the {@link ServiceLoader} facility of the JDK. A ServiceLoader is used to load an implementation instance of type 
 * {@link CommandLineExecutorProviderService}. It is expected that exactly one implementation is bound by a service-provider configuration. If no implementation or
 * more than one implementation is found the program ends immediately by throwing an {@link IllegalStateException}.
 * </p>
 * <p>
 * A {@link CommandLineExecutor} implementation as the root entry point for the program is obtained from the {@link CommandLineExecutorProviderService} instance.
 * The returned status code is used as the exit code while the process is stopped by calling {@link System#exit(int)}.
 * </p>
 * <p>
 * No exception handling is done in this implementation. Any runtime exception will cause the program to end immediately throwing this exception.
 * </p>
 * 
 * @see CommandLineExecutorProviderService
 */
public final class BaseMain {

    public static void main(String[] args) {
        System.exit(startProgram(args));
    }

    /**
     * Is called by {@link #main(String[])} and starts the programm.
     * 
     * @param args the arguments from the command line.
     *             
     * @return a status code to be returned by the process via {@link System#exit(int)}.
     */
    public static int startProgram(String[] args) {
        CommandLineExecutorProviderService providerService = ServiceLoaderUtil.expectAndRetrieveExactlyOneServiceImplInstanceOfType(
                CommandLineExecutorProviderService.class, ServiceLoader.load(CommandLineExecutorProviderService.class));
        return providerService.getCommandLineExecutorImpl().execute(args);
    }
    
    // Creating instances of this class is not allowed 
    private BaseMain() {
        // intentionally empty
    }
}
