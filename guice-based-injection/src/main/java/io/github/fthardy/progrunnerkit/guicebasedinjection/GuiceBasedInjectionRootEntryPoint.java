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

import io.github.fthardy.progrunnerkit.core.ProgramPartEntryPoint;

/**
 * A marker type for a program entry point which is managed by a Guice-Injector.
 * <p>
 * An implementation should be typically a singleton (not a must) and must be provided in a Guice-Module binding where that Guice-Module has to be created and
 * provided by a {@link InitialGuiceModuleProviderService} implementation.
 * </p>
 * 
 * @see InitialGuiceModuleProviderService
 */
public interface GuiceBasedInjectionRootEntryPoint extends ProgramPartEntryPoint {
    // actually no further method definitions.
}
