package org.metadatacenter.nih.ingestor.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class JsonDatePrecisions {
    public static final String MINUTE = "Minute";
    public static final String DAY = "Day";

    public static final Set<String> ALLOWEDDATEPRECISIONS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(DAY, MINUTE)));
}
