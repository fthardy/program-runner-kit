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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;

/**
 * A utility class which is responsible for providing the implemenations of a particular service type.
 * <p>
 * The service implementations are going to be loaded by using the {@link ServiceLoader} facility of the JDK. All loaded implemenation instances are going to be
 * sorted.
 * </p>
 * <p>
 * Sorting of the implementation instances is done according to their priority. A priority can be defined by implementing {@link Prioritized}. The priority
 * value obtained by {@link Prioritized#getPriority()} is used for sorting. The sorting is done according to the natural order. This means the lower the value
 * the higher the priority. If an instance does not implement {@link Prioritized} it has the lowest priority. The order for instances with the same priortity is
 * undefined. 
 * </p>
 * 
 * @param <T> the type of service to load.
 */
public class ServiceImplProvider<T> {
    
    private final Iterable<T> services;
    
    public ServiceImplProvider(Class<T> serviceTypeClass) {
        this(ServiceLoader.load(serviceTypeClass));
    }
    
    public ServiceImplProvider(Iterable<T> services) {
        this.services = services;
    }

    /**
     * @return a collection of the sorted implementation instances.
     */
    public Collection<T> provideImpls() {
        List<T> list = new ArrayList<>();
        this.services.forEach(list::add);
        list.sort((impl1, impl2) -> {
            Integer order1 = Integer.MAX_VALUE;
            if (impl1 instanceof Prioritized) {
                order1 = ((Prioritized) impl1).getPriority();
            }
            int order2 = Integer.MAX_VALUE;
            if (impl2 instanceof Prioritized) {
                order2 = ((Prioritized) impl2).getPriority();
            }
            return order1.compareTo(order2);
        });
        return list;
    }
}
