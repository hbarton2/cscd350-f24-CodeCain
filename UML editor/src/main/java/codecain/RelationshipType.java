package codecain;


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

}
