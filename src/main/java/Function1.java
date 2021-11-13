public interface Function1<R> {
    /**
     * Apply some calculation to the input value and return some other value.
     * @return the output value
     * @throws Exception on error
     */
    R apply() throws Exception;
}
