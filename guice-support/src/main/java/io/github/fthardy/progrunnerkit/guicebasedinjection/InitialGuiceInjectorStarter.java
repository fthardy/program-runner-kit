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
import com.google.inject.Module;
import io.github.fthardy.progrunnerkit.core.CommandLineExecutor;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A program entry point which creates a Guice-Injector instance with all Guice-Modules created by each of the found {@link InitialGuiceModuleProviderService}
 * instances.
 * <p>
 * The {@link InitialGuiceModuleProviderService} instances are loaded by a {@link ServiceLoader}.
 * </p>
 */
public final class InitialGuiceInjectorStarter implements CommandLineExecutor {
    
    static final String MSG_NO_IMPLS_FOUND = String.format(
                "No implementation(s) found for '%s'! Make sure that at least one service-provider configuration for this type is in the class path available.",
                InitialGuiceModuleProviderService.class.getName());
    
    @Override
    public int execute(String[] args) {
        return this.createInjectorAndStartProgramEntryPoint(ServiceLoader.load(InitialGuiceModuleProviderService.class), args);
    }

    /**
     * Is called by {@link #execute(String[])}. Creates the injector, obtains an instance of {@link GuiceBasedCommandLineExecutor} and
     * delegates the execution to this instance.
     * 
     * @param moduleProviders the module providers.
     * @param args the command line arguments.
     *                
     * @return the status code from the delegate entry point.
     */
    int createInjectorAndStartProgramEntryPoint(Iterable<InitialGuiceModuleProviderService> moduleProviders, String[] args) {
        Stream<InitialGuiceModuleProviderService> stream = StreamSupport.stream(moduleProviders.spliterator(), false);
        List<Module> modules = stream.map(InitialGuiceModuleProviderService::createInitialGuiceModule).collect(Collectors.toList());
        if (modules.isEmpty()) {
            throw new IllegalStateException(MSG_NO_IMPLS_FOUND);
        }
        Injector initialInjector = Guice.createInjector(modules);

        // Let (most of) the injection-magic happen!
        GuiceBasedCommandLineExecutor entryPoint = initialInjector.getInstance(GuiceBasedCommandLineExecutor.class);

        return entryPoint.execute(args);
    }
}
