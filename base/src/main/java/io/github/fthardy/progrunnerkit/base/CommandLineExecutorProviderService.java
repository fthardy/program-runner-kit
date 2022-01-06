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

/**
 * A service provider interface definition which provides the command line executor which serves as the main entry point for a program.
 * <p>
 * An implementation of this interface is intended to be bound by a service-provider configuration for a {@link java.util.ServiceLoader}.
 * </p>
 * <p>
 * Provides a {@link CommandLineExecutor} implemenation which implements the program main functionality. The intention is that this entry point is invoked by
 * a main-method.
 * </p>
 * 
 * @see BaseMain
 */
public interface CommandLineExecutorProviderService {

    /**
     * @return the command line executor implemenation which serves as the root entry point for the program.
     */
    CommandLineExecutor getCommandLineExecutorImpl();
}
