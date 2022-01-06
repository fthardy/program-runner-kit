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

import io.github.fthardy.progrunnerkit.base.CommandLineExecutorProviderService;
import io.github.fthardy.progrunnerkit.core.CommandLineExecutor;

/**
 * An implementation for a {@link CommandLineExecutorProviderService} which is used by
 * {@link io.github.fthardy.progrunnerkit.base.BaseMain}.
 * <p>
 * You might decide to use {@link io.github.fthardy.progrunnerkit.base.BaseMain} as your main program starter than you can put the FQN of this class into a
 * service-provider configuration file for {@link CommandLineExecutorProviderService}.
 * </p>
 * <p>
 * This implementation creates an instance of {@link InitialGuiceInjectorStarter}.
 * </p>
 */
public final class InitialGuiceInjectorStarterProviderService implements CommandLineExecutorProviderService {
    
    @Override
    public CommandLineExecutor getCommandLineExecutorImpl() {
        return new InitialGuiceInjectorStarter();
    }
}
