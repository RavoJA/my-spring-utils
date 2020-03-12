package jean.aime.myutils.core;


import java.util.UUID;

/**
 * Utility class for generating values.
 */
public final class GeneratorUtils {
    /**
     * Generates a unique identifier.
     *
     * @return the UUID.
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generates a unique identifier too.
     *
     * @return the identifier.
     */
    public static Long longUuid() {
        return (long) Math.abs(uuid().hashCode());
    }
}
