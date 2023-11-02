# cedar-nih-tools

Ingest CDEs from the [NIH CDE repository](https://cde.nlm.nih.gov/cde/search).
To run this, export CDEs from the NIH repository in JSON format.
There is a button to export CDEs on the website.

After building the package (`mvn package`), run the tool from command line  to parse the CDEs, convert them to CEDAR format, and create Fields elements in CEDAR.

```
mvn exec:java -Dexec.args="<JSON file> <API Key> <CEDAR Folder ID>"
```