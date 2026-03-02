package org.example.model;

import java.util.Objects;

public class DocumentPhoto {
    private final PhotoQuality quality;
    private final String label;

    public DocumentPhoto(PhotoQuality quality, String label) {
        this.quality = Objects.requireNonNull(quality, "quality não pode ser nulo");
        this.label = label;
    }

    public PhotoQuality getQuality() {
        return quality;
    }

    public String getLabel() {
        return label;
    }
}