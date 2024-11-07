package codecain;

import java.util.HashMap;
import java.util.Map;

public class Storage {

    public static Map<String, ClassNode> storage = new HashMap<String, ClassNode>();

    public static Result addClass(String className) {
        if(className == null || className.isBlank()) {
            System.out.println("Canceled: Empty input");
            return new Result(Status.WARNING, "Canceled: Empty input");
        } else {
            storage.put(className, new ClassNode(className));
            System.out.println("Class " + className + " added");
            return new Result(Status.SUCCESS, "Class " + className + " added");
        }
    }

    public static Result renameClass(String className) {
        if(className == null || className.isBlank()) {
            System.out.println("Canceled: Empty input");
            return new Result(Status.WARNING, "Canceled: Empty input");
        } else if(exists(className)) {
            System.out.println("Canceled: Class " + className + " already exists");
            return new Result(Status.ERROR, "Canceled: Class " + className + " already exists");
        } else {
            storage.put(className, new ClassNode(className));
            System.out.println("Class " + className + " added");
            return new Result(Status.SUCCESS, "Class " + className + " added");
        }
    }

    public static boolean exists(String className) {
        return storage.containsKey(className);
    }

    public static void printStorage() {
        for(Map.Entry<String, ClassNode> entry : storage.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().getName());

        }
    }

    public static void loadClass(String className, ClassNode classNode) {
        storage.put(className, classNode);
    }

    public static void clear() {
        storage.clear();
    }
}
