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
package io.github.fthardy.progrunnerkit.cdi.deltaspike;

import io.github.fthardy.progrunnerkit.core.CommandLineExecutor;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.enterprise.context.ApplicationScoped;

/**
 * A main class implementation which starts a CDI-Container instance through DeltaSpikes Container-Control-Module.
 * <p>
 * When the main method is executed the CDI-Container is booted. Then an instance of a {@link CommandLineExecutor} implementation annotated with
 * {@link ApplicationScoped} is obtained from the container. The execution is then delegated to that instance. When the calling thread returns the container is
 * shut down and {@link System#exit(int)} is called passing the returned status code.
 * </p>
 * <p>
 * No exception handling is done in this implementation. Any runtime exception will cause the program to end immediately throwing this exception.
 * </p> */
public final class DeltaSpikeCdiContainerStarterMain {

    public static void main(String[] args) {

        CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
        cdiContainer.boot();

        try {
            cdiContainer.getContextControl().startContext(ApplicationScoped.class);
            System.exit(BeanProvider.getContextualReference(CommandLineExecutor.class, new ApplicationScoped.Literal()).execute(args));
        } finally {
            cdiContainer.shutdown();
        }
    }
}
