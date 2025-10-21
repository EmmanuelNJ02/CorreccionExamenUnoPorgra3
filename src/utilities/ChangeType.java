package utilities;

public class ChangeType {
    public static String nextId(String prefix, int number) {
        return String.format("%s%03d", prefix, number);
    }
}
