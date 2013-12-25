package com.zackbleach.memetable.contentextraction.extractor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

import com.zackbleach.memetable.contentextraction.entity.ExtractedEntity;

public interface Extractor {

    /**
     * Attempts to extract an Entity from a valid URL
     *
     * @param path
     *            Path to extract Entity from
     * @return {@link BufferedImage} Entity.
     * @throws IOException
     * @throws URISyntaxException
     */
    public abstract ExtractedEntity extractEntity(String path)
            throws IOException, URISyntaxException;

}
