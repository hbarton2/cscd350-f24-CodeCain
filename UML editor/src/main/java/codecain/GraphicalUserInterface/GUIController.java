package codecain.GraphicalUserInterface;

public class GUIController {
    private final GUIClassManager classManager;
    private final GUIFieldManager fieldManager;
    private final GUIMethodManager methodManager;
    private final GUIRelationshipManager relationshipManager;

    public GUIController(GUIClassManager classManager, GUIFieldManager fieldManager, GUIMethodManager methodManager, GUIRelationshipManager relationshipManager) {
        this.classManager = classManager;
        this.fieldManager = fieldManager;
        this.methodManager = methodManager;
        this.relationshipManager = relationshipManager;
    }

    public void handleAddClass() {
        classManager.addClass();
    }

    public void handleDeleteClass() {
        classManager.deleteClass();
    }

    public void handleRenameClass() {
        classManager.renameClass();
    }

    public void handleAddField() {
        fieldManager.addField();
    }

    public void handleDeleteField() {
        fieldManager.deleteField();
    }

    public void handleRenameField() {
        fieldManager.renameField();
    }

    public void handleAddMethod() {
        methodManager.addMethod();
    }

    public void handleDeleteMethod() {
        methodManager.deleteMethod();
    }

    public void handleRenameMethod() {
        methodManager.renameMethod();
    }

    public void handleAddParameter() {
        methodManager.addMethod();
    }

    public void handleDeleteParameter() {
        methodManager.deleteMethod();
    }

    public void handleRenameParameter() {
        methodManager.renameMethod();
    }

    public void handleAddRelationship() {
        relationshipManager.addRelationship();
    }

    public void handleDeleteRelationship() {
        relationshipManager.deleteRelationship();
    }


    public void handleSave() {
        // Save logic here
    }

    public void handleLoad() {
        // Load logic here
    }
}
