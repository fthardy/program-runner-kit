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

import io.github.fthardy.progrunnerkit.core.Prioritized;
import io.github.fthardy.progrunnerkit.core.StartPhaseTask;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import java.util.List;

/**
 * Starts the application through an {@link ApplicationEntryPoint}.
 * <p>
 * The application entry point is retrieved from the {@link BeanProvider} and the {@link ApplicationEntryPoint#run(List)} method is going to be invoked.
 * </p>
 * <p>
 * The default priority of this task is 0.
 * </p>
 */
public class DefaultDeltaSpikeCdiApplicationStarterTask implements Prioritized, StartPhaseTask {

    /**
     * Interface definition for the application start entry point.
     */
    public interface ApplicationEntryPoint {
        
        /**
         * Is called by the {@link DefaultDeltaSpikeCdiApplicationStarterTask} to start the application. 
         */
        void runApplication();
    }
    
    @Override
    public void run(List<String> arguments) {
        BeanProvider.getContextualReference(ApplicationEntryPoint.class).runApplication();
    }
}
