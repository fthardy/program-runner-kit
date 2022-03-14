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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceImplProviderTest {

    @Test
    public void Sorting_is_done_in_natural_order() {
        
        Prioritized last = mock(Prioritized.class, "last");
        Prioritized first = mock(Prioritized.class, "first");
        Prioritized middle = mock(Prioritized.class, "middle");
        
        when(last.getPriority()).thenReturn(Integer.MAX_VALUE);
        when(first.getPriority()).thenReturn(Integer.MIN_VALUE);
        when(middle.getPriority()).thenReturn(0);
        
        ServiceImplProvider<Object> provider = new ServiceImplProvider<>(Arrays.asList(last, first, middle));

        assertThat(provider.provideImpls()).containsExactly(first, middle, last);
    }

    @Test
    public void Non_prioritized_instances_are_always_at_last() {

        Prioritized last = mock(Prioritized.class, "last");
        Prioritized first = mock(Prioritized.class, "first");
        Prioritized middle = mock(Prioritized.class, "middle");
        Object veryLast = new Object();

        when(last.getPriority()).thenReturn(Integer.MAX_VALUE);
        when(first.getPriority()).thenReturn(Integer.MIN_VALUE);
        when(middle.getPriority()).thenReturn(0);
        
        ServiceImplProvider<Object> provider = new ServiceImplProvider<>(Arrays.asList(last, veryLast, first, middle));

        assertThat(provider.provideImpls()).containsExactly(first, middle, last, veryLast);
    }
}