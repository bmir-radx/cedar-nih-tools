package org.metadatacenter.nih.ingestor.poster;

public class RequestURLPrefixes {
    public static int SEARCHPAGELIMIT = 500;
    public static final String validateURL = "https://resource.metadatacenter.org/command/validate?resource_type=field";
    public static final String putURL = "https://resource.metadatacenter.org/template-fields?folder_id=";
    public static final String publishURL = "https://resource.metadatacenter.org/command/publish-artifact/template-fields?folder_id=";
    public static final String fieldURL = "https://resource.metadatacenter.org/template-fields/";
    public static final String folderURL = "https://resource.metadatacenter.org/folders/";
    public static String createSearchURL(String folderId, String resourceType, int offset) {
        String prefix = "https://resource.metadatacenter.org/folders";
        String suffix = String.format(
                "contents?resource_types=%s&version=all&publication_status=all&limit=%d&offset=%d",
                resourceType, SEARCHPAGELIMIT, offset);
        return String.format("%s/%s/%s", prefix, folderId, suffix);
    }
}
