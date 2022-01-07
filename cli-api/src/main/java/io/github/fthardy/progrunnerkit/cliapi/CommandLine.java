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
package io.github.fthardy.progrunnerkit.cliapi;

import java.util.List;
import java.util.Optional;

/**
 * Represents a parsed command line and defines the interface for the command line from the view point of the application programmer.
 */
public interface CommandLine {

    /**
     * Check if a particular parameter of a given identifier is set.
     * <p>
     * A parameter might be an optional switch or a single value or a list of values. This method is the only way to test if an optional switch is set in the
     * command line arguments.
     * </p>
     * 
     * @param id the parameter identifier to check for.
     *           
     * @return {@code true} when the parameter is set. Otherwise {@code false}.
     */
    boolean isSet(String id);

    /**
     * Get a list with all original command line arguments - in fact the list of arguments on which the parser created the receiving command line object.
     * 
     * @return an immutable list of the original argument values from the command line. 
     */
    List<String> getArguments();

    /**
     * Get a list of the remaining argument values from the parsed command line arguments which have not been parsed.
     * 
     * @return an immutable list of argument values. The list is in fact a trailing part of the {@link #getArguments() full argument list}.
     */
    List<String> getUnparsedArguments();

    /**
     * Get the value of a parameter or the first value of a parameter list.
     * <p>
     * For an optional switch this method will always return an emtpy optional regardless whether the switch is present in the command line. 
     * </p>
     * 
     * @param id the identifier of the parameter.
     *           
     * @return an optional containing the raw string value from the command line. If the parameter value is not available the optional is emtpy.
     */
    Optional<String> getParameterValue(String id);

    /**
     * Get a list of parameter values.
     * <p>
     * For an optional switch this method will always return an emtpy list regardless whether the switch is present in the command line. 
     * </p>
     * 
     * @param id the identifier of the parameter.
     *           
     * @return an immutable list of string values which represent the raw values from the command line. If the parameter is not available the list is empty.
     */
    List<String> getParameterList(String id);

    /**
     * Get the value of a parameter as the given type.
     * <p>
     * For an optional switch this method will always return an emtpy optional regardless whether the switch is present in the command line. 
     * </p>
     * 
     * @param type the type class of the parameter.
     * @param id the identifier of the parameter.
     * @param <T> the type of the parameter.
     *           
     * @return an optional containing the parameter object with the desired type. If the parameter is not available the optional is emtpy.
     * 
     * @throws ClassCastException when a parameter for the given identifier exists but the given type is not supported.
     */
    <T> Optional<T> getParameterValueAsType(Class<T> type, String id) throws ClassCastException;
}
