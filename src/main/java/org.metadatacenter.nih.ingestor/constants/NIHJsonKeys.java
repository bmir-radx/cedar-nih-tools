package org.metadatacenter.nih.ingestor.constants;

/*
Constants for each of the Json fields in the NIH CDEs that
are relevant for CEDAR.
 */
public class NIHJsonKeys {
    // Constants for NIH CDE json fields
    public static final String VALUEDOMAIN = "valueDomain";
    public static final String DATATYPE = "datatype";
    public static final String TINYID = "tinyId";
    public static final String VERSION = "__v";
    public static final String DEFINITIONS = "definitions";
    public static final String DEFINITION = "definition";
    public static final String PERMISSIBLEVALUES = "permissibleValues";
    public static final String PERMISSIBLEVALUE = "permissibleValue";
    public static final String VALUEMEANINGNAME = "valueMeaningName";
    public static final String DESIGNATIONS = "designations";
    public static final String DESIGNATION = "designation";
    public static final String TAGS = "tags";
    public static final String PREFERREDQUESTIONTEXT = "\"Preferred Question Text\"";
    public static final String DATATYPETEXT = "datatypeText";
    public static final String DATATYPENUMBER = "datatypeNumber";
    public static final String DATATYPEDATE = "datatypeDate";
    public static final String PRECISION = "precision"; // for DATE (Minute/Day) and NUMBER (num decimal places)
    public static final String FORMAT = "format"; // for DATE
    public static final String MINLENGTH = "minLength";
    public static final String MAXLENGTH = "maxLength";
    public static final String MINVALUE = "minValue";
    public static final String MAXVALUE = "maxValue";
}
