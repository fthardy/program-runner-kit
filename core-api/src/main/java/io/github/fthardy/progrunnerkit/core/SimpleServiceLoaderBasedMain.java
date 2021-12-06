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
package io.github.fthardy.progrunnerkit.core;

import java.util.*;

/**
 * A simple main class implementation which makes use of the {@link ServiceLoader} facility of the JDK to load an 
 * implementation of {@link MainRoutine}.
 * <p>
 * It has to be exactly one {@link MainRoutine} implementation that is bound by a <i>provider-configuration</i>. If none
 * or more than one is found an {@link IllegalStateException} is thrown.
 * </p>
 */
public class SimpleServiceLoaderBasedMain implements MainRoutine {

    public static void main(String[] args) {
        System.exit(loadAndStartMainRoutine(Arrays.asList(args), ServiceLoader.load(MainRoutine.class)));
    }
    
    static int loadAndStartMainRoutine(List<String> inputArguments, Iterable<MainRoutine> mainRoutinesFromLoader) {
        Iterator<MainRoutine> mainRoutineIterator = mainRoutinesFromLoader.iterator();
        if (mainRoutineIterator.hasNext()) {
            MainRoutine mainRoutine = mainRoutineIterator.next();
            if (mainRoutineIterator.hasNext()) {
                throw new IllegalStateException(
                        "More than one main routine implementation is bound to type: " + MainRoutine.class.getName());
            } else {
                return new SimpleServiceLoaderBasedMain(mainRoutine).execute(inputArguments);
            }
        } else {
            throw new IllegalStateException(
                    "No main routine implementation is bound to type: " + MainRoutine.class.getName());
        }
    }
    
    private final MainRoutine mainRoutine;
    
    SimpleServiceLoaderBasedMain(MainRoutine mainRoutine) {
        this.mainRoutine = Objects.requireNonNull(mainRoutine);
    }

    @Override
    public int execute(List<String> inputArguments) {
        return this.mainRoutine.execute(inputArguments);
    }
}
