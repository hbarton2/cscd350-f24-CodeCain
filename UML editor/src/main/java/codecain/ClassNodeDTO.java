package codecain;

import java.util.List;

public class ClassNodeDTO {
    private String className;
    private List<String> fields;
    private List<String> methods;

    // No-argument constructor (required for Jackson)
    public ClassNodeDTO() {}

    public ClassNodeDTO(String className, List<String> fields, List<String> methods) {
        this.className = className;
        this.fields = fields;
        this.methods = methods;
    }

    // Getters and setters
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }
}
