package org.metadatacenter.nih.ingestor;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.nih.ingestor.converter.Converter;
import org.metadatacenter.nih.ingestor.poster.HttpRequester;
import org.metadatacenter.nih.ingestor.poster.RequestURLPrefixes;
import org.metadatacenter.nih.ingestor.poster.RetryableRequester;
import org.metadatacenter.nih.ingestor.writer.JsonWriter;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "NIHCDEConverter",
        description = "Convert NIH CDEs to CEDAR format"
)
public class NIHCDEConverter implements Callable<Integer> {

    @CommandLine.Command(name = "convert", description = "Convert NIH CDEs to CEDAR format and save to file")
    public void convert(
            @CommandLine.Option(names = {"-f", "--file"}, description = "the json file containing NIH CDEs")
            String filename,
            @CommandLine.Option(names = {"-o", "--output"}, description = "the json output file containing NIH CDEs in CEDAR format")
            String output
    ) throws Throwable {
        Converter converter = new Converter();
        JsonWriter writer = new JsonWriter();

        ArrayList<ObjectNode> cedarCDEs = converter.convertCDEsToCEDAR(filename);
        writer.writeToFile(cedarCDEs, output);
    }

    @CommandLine.Command(name = "validate", description = "Convert NIH CDEs to CEDAR format and validate against CEDAR API")
    public void validate(
            @CommandLine.Option(names = {"-f", "--file"}, description = "the json file containing NIH CDEs")
            String filename,
            @CommandLine.Option(names = {"-a", "--apiKey"}, description = "CEDAR API key")
            String apiKey
    ) throws Throwable {
        Converter converter = new Converter();
        HttpRequester httpRequester = new HttpRequester(apiKey);
        RetryableRequester retryableRequester = new RetryableRequester(httpRequester, "validate");

        ArrayList<ObjectNode> cedarCDEs = converter.convertCDEsToCEDAR(filename);
        for (ObjectNode cde : cedarCDEs) {
            System.out.println(cde);
            retryableRequester.validate(cde);
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
        HttpRequester httpRequester = new HttpRequester(apiKey);
        RetryableRequester retryableRequester = new RetryableRequester(httpRequester, "validate");

        ArrayList<ObjectNode> cedarCDEs = converter.convertCDEsToCEDAR(filename);
        for (ObjectNode cde : cedarCDEs) {
            System.out.println(cde);
            retryableRequester.put(cde, targetFolder);
        }
    }

    @CommandLine.Command(name = "delete-folder", description = "Recursively delete fields and sub-folders and the folder itself given a folder ID.")
    public void deleteFolder(
            @CommandLine.Option(names = {"-a", "--apiKey"}, description = "CEDAR API key")
            String apiKey,
            @CommandLine.Option(names = {"-t", "--targetFolder"}, description = "CEDAR Folder ID to recursively search")
            String targetFolder
    ) throws Throwable {
        HttpRequester httpRequester = new HttpRequester(apiKey);
        // find all the things that need deletion
        ArrayList<String> folders = httpRequester.findFolders(targetFolder);
        System.out.println("Folders: " + folders);
        ArrayList<String> fields = httpRequester.findFields(folders, false);
        System.out.println("Fields: " + fields);
        System.out.println("Number of Fields: " + fields.size());
        // delete them all
        httpRequester.deleteArtifacts(RequestURLPrefixes.fieldURL, fields);
    }

    @CommandLine.Command(name = "delete", description = "Recursively delete a single field in CEDAR.")
    public void delete(
            @CommandLine.Option(names = {"-a", "--apiKey"}, description = "CEDAR API key")
            String apiKey,
            @CommandLine.Option(names = {"-i", "--fieldId"}, description = "CEDAR field to delete")
            String fieldId
    ) throws Throwable {
        HttpRequester httpRequester = new HttpRequester(apiKey);
        httpRequester.deleteSingleArtifact(RequestURLPrefixes.fieldURL, fieldId);
    }

    @CommandLine.Command(name = "publish-folder", description = "Publish all fields in a folder.")
    public void publishFolder(
            @CommandLine.Option(names = {"-a", "--apiKey"}, description = "CEDAR API key")
            String apiKey,
            @CommandLine.Option(names = {"-t", "--targetFolder"}, description = "CEDAR Folder ID to recursively search")
            String targetFolder
    ) throws Throwable {
        HttpRequester httpRequester = new HttpRequester(apiKey);
        // find all the things that need deletion
        ArrayList<String> folders = httpRequester.findFolders(targetFolder);
        ArrayList<String> fields = httpRequester.findFields(folders, true);
        System.out.println("Fields: " + fields);
        System.out.println("Number of Fields: " + fields.size());
        // delete them all
        RetryableRequester retryableRequester = new RetryableRequester(httpRequester, "publish");
        for (String field: fields) {
            retryableRequester.publish(field);
        }
    }

    @CommandLine.Command(name = "publish", description = "Publish a field.")
    public void publish(
            @CommandLine.Option(names = {"-a", "--apiKey"}, description = "CEDAR API key")
            String apiKey,
            @CommandLine.Option(names = {"-i", "--fieldId"}, description = "CEDAR field to publish")
            String fieldId
    ) throws Throwable {
        HttpRequester httpRequester = new HttpRequester(apiKey);
        RetryableRequester retryableRequester = new RetryableRequester(httpRequester, "publish");
        retryableRequester.publish(fieldId);
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