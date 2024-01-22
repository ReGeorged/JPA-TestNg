package org.regeorged.dev.persistence.query.utils;

import java.util.HashMap;
import java.util.Map;

public class QueryBuilder {
    private String query;
    private Map<String,String> replacements;

    public QueryBuilder(String queryPath){
        if(queryPath == null || queryPath.isEmpty()){
            throw new IllegalArgumentException("Query path cannot be null or empty");
        }if(!queryPath.endsWith(".sql")){
            throw new IllegalArgumentException("Query path must end with .sql");
        }
        this.query = new SqlFileParser().parseSqlFile(queryPath);
        this.replacements = new HashMap<String,String>();
    }

    public QueryBuilder replace(String property, Object value){
        if(property == null || property.isEmpty()){
            throw new IllegalArgumentException("Property cannot be null or empty");
        }
        if(value == null){
            throw new IllegalArgumentException("Value cannot be null");
        }
        if(!property.startsWith(":")){
           throw new IllegalArgumentException("Property must start with :");
        }
        this.replacements.put(property, value.toString());
        return this;
    }

    public String build(){
        String result = this.query;
        for(Map.Entry<String,String> entry : this.replacements.entrySet()){
            result = result.replace(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
