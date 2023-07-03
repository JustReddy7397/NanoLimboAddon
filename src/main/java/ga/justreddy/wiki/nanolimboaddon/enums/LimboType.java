package ga.justreddy.wiki.nanolimboaddon.enums;

/**
 * @author JustReddy
 */
public enum LimboType {

    AFK,
    QUEUE;

    public static LimboType getType(String name) {
        for (LimboType type : LimboType.values()) {
            if (type.name().equalsIgnoreCase(name)) return type;
        }
        return null;
    }

}
