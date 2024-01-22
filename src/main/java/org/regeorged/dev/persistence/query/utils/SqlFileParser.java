package org.regeorged.dev.persistence.query.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SqlFileParser {

    public String parseSqlFile(String sqlFile) {
        StringBuffer sql = new StringBuffer();
        Path path = Paths.get(sqlFile);
        try(BufferedReader br = Files.newBufferedReader(path)) {
        String line;
        while((line = br.readLine()) != null) {
            sql.append(line).append("\n");
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sql.toString();
    }
}
