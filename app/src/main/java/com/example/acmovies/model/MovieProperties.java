package com.example.acmovies.model;

public class MovieProperties {
    private String properties;
    private String value;

    public MovieProperties(String properties, String value) {
        this.properties = properties;
        this.value = value;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
