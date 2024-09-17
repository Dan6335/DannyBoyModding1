package com.bdc.dannyboymodding.utils;

public enum FileTypes {
    JAVA("Java Files", "*.java"),
    TXT("Text Files", "*.txt"),
    XML("XML Files", "*.xml"),
    PNG("PNG Files", "*.png"),
    ALL("All Files", "*.*");

    private final String description;
    private final String extension;

    FileTypes(String description, String extension) {
        this.description = description;
        this.extension = extension;
    }

    public String getDescription() {
        return description;
    }

    public String getExtension() {
        return extension;
    }
}