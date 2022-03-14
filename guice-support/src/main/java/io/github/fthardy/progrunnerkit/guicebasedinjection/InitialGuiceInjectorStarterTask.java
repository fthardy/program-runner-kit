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

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.github.fthardy.progrunnerkit.core.Prioritized;
import io.github.fthardy.progrunnerkit.core.StartPhaseTask;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * A program entry point which creates a Guice-Injector instance with all Guice-Modules created by each of the found {@link InitialGuiceModuleProviderService}
 * instances.
 * <p>
 * The {@link InitialGuiceModuleProviderService} instances are loaded by a {@link ServiceLoader}.
 * </p>
 */
public final class InitialGuiceInjectorStarterTask implements Prioritized, StartPhaseTask {
    
    private final static InheritableThreadLocal<Injector> _threadLocalGuiceInjector = new InheritableThreadLocal<>();
    
    private final Iterable<InitialGuiceModuleProviderService> services;
    
    public InitialGuiceInjectorStarterTask() {
        this(ServiceLoader.load(InitialGuiceModuleProviderService.class));
    }
    
    public InitialGuiceInjectorStarterTask(Iterable<InitialGuiceModuleProviderService> services) {
        this.services = services;
    }

    /**
     * Provides access to the thread local injector instance.
     * 
     * @return the injector instance.
     */
    public static Injector getInjector() {
        Injector injector = _threadLocalGuiceInjector.get();
        if (injector == null) {
            throw new IllegalStateException("No injector available!");
        }
        return injector;
    }
    
    @Override
    public void run(List<String> args) {
        _threadLocalGuiceInjector.set(Guice.createInjector(StreamSupport.stream(this.services.spliterator(), false) //
                .map(InitialGuiceModuleProviderService::createInitialGuiceModule).collect(Collectors.toList())));
    }
}
