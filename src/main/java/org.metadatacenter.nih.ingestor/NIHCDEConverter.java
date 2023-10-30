package org.metadatacenter.nih.ingestor;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.nih.ingestor.converter.Converter;
import org.metadatacenter.nih.ingestor.exceptions.DesignationNotFoundException;
import org.metadatacenter.nih.ingestor.exceptions.InvalidJsonPathException;
import org.metadatacenter.nih.ingestor.exceptions.RESTRequestFailedException;
import org.metadatacenter.nih.ingestor.exceptions.UnsupportedDataTypeException;
import org.metadatacenter.nih.ingestor.poster.Poster;

import java.util.ArrayList;
import java.io.IOException;

public class NIHCDEConverter {

    public static void main( String[] args )
            throws IOException, InvalidJsonPathException, DesignationNotFoundException,
            UnsupportedDataTypeException, RESTRequestFailedException {

        String fileName = args[0];
        String apiKey = args[1];
        String targetFolder = args[2];
        Converter converter = new Converter();
        Poster poster = new Poster(apiKey, targetFolder);

        ArrayList<ObjectNode> cedarCDEs = converter.convertCDEsToCEDAR(fileName);
        for (ObjectNode cde: cedarCDEs) {
            System.out.println(cde);
            poster.validate(cde);
            // poster.put(cde);
        }
    }
}