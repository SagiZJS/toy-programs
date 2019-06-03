package jishun.sorm.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import jishun.sorm.bean.ColumnInfo;
import jishun.sorm.bean.TableInfo;
import jishun.sorm.core.DBManager;
import jishun.sorm.core.MysqlTypeConvertor;
import jishun.sorm.core.TypeConvertor;

public class JavaFileUtils {

    /**
     * generate java src file from the {@link ColumnInfo}
     * 
     * @param columnInfo
     * @param convertor
     * @return
     */
    public static JavaFieldGenerator createJavaFieldGenerator(ColumnInfo columnInfo, TypeConvertor convertor) {
        return new JavaFieldGenerator.JavaFieldGeneratorBuilder(
                convertor.databaseType2JavaType(columnInfo.getDataType()), columnInfo.getName())
                        .setIndentifier("private").build();
    }

    public static String createJavaSourceCode(TableInfo tableInfo, TypeConvertor convertor) {
        StringBuilder sourceCode = new StringBuilder();
        Map<String, ColumnInfo> columns = tableInfo.getColumns();
        sourceCode.append(String.format(
                "package %s;\n\nimport java.sql.*;\nimport java.util.*;\n\n@SuppressWarnings(\"unused\")\npublic final class %s {\n\n",
                DBManager.getPoPackage(),
                tableInfo.getTableName().substring(0, 1).toUpperCase() + tableInfo.getTableName().substring(1)));
        StringBuilder fileds = new StringBuilder();
        StringBuilder setters = new StringBuilder();
        StringBuilder getters = new StringBuilder();
        for (ColumnInfo columnInfo : columns.values()) {
            JavaFieldGenerator fieldGenerator = createJavaFieldGenerator(columnInfo,
                    MysqlTypeConvertor.getMysqlTypeConvertor());
            fileds.append(fieldGenerator.getFieldBlock());
            setters.append(fieldGenerator.getSetBlock());
            getters.append(fieldGenerator.getGetBlock());
        }
        return sourceCode.append(fileds).append(setters).append(getters).append("}").toString();
    }

    public static Class<?> createJavaFile(TableInfo tableInfo, TypeConvertor convertor) {
        StringBuilder path = new StringBuilder().append(DBManager.getSrcPath());
        for (String string : DBManager.getPoPackage().split("\\.")) {
            path.append(java.io.File.separator + string);
        }
        path.append(java.io.File.separator);
        path.append(tableInfo.getTableName().substring(0, 1).toUpperCase() + tableInfo.getTableName().substring(1)
                + ".java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()))) {
            writer.write(createJavaSourceCode(tableInfo, convertor));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return Class.forName(DBManager.getPoPackage()+"."+tableInfo.getTableName().substring(0, 1).toUpperCase() + tableInfo.getTableName().substring(1));
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException(cnfe);
        }
    }
    
    

}
