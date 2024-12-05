package codecain.BackendCode.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RelationshipType {
    AGGREGATION,
    COMPOSITION,
    GENERALIZATION,
    REALIZATION;

    /**
     * returns the string name of the relationship type
     * @return String - the name of the relationship type
     */
    public String toString(){
        switch (this){
            case GENERALIZATION -> {
                return "Generalization";
            }
            case REALIZATION -> {
                return "Realization";
            }
            case COMPOSITION -> {
                return "Composition";
            }
            case AGGREGATION -> {
                return "Aggregation";
            }
        }
        return null;
    }

    public static RelationshipType fromString(String type){
        if (type == null || type.trim().isEmpty()){
            System.err.println("Input cannot be null or empty.");
            return null;
        }
        switch (type.toLowerCase().trim()){
            case "generalization"  -> {
                return GENERALIZATION;
            }
            case "realization" -> {
                return REALIZATION ;
            }
            case "composition" -> {
                return COMPOSITION;
            }
            case "aggregation" -> {
                return AGGREGATION;
            }
        }
        System.err.println("Invalid relationship type: " + type);
        return null;
    }

    public static boolean typeExists(String typeName){
        if (typeName == null || typeName.trim().isEmpty()){
            return false;
        }
        typeName = typeName.trim().toLowerCase();
        return typeName.equals("composition")
                || typeName.equals("aggregation")
                || typeName.equals("realization")
                || typeName.equals("generalization");
    }

    /**
     * @return - returns an ascii representation of an arrow associated with the relationship
     * if the relationship type does not exist, it returns null
     *
     */
    public String getArrowString(){
        switch (this){
            case GENERALIZATION -> {
                return " -----|> ";
            }
            case REALIZATION -> {
                return " - - -|> ";
            }
            case COMPOSITION -> {
                return " <*>---- ";
            }
            case AGGREGATION -> {
                return " <>----- ";
            }
        }
        return null;
    }

    /**
     * constructor needed for save/load
     * @param node json node
     * @return the relationship value of the name of the node
     */
    @JsonCreator
    public static RelationshipType fromNode(JsonNode node) {
        if (node == null || !node.has("name")) {
            throw new IllegalArgumentException("Invalid input: JSON node must have a 'name' property.");
        }
        String name = node.get("name").asText().toUpperCase();
        try {
            return RelationshipType.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid relationship type: " + name, e);
        }
    }

    /**
     * needed for serialization in jackson
     * @return the full name of the enum value
     */
    @JsonProperty
    public String getName(){
        return name();
    }

}


