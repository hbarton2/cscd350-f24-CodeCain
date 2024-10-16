import java.util.Arrays;

public class TestUML {

    public static void main(String[] args) {
        // Test adding classes
        Class.addClass("Person");
        Class.addClass("Animal");

        // Test Fields
        Fields fields = new Fields();
        fields.addField("Person", "name");
        fields.addField("Person", "age");
        fields.addField("Animal", "species");

        // Test Methods
        Methods methods = new Methods();
        methods.addMethod("Person", "greet", Arrays.asList("String greeting"));
        methods.addMethod("Animal", "speak", Arrays.asList("String sound"));

        // Test Field rename
        fields.renameField("Person", "name", "fullName");

        // Test Method rename
        methods.renameMethod("Animal", "speak", "makeSound");

        // Test removing Field
        fields.removeField("Person", "age");

        // Test removing Method
        methods.removeMethod("Person", "greet");
    }
}
