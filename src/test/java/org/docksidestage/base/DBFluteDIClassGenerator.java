package org.docksidestage.base;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.util.JdbcConstants;

/**
 * 
 */
public class DBFluteDIClassGenerator {

    public static final String PACKAGE_NAME = "org.docksidestage";
    public static final String OUTPUT_DIR = "src/main/java/org/docksidestage/base";
    public static final String DDL_FILE_PATH = "./dbflute_maihamadb/playsql/replace-schema-10-basic.sql";

    @Test
    public void generate() throws Exception {
        var ddl = readDDL();
        var tableNameList = parseDDL(ddl);
        createProducerFile(tableNameList);
    }

    private void createProducerFile(List<String> tableNameList) {
        var builder = new StringBuilder();
        builder.append("package ");
        builder.append(PACKAGE_NAME);
        builder.append(".base;\n\n");
        builder.append("import com.google.inject.Inject;\n");
        builder.append("import jakarta.enterprise.context.ApplicationScoped;\n");
        builder.append("import ");
        builder.append(PACKAGE_NAME);
        builder.append(".dbflute.exbhv.*;\n");
        builder.append("\n");

        builder.append("/**\n");
        builder.append(" * Class that allows Google Guice to DI and generate Behavior instances and DI them to CDI\n");
        builder.append(" */\n");
        builder.append("public class DBFluteBehaviorProducer {\n\n");
        tableNameList.forEach(tableName -> {
            builder.append("  @Inject ");
            builder.append(toCamelCase(tableName));
            builder.append("Bhv ");
            builder.append(toCamelCaseSmallFirst(tableName));
            builder.append("Bhv;\n\n");
        });
        tableNameList.forEach(tableName -> {
            builder.append("  @ApplicationScoped\n");
            builder.append("  public ");
            builder.append(toCamelCase(tableName));
            builder.append("Bhv get");
            builder.append(toCamelCase(tableName));
            builder.append("Bhv() {\n");
            builder.append("    return GuiceComponents.find(this.getClass()).");
            builder.append(toCamelCaseSmallFirst(tableName));
            builder.append("Bhv;\n");
            builder.append("  }\n\n");
        });
        builder.append("}");
        String outputFile = "DBFluteBehaviorProducer.java";
        try {
            Files.createDirectories(Paths.get(OUTPUT_DIR));
            Files.write(Paths.get(OUTPUT_DIR + "/" + outputFile), builder.toString().getBytes(), StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(outputFile + "の生成に失敗しました。", e);
        }
    }

    private List<String> parseDDL(String ddl) {
        return SQLUtils.parseStatements(ddl, JdbcConstants.MYSQL).stream().map(statement -> {
            if (statement instanceof SQLCreateTableStatement) {
                SQLCreateTableStatement createTableStatement = (SQLCreateTableStatement) statement;
                return createTableStatement.getName().getSimpleName();
            }
            return null;
        }).filter(result -> result != null).toList();
    }

    private String readDDL() {
        StringBuilder ddlBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(Paths.get(DDL_FILE_PATH))) {
            while (scanner.hasNextLine()) {
                ddlBuilder.append(scanner.nextLine()).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ddlBuilder.toString();
    }

    private static String toCamelCase(String str) {
        StringBuilder builder = new StringBuilder();
        boolean toUpperCase = true;
        for (char c : str.toCharArray()) {
            if (c == '_') {
                toUpperCase = true;
            } else {
                if (toUpperCase) {
                    builder.append(Character.toUpperCase(c));
                    toUpperCase = false;
                } else {
                    builder.append(Character.toLowerCase(c));
                }
            }
        }
        return builder.toString();
    }

    private static String toCamelCaseSmallFirst(String str) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        boolean toUpperCase = true;
        for (char c : str.toCharArray()) {
            if (c == '_') {
                toUpperCase = true;
            } else {
                if (toUpperCase) {

                    if (isFirst) {
                        builder.append(Character.toLowerCase(c));
                        isFirst = false;
                    } else {
                        builder.append(Character.toUpperCase(c));
                    }
                    toUpperCase = false;
                } else {
                    builder.append(Character.toLowerCase(c));
                }
            }
        }
        return builder.toString();
    }
}
