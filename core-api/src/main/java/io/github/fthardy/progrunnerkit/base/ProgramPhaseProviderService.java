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

import io.github.fthardy.progrunnerkit.core.ProgramPartEntryPoint;

/**
 * A provider service for the program phase implemenations which are used by a .
 * <p>
 * An implementation of this interface is intended to be bound by a service-provider configuration for a {@link java.util.ServiceLoader}.
 * </p>
 * <p>
 * Provides the implementation of the main phase for the program. An implementor of this interface can additionally implement {@link StartUpPhaseImplProvider}
 * and/or {@link ShutDownPhaseImplProvider} to provide an implementation for a startup and/or shutdown phase.
 * </p>
 * 
 * @see StartUpPhaseImplProvider
 * @see ShutDownPhaseImplProvider
 */
public interface ProgramPhaseProviderService {

    /**
     * An implementor of {@link ProgramPhaseProviderService} can additionally implement this interface to provide an implementation of a startup phase. 
     */
    interface StartUpPhaseImplProvider {
        ProgramPartEntryPoint getStartUpPhaseImpl();
    }

    /**
     * An implementor of {@link ProgramPhaseProviderService} can additionally implement this interface to provide an implementation of a shutdown phase. 
     */
    interface ShutDownPhaseImplProvider {
        ProgramPartEntryPoint getShutDownPhaseImpl();
    }

    /**
     * @return the main phase implementation instance. Never {@code null}.
     */
    ProgramPartEntryPoint getMainPhaseImpl();
}
