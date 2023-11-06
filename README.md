# cedar-nih-tools

Ingest CDEs from the [NIH CDE repository](https://cde.nlm.nih.gov/cde/search).
To run this, export CDEs from the NIH repository in JSON format.
There is a button to export CDEs on the website.

Build the package with the following command:
```
mvn clean compile assembly:single
```

This produces `./target/cedar-nih-tools-<VERSION>-SNAPSHOT-jar-with-dependencies.jar`.
Use this `.jar` to run the tool from command line.

To convert from NIH format to CEDAR and then validate against CEDAR's API, run:
```
java -cp <path/to/picocli/jar>:target/cedar-nih-tools-<VERSION>-SNAPSHOT-jar-with-dependencies.jar \
    validate \
    --file <JSON from NIH CDE repository> \
    --apiKey <API key from CEDAR> \
```

To convert from NIH format to CEDAR and then create field entries for the data elements in CEDAR, run:
```
java -cp <path/to/picocli/jar>:target/cedar-nih-tools-<VERSION>-SNAPSHOT-jar-with-dependencies.jar \
    put \
    --file <JSON from NIH CDE repository> \
    --apiKey <API key from CEDAR> \
    --targetFolder <CEDAR folderId>
```
After building the package (`mvn package`), run the tool from command line  to parse the CDEs, convert them to CEDAR format, and create Fields elements in CEDAR.

```
mvn exec:java -Dexec.args="<JSON file> <API Key> <CEDAR Folder ID>"
```

Example for cleaning out a folder, creating a set of fields in the folder, and then publishing all of the draft fields:
```
java -cp <path/to/picocli/jar>:target/cedar-nih-tools-<VERSION>-SNAPSHOT-jar-with-dependencies.jar \
    delete-folder \
    --targetFolder <CEDAR FolderId (URL)> \
    --apiKey <API key from CEDAR>
    
java -cp <path/to/picocli/jar>:target/cedar-nih-tools-<VERSION>-SNAPSHOT-jar-with-dependencies.jar \
    put \
    --targetFolder <CEDAR FolderId (URL)> \
    --apiKey <API key from CEDAR>
    --file <JSON from NIH CDE repository>
    
java -cp <path/to/picocli/jar>:target/cedar-nih-tools-<VERSION>-SNAPSHOT-jar-with-dependencies.jar \
    publish-folder \
    --targetFolder <CEDAR FolderId (URL)> \
    --apiKey <API key from CEDAR>
```

A single CEDAR field can also be specified for deletion or publishing:
```
java -cp <path/to/picocli/jar>:target/cedar-nih-tools-<VERSION>-SNAPSHOT-jar-with-dependencies.jar \
    publish \
    --fieldId <CEDAR Field ID (URL)> \
    --apiKey <API key from CEDAR>
    
java -cp <path/to/picocli/jar>:target/cedar-nih-tools-<VERSION>-SNAPSHOT-jar-with-dependencies.jar \
    delete \
    --fieldId <CEDAR Field ID (URL)> \
    --apiKey <API key from CEDAR>
```