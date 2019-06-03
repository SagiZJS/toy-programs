package jishun.sorm.utils;

public class JavaFieldGenerator {

    /**
     * src cod for field, e.g. private int userId;
     */
    private String fieldBlock;
    /**
     * getter
     */
    private String getBlock;
    /**
     * setter
     */
    private String setBlock;

    public static class JavaFieldGeneratorBuilder {

        private String identifier = "";

        private String type;

        private String variableName;

        public JavaFieldGeneratorBuilder(String type, String variableName) {
            super();
            this.type = type;
            this.variableName = variableName;
        }

        public JavaFieldGeneratorBuilder setIndentifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public JavaFieldGenerator build() {
            return new JavaFieldGenerator(this);
        }
    }
    

    public String getFieldBlock() {
        return fieldBlock;
    }

    public String getGetBlock() {
        return getBlock;
    }

    public String getSetBlock() {
        return setBlock;
    }

    private JavaFieldGenerator(JavaFieldGeneratorBuilder builder) {
        fieldBlock = String.format("    %s %s %s;\n\n", builder.identifier, builder.type, builder.variableName);

        getBlock = String.format("    public %s get%s() {\n        return %s;\n    }\n\n", builder.type,
                builder.variableName.substring(0, 1).toUpperCase() + builder.variableName.substring(1),
                builder.variableName);
        setBlock = String.format("    public void set%s(%s %s) {\n        this.%s = %s;\n    }\n\n",
                builder.variableName.substring(0, 1).toUpperCase() + builder.variableName.substring(1),
                builder.type,
                builder.variableName,
                builder.variableName, 
                builder.variableName);
    }

    @Override
    public String toString() {
        return fieldBlock + setBlock + getBlock;
    }

}
