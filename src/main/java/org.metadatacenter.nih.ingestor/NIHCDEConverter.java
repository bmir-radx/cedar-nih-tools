package org.metadatacenter.nih.ingestor;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.nih.ingestor.converter.Converter;
import org.metadatacenter.nih.ingestor.poster.Poster;
import org.metadatacenter.nih.ingestor.poster.RetryablePoster;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "NIHCDEConverter",
        description = "Convert NIH CDEs to CEDAR format"
)
public class NIHCDEConverter implements Callable<Integer> {

    @CommandLine.Command(name = "validate", description = "Convert NIH CDEs to CEDAR format and validate against CEDAR API")
    public void validate(
            @CommandLine.Option(names = {"-f", "--file"}, description = "the json file containing NIH CDEs")
            String filename,
            @CommandLine.Option(names = {"-a", "--apiKey"}, description = "CEDAR API key")
            String apiKey
    ) throws Throwable {
        Converter converter = new Converter();
        Poster poster = new Poster(apiKey);
        RetryablePoster retryablePoster = new RetryablePoster(poster, "validate");

        ArrayList<ObjectNode> cedarCDEs = converter.convertCDEsToCEDAR(filename);
        for (ObjectNode cde : cedarCDEs) {
            System.out.println(cde);
            retryablePoster.validate(cde);
        }
    }

    @CommandLine.Command(name = "put", description = "Convert NIH CDEs to CEDAR format and create field entries for them in CEDAR")
    public void put(
            @CommandLine.Option(names = {"-f", "--file"}, description = "the json file containing NIH CDEs")
            String filename,
            @CommandLine.Option(names = {"-a", "--apiKey"}, description = "CEDAR API key")
            String apiKey,
            @CommandLine.Option(names = {"-t", "--targetFolder"}, description = "CEDAR Folder ID for creating CEDAR data elements")
            String targetFolder
    ) throws Throwable {
        Converter converter = new Converter();
        Poster poster = new Poster(apiKey);
        RetryablePoster retryablePoster = new RetryablePoster(poster, "validate");

        ArrayList<ObjectNode> cedarCDEs = converter.convertCDEsToCEDAR(filename);
        for (ObjectNode cde : cedarCDEs) {
            System.out.println(cde);
            retryablePoster.put(cde, targetFolder);
        }
    }

    @Override
    public Integer call() {
        return 0;
    }

    public static void main( String[] args ) {
        int exitCode = new CommandLine(new NIHCDEConverter()).execute(args);
        System.exit(exitCode);
    }
}