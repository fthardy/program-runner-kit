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
package io.github.fthardy.progrunnerkit.deltaspike;

import io.github.fthardy.progrunnerkit.core.CommandLineExecutor;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.enterprise.util.AnnotationLiteral;

/**
 * A program entry point implemenation which handles the start and stop of a CDI-Container through DeltaSpikes Container-Control-Module.
 * <p>
 * On execute the CDI-Container is booted. Then an instance of a {@link CommandLineExecutor} annotated with {@link CdiEnabledEntryPoint} is obtained from the
 * container. The execution is then delegated to this instance. When the calling thread returns the container is shut down.
 * </p>
 */
public final class DeltaSpikeContainerStarter implements CommandLineExecutor {
    
    private static final class CdiEnabledEntryPointLiteral extends AnnotationLiteral<CdiEnabledEntryPoint> implements CdiEnabledEntryPoint {};
    
    @Override
    public int execute(String[] args) {
        
        CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
        cdiContainer.boot();

        try {
            // Let the injection magic happen.
            return BeanProvider.getContextualReference(CommandLineExecutor.class, false, new CdiEnabledEntryPointLiteral()).execute(args);
        } finally {
            cdiContainer.shutdown();
        }
    }
}
