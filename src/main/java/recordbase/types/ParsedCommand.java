package recordbase.types;

/**
 * Contains a parsed command type and the arguments following its command word.
 *
 * @param type the identified command type
 * @param arguments the unmodified arguments following the command word
 */
public record ParsedCommand(CommandType type, String arguments) {
    /**
     * Returns whether arguments were supplied provided.
     *
     * @return true if the command has non-blank arguments
     */
    public boolean hasArguments() {
        return !arguments.isBlank();
    }
}
