package io.github.fthardy.progrunnerkit.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicProgramExecutionContextTest {
    
    @Test
    void Args_list_is_not_allowed_to_be_null() {
        assertThrows(NullPointerException.class, () -> new BasicProgramExecutionContext((List<String>) null, null));
        assertThrows(NullPointerException.class, () -> new BasicProgramExecutionContext((String[]) null, null));
    }

    @Test
    void Given_args_list_is_immutable() {
        
        ProgramExecutionContext context = new BasicProgramExecutionContext(new String[] {"foo"}, null);

        List<String> commandLineInputArguments = context.getCommandLineInputArguments();
        
        assertEquals(1, commandLineInputArguments.size());
        
        assertThrows(UnsupportedOperationException.class, () -> commandLineInputArguments.add("bar"));
    }
}