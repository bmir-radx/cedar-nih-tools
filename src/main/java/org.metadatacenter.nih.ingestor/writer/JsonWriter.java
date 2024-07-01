package org.metadatacenter.nih.ingestor.writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonWriter {
    ObjectMapper mapper = new ObjectMapper();

    public void writeToFile(List<ObjectNode> cdes, String path) throws IOException {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(path), cdes);
    }
}
