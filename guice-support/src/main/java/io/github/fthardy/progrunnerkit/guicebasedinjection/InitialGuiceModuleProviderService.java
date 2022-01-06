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
package io.github.fthardy.progrunnerkit.guicebasedinjection;

import com.google.inject.Module;

/**
 * Defines the interface for a provider service which provides a Guice-Module instance for the initial Guice-Injector created by the
 * {@link InitialGuiceInjectorStarter}.
 * <p>
 * Is is possible to define several implementations of this interface where each returns a different module. However, any of the module has to provide a
 * binding for an implementation of {@link GuiceBasedCommandLineExecutor}.
 * </p>
 * 
 * @see InitialGuiceInjectorStarter
 * @see GuiceBasedCommandLineExecutor
 */
public interface InitialGuiceModuleProviderService {

    /**
     * @return a Guice-Module instance.
     */
    Module createInitialGuiceModule();
}
