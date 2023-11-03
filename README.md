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
    --file <JSON from NIH CDE repository> \
    --apiKey <API key from CEDAR> \
    validate
```

To convert from NIH format to CEDAR and then create field entries for the data elements in CEDAR, run:
```
java -cp <path/to/picocli/jar>:target/cedar-nih-tools-<VERSION>-SNAPSHOT-jar-with-dependencies.jar \
    --file <JSON from NIH CDE repository> \
    --apiKey <API key from CEDAR> \
    put \
    --targetFolder <CEDAR folderId>
```
After building the package (`mvn package`), run the tool from command line  to parse the CDEs, convert them to CEDAR format, and create Fields elements in CEDAR.

```
mvn exec:java -Dexec.args="<JSON file> <API Key> <CEDAR Folder ID>"
```