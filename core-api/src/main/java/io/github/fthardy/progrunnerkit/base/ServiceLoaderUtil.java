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

import java.util.Iterator;

/**
 * A utility method container.
 */
public final class ServiceLoaderUtil {

    /**
     * Expect and retrieve one service implementation instance of a given type from a given iterable ({@link java.util.ServiceLoader} instance).
     * 
     * @param typeClass the type class of the elements in the iterable.
     * @param iterable the iterable to get the element from.
     * 
     * @param <T> the type of the element to get from the iterable.
     *     
     * @return the element of the given iterable.
     * 
     * @throws IllegalStateException when the given Iterable does not contain exactly one element.
     */
    public static <T> T expectAndRetrieveExactlyOneServiceImplInstanceOfType(Class<T> typeClass, Iterable<T> iterable) {
        Iterator<T> iterator = iterable.iterator();
        if (iterator.hasNext()) {
            T implInstance = iterator.next();
            if (iterator.hasNext()) {
                throw new IllegalStateException(String.format("More than one service provider implementation of type [%s] found!", typeClass.getName()));
            } else {
                return implInstance;
            }
        } else {
            throw new IllegalStateException(String.format("No service provider implementation of type [%s] found!", typeClass.getName()));
        }
    }

    // Do not allow to create instances of this class
    private ServiceLoaderUtil() {
        // do nothing
    }
}
