package org.metadatacenter.nih.ingestor;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.nih.ingestor.converter.Converter;
import org.metadatacenter.nih.ingestor.exceptions.DesignationNotFoundException;
import org.metadatacenter.nih.ingestor.exceptions.InvalidJsonPathException;
import org.metadatacenter.nih.ingestor.exceptions.UnsupportedDataTypeException;

import java.util.ArrayList;
import java.io.IOException;

public class NIHCDEConverter {

    public static void main( String[] args )
            throws IOException, InvalidJsonPathException, DesignationNotFoundException, UnsupportedDataTypeException {

        Converter converter = new Converter();
        String fileName = args[0];
        ArrayList<ObjectNode> renders = converter.convertCDEsToCEDAR(fileName);
        for (ObjectNode render: renders) {
            System.out.println(render);
        }
    }
}