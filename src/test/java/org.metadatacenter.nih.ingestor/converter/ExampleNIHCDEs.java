package org.metadatacenter.nih.ingestor.converter;

public class ExampleNIHCDEs {

    public final static String textCDE_NIH = """
            {
              "stewardOrg": {
                "name": "Project 5 (COVID-19)"
              },
              "createdBy": {
                "username": "NIH CDE_NIH Repository Team"
              },
              "property": {
                "concepts": []
              },
              "dataElementConcept": {
                "concepts": [
                  {
                    "name": "Address",
                    "origin": "NCI Thesaurus",
                    "originId": "C25407"
                  },
                  {
                    "name": "City",
                    "origin": "NCI Thesaurus",
                    "originId": "C25160"
                  },
                  {
                    "name": "Name",
                    "origin": "NCI Thesaurus",
                    "originId": "C42614"
                  }
                ]
              },
              "objectClass": {
                "concepts": []
              },
              "valueDomain": {
                "identifiers": [],
                "ids": [],
                "datatype": "Text",
                "permissibleValues": []
              },
              "partOfBundles": [],
              "elementType": "cde",
              "nihEndorsed": true,
              "tinyId": "aQO0VmltMn",
              "designations": [
                {
                  "designation": "Address City Name",
                  "tags": []
                },
                {
                  "designation": "City",
                  "tags": [
                    "Preferred Question Text"
                  ]
                }
              ],
              "definitions": [
                {
                  "definition": "The city or township for the address to describe where a mail piece is intended to be delivered.",
                  "tags": []
                }
              ],
              "sources": [
                {
                  "sourceName": "Project 5 (COVID-19)",
                  "imported": "2022-03-29T15:13:59.754Z"
                }
              ],
              "sourcesNew": {},
              "created": "2022-03-29T15:13:59.754Z",
              "imported": "2022-03-29T15:13:59.754Z",
              "changeNote": "load Project5 on 29 March 2022",
              "registrationState": {
                "registrationStatus": "Qualified",
                "administrativeStatus": "Org Approve"
              },
              "classification": [
                {
                  "stewardOrg": {
                    "name": "Project 5 (COVID-19)"
                  },
                  "elements": [
                    {
                      "elements": [],
                      "name": "Tier 1"
                    }
                  ]
                }
              ],
              "properties": [
                {
                  "key": "Tags/Keywords",
                  "value": "Person, Demographics, Address, SDOH Geographic Location",
                  "source": "Project 5 (COVID-19)"
                },
                {
                  "key": "Project 5 (COVID-19) Source",
                  "value": "Project 5 WG2",
                  "source": "Project 5 (COVID-19)"
                }
              ],
              "ids": [],
              "attachments": [],
              "history": [],
              "archived": false,
              "views": 570,
              "referenceDocuments": [],
              "dataSets": [],
              "derivationRules": [],
              "__v": 0,
              "cdeTinyIds": [],
              "steward": "Project 5 (COVID-19)",
              "classificationSize": 1,
              "noRenderAllowed": false
            }
            """;

    public final static String numberCDE_NIH = """
            {
              "stewardOrg": {
                "name": "Project 5 (COVID-19)"
              },
              "createdBy": {
                "username": "NIH CDE_NIH Repository Team"
              },
              "property": {
                "concepts": []
              },
              "dataElementConcept": {
                "concepts": [
                  {
                    "name": "Person",
                    "origin": "NCI Thesaurus",
                    "originId": "C25190"
                  },
                  {
                    "name": "Age",
                    "origin": "NCI Thesaurus",
                    "originId": "C25150"
                  }
                ]
              },
              "objectClass": {
                "concepts": []
              },
              "valueDomain": {
                "identifiers": [],
                "ids": [],
                "datatype": "Number",
                "permissibleValues": []
              },
              "elementType": "cde",
              "nihEndorsed": true,
              "tinyId": "fmMMaUGpKS",
              "designations": [
                {
                  "designation": "Age",
                  "tags": []
                },
                {
                  "designation": "What is the person's age?",
                  "tags": [
                    "Preferred Question Text"
                  ]
                }
              ],
              "definitions": [
                {
                  "definition": "The length of time that one has existed in completed years, months and days at the time of the event",
                  "tags": []
                }
              ],
              "sources": [
                {
                  "sourceName": "Project 5 (COVID-19)",
                  "imported": "2022-03-29T15:13:59.754Z"
                }
              ],
              "sourcesNew": {},
              "created": "2022-03-29T15:13:59.754Z",
              "imported": "2022-03-29T15:13:59.754Z",
              "changeNote": "load Project5 on 29 March 2022",
              "registrationState": {
                "registrationStatus": "Qualified",
                "administrativeStatus": "Org Approve"
              },
              "classification": [
                {
                  "stewardOrg": {
                    "name": "Project 5 (COVID-19)"
                  },
                  "elements": [
                    {
                      "elements": [],
                      "name": "Tier 1"
                    }
                  ]
                }
              ],
              "properties": [
                {
                  "key": "Tags/Keywords",
                  "value": "Person, Demographics",
                  "source": "Project 5 (COVID-19)"
                },
                {
                  "key": "Project 5 (COVID-19) Source",
                  "value": "Project 5 WG2",
                  "source": "Project 5 (COVID-19)"
                }
              ],
              "ids": [],
              "attachments": [],
              "history": [],
              "archived": false,
              "views": 954,
              "referenceDocuments": [],
              "dataSets": [],
              "derivationRules": [],
              "__v": 4352,
              "partOfBundles": [
                "jVnD4QVbi"
              ],
              "cdeTinyIds": [],
              "steward": "Project 5 (COVID-19)",
              "classificationSize": 1
            }
            """;

    public final static String dateCDE_NIH = """
            {
              "stewardOrg": {
                "name": "Project 5 (COVID-19)"
              },
              "createdBy": {
                "username": "NIH CDE_NIH Repository Team"
              },
              "property": {
                "concepts": []
              },
              "dataElementConcept": {
                "concepts": [
                  {
                    "name": "COVID-19 Infection",
                    "origin": "NCI Thesaurus",
                    "originId": "C171133"
                  },
                  {
                    "name": "Case",
                    "origin": "NCI Thesaurus",
                    "originId": "C49152"
                  },
                  {
                    "name": "Classification",
                    "origin": "NCI Thesaurus",
                    "originId": "C25161"
                  },
                  {
                    "name": "Date and Time",
                    "origin": "NCI Thesaurus",
                    "originId": "C37939"
                  }
                ]
              },
              "objectClass": {
                "concepts": []
              },
              "valueDomain": {
                "identifiers": [],
                "ids": [],
                "datatype": "Date",
                "permissibleValues": []
              },
              "elementType": "cde",
              "nihEndorsed": true,
              "tinyId": "__b0hlBmCJ",
              "designations": [
                {
                  "designation": "COVID-19 Case Classification Date",
                  "tags": []
                },
                {
                  "designation": "What is the date of this diagnosis classification/confirmation?",
                  "tags": [
                    "Preferred Question Text"
                  ]
                }
              ],
              "definitions": [
                {
                  "definition": "The date the diagnosis was confirmed.",
                  "tags": []
                }
              ],
              "sources": [
                {
                  "sourceName": "Project 5 (COVID-19)",
                  "imported": "2022-03-29T15:13:59.754Z"
                }
              ],
              "sourcesNew": {},
              "created": "2022-03-29T15:13:59.754Z",
              "imported": "2022-03-29T15:13:59.754Z",
              "changeNote": "load Project5 on 29 March 2022",
              "registrationState": {
                "registrationStatus": "Qualified",
                "administrativeStatus": "Org Approve"
              },
              "classification": [
                {
                  "stewardOrg": {
                    "name": "Project 5 (COVID-19)"
                  },
                  "elements": [
                    {
                      "elements": [],
                      "name": "Tier 1"
                    }
                  ]
                }
              ],
              "properties": [
                {
                  "key": "Tags/Keywords",
                  "value": "COVID Specific, Diagnosis, COVID-19 Diagnosis",
                  "source": "Project 5 (COVID-19)"
                },
                {
                  "key": "Project 5 (COVID-19) Source",
                  "value": "CDC COVID-\\n19 Form  https://www.cdc.gov/coronavirus/2019-ncov/downloads/pui-form.pdf",
                  "source": "Project 5 (COVID-19)"
                }
              ],
              "ids": [],
              "attachments": [],
              "history": [],
              "archived": false,
              "views": 12,
              "referenceDocuments": [],
              "dataSets": [],
              "derivationRules": [],
              "__v": 4352,
              "partOfBundles": [
                "RFMQ6dEbz"
              ],
              "cdeTinyIds": [],
              "steward": "Project 5 (COVID-19)",
              "classificationSize": 1
            }
            """;

    public final static String timeCDE_NIH = """
            {
              "stewardOrg": {
                "name": "NCI"
              },
              "createdBy": {
                "username": "NIH CDE_NIH Repository Team"
              },
              "updatedBy": {
                "username": "NIH CDE_NIH Repository Team"
              },
              "property": {
                "concepts": [
                  {
                    "name": "Administered",
                    "origin": "NCI Thesaurus",
                    "originId": "C25382"
                  }
                ]
              },
              "dataElementConcept": {
                "concepts": [
                  {
                    "name": "Agent Administered",
                    "origin": "NCI caDSR",
                    "originId": "2013308v3"
                  }
                ]
              },
              "objectClass": {
                "concepts": [
                  {
                    "name": "Agent",
                    "origin": "NCI Thesaurus",
                    "originId": "C1708"
                  }
                ]
              },
              "valueDomain": {
                "definition": "The continuum of experience in which events pass from the future through the present to the past.",
                "name": "Time",
                "datatype": "Time",
                "permissibleValues": [],
                "ids": [],
                "identifiers": []
              },
              "partOfBundles": [],
              "nihEndorsed": false,
              "created": "2015-05-20T21:33:43.774Z",
              "tinyId": "mJzeIE1Je",
              "imported": "2017-01-31T19:21:46.306Z",
              "source": "caDSR",
              "version": "1",
              "origin": "DCP:Division of Cancer Prevention",
              "__v": 3,
              "changeNote": "Modified Tag from Health to Long Name",
              "lastMigrationScript": "fixDataElement",
              "derivationRules": [],
              "referenceDocuments": [],
              "attachments": [
                {
                  "uploadedBy": {
                    "username": "batchloader"
                  },
                  "fileid": "5876661699b5953c14eb22ac",
                  "filename": "2683505v1.xml",
                  "filetype": "application/xml",
                  "uploadDate": "2017-01-11T17:06:30.379Z",
                  "comment": "Original XML File",
                  "filesize": 13918
                }
              ],
              "comments": [],
              "dataSets": [],
              "ids": [
                {
                  "source": "caDSR",
                  "id": "2683505",
                  "version": "1"
                }
              ],
              "properties": [
                {
                  "key": "caDSR_Context",
                  "source": "caDSR",
                  "value": "DCP"
                },
                {
                  "key": "caDSR_Short_Name",
                  "source": "caDSR",
                  "value": "AGT_ADM_TM"
                },
                {
                  "key": "COG Form Element",
                  "value": "AgtAdmrPdrTm"
                }
              ],
              "classification": [
                {
                  "stewardOrg": {
                    "name": "NCI"
                  },
                  "elements": [
                    {
                      "name": "BBRB - BPV",
                      "elements": [
                        {
                          "name": "BPV - Tumor Biospecimen Acquisition",
                          "elements": []
                        }
                      ]
                    }
                  ]
                }
              ],
              "registrationState": {
                "registrationStatus": "Qualified",
                "administrativeStatus": "Published"
              },
              "history": [],
              "sources": [
                {
                  "sourceName": "caDSR",
                  "created": "2007-09-21T04:00:00.000Z",
                  "updated": "2016-09-16T04:00:00.000Z",
                  "datatype": "TIME",
                  "registrationStatus": "Qualified",
                  "administrativeStatus": "RELEASED"
                }
              ],
              "archived": false,
              "definitions": [
                {
                  "definition": "The time at which the agent was administered.",
                  "tags": [
                    "Long Name"
                  ]
                },
                {
                  "definition": "Agent Administration Time",
                  "tags": [
                    "Preferred Question Text"
                  ]
                },
                {
                  "definition": "CRF TEXT",
                  "tags": [
                    "Alternate Question Text"
                  ]
                },
                {
                  "definition": "Time of Drug Administration",
                  "tags": [
                    "Alternate Question Text"
                  ]
                },
                {
                  "definition": "PBTC_Text1",
                  "tags": [
                    "Alternate Question Text"
                  ]
                },
                {
                  "definition": "DCP Text 1",
                  "tags": [
                    "Alternate Question Text"
                  ]
                },
                {
                  "definition": "PBTC_Text2",
                  "tags": [
                    "Alternate Question Text"
                  ]
                },
                {
                  "definition": "PBTC_Text3",
                  "tags": [
                    "Alternate Question Text"
                  ]
                },
                {
                  "definition": "PBTC_Text4",
                  "tags": [
                    "Alternate Question Text"
                  ]
                }
              ],
              "designations": [
                {
                  "tags": [
                    "Long Name"
                  ],
                  "designation": "Agent Administered Time"
                },
                {
                  "tags": [
                    "Preferred Question Text"
                  ],
                  "designation": "Agent Administration Time"
                },
                {
                  "tags": [
                    "Alternate Question Text"
                  ],
                  "designation": "Time agent administered:"
                },
                {
                  "tags": [
                    "Alternate Question Text"
                  ],
                  "designation": "Time of Drug Administration"
                },
                {
                  "tags": [
                    "Alternate Question Text"
                  ],
                  "designation": "Time of PD-0332991 administration"
                },
                {
                  "tags": [
                    "Alternate Question Text"
                  ],
                  "designation": "Time"
                },
                {
                  "tags": [
                    "Alternate Question Text"
                  ],
                  "designation": "Time of buparlisib administration"
                },
                {
                  "tags": [
                    "Alternate Question Text"
                  ],
                  "designation": "Time of pomalidomide administration"
                },
                {
                  "tags": [
                    "Alternate Question Text"
                  ],
                  "designation": "Time of Panobinostat administration"
                }
              ],
              "elementType": "cde",
              "views": 3,
              "NIH_Endorsed": false,
              "sourcesNew": {},
              "cdeTinyIds": [],
              "steward": "NCI",
              "classificationSize": 1
            }
            """;

    public final static String fileCDE_NIH = """
            {
              "stewardOrg": {
                "name": "NINDS"
              },
              "createdBy": {
                "username": "NIH CDE_NIH Repository Team"
              },
              "updatedBy": {
                "username": "NIH CDE_NIH Repository Team"
              },
              "property": {
                "concepts": []
              },
              "dataElementConcept": {
                "concepts": []
              },
              "objectClass": {
                "concepts": []
              },
              "valueDomain": {
                "identifiers": [],
                "ids": [],
                "datatype": "File",
                "permissibleValues": []
              },
              "partOfBundles": [],
              "nihEndorsed": false,
              "elementType": "cde",
              "history": [],
              "archived": false,
              "views": 12,
              "registrationState": {
                "registrationStatus": "Qualified",
                "administrativeStatus": "Published"
              },
              "tinyId": "Xy0pqXcR9X",
              "created": "2017-05-22T14:20:49.398Z",
              "imported": "2021-04-13T00:09:34.880Z",
              "version": "1.1",
              "__v": 2,
              "derivationRules": [],
              "referenceDocuments": [
                {
                  "source": "NINDS",
                  "document": "Corrigan JD, Hinkeldey MS. Relationships between parts A and B of the Trail Making Test. J Clin Psychol. 1987;43(4):402&#8211;409."
                }
              ],
              "attachments": [],
              "dataSets": [],
              "ids": [
                {
                  "source": "NINDS",
                  "id": "C54097",
                  "version": "1"
                },
                {
                  "source": "BRICS Variable Name",
                  "id": "DKEFSTMTPartAImageSource"
                }
              ],
              "properties": [],
              "classification": [
                {
                  "stewardOrg": {
                    "name": "NINDS"
                  },
                  "elements": [
                    {
                      "elements": [
                        {
                          "name": "Huntington\\u2019s Disease",
                          "elements": [
                            {
                              "name": "Classification",
                              "elements": [
                                {
                                  "name": "Supplemental-Highly Recommended",
                                  "elements": []
                                }
                              ]
                            },
                            {
                              "name": "Domain",
                              "elements": [
                                {
                                  "name": "Outcomes and End Points",
                                  "elements": [
                                    {
                                      "name": "Cognitive",
                                      "elements": []
                                    }
                                  ]
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "name": "Sport Related Concussion",
                          "elements": [
                            {
                              "name": "Acute",
                              "elements": [
                                {
                                  "name": "Classification",
                                  "elements": [
                                    {
                                      "name": "Supplemental-Highly Recommended",
                                      "elements": []
                                    }
                                  ]
                                },
                                {
                                  "name": "Domain",
                                  "elements": [
                                    {
                                      "name": "Outcomes and End Points",
                                      "elements": [
                                        {
                                          "name": "Cognitive Assessment",
                                          "elements": []
                                        }
                                      ]
                                    }
                                  ]
                                }
                              ]
                            },
                            {
                              "name": "Comprehensive",
                              "elements": [
                                {
                                  "name": "Classification",
                                  "elements": [
                                    {
                                      "name": "Supplemental-Highly Recommended",
                                      "elements": []
                                    }
                                  ]
                                },
                                {
                                  "name": "Domain",
                                  "elements": [
                                    {
                                      "name": "Outcomes and End Points",
                                      "elements": [
                                        {
                                          "name": "Cognitive Assessment",
                                          "elements": []
                                        }
                                      ]
                                    }
                                  ]
                                }
                              ]
                            },
                            {
                              "name": "Persistent/Chronic",
                              "elements": [
                                {
                                  "name": "Classification",
                                  "elements": [
                                    {
                                      "name": "Supplemental-Highly Recommended",
                                      "elements": []
                                    }
                                  ]
                                },
                                {
                                  "name": "Domain",
                                  "elements": [
                                    {
                                      "name": "Outcomes and End Points",
                                      "elements": [
                                        {
                                          "name": "Cognitive Assessment",
                                          "elements": []
                                        }
                                      ]
                                    }
                                  ]
                                }
                              ]
                            },
                            {
                              "name": "Subacute",
                              "elements": [
                                {
                                  "name": "Classification",
                                  "elements": [
                                    {
                                      "name": "Supplemental-Highly Recommended",
                                      "elements": []
                                    }
                                  ]
                                },
                                {
                                  "name": "Domain",
                                  "elements": [
                                    {
                                      "name": "Outcomes and End Points",
                                      "elements": [
                                        {
                                          "name": "Cognitive Assessment",
                                          "elements": []
                                        }
                                      ]
                                    }
                                  ]
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "name": "Traumatic Brain Injury",
                          "elements": [
                            {
                              "name": "Acute Hospitalized",
                              "elements": [
                                {
                                  "name": "Classification",
                                  "elements": [
                                    {
                                      "name": "Supplemental",
                                      "elements": []
                                    }
                                  ]
                                },
                                {
                                  "name": "Domain",
                                  "elements": [
                                    {
                                      "name": "Outcomes and End Points",
                                      "elements": [
                                        {
                                          "name": "Neuropsychological Impairment",
                                          "elements": []
                                        }
                                      ]
                                    }
                                  ]
                                }
                              ]
                            },
                            {
                              "name": "Comprehensive",
                              "elements": [
                                {
                                  "name": "Classification",
                                  "elements": [
                                    {
                                      "name": "Supplemental",
                                      "elements": []
                                    }
                                  ]
                                },
                                {
                                  "name": "Domain",
                                  "elements": [
                                    {
                                      "name": "Outcomes and End Points",
                                      "elements": [
                                        {
                                          "name": "Neuropsychological Impairment",
                                          "elements": []
                                        }
                                      ]
                                    }
                                  ]
                                }
                              ]
                            },
                            {
                              "name": "Concussion/Mild TBI",
                              "elements": [
                                {
                                  "name": "Classification",
                                  "elements": [
                                    {
                                      "name": "Supplemental",
                                      "elements": []
                                    }
                                  ]
                                },
                                {
                                  "name": "Domain",
                                  "elements": [
                                    {
                                      "name": "Outcomes and End Points",
                                      "elements": [
                                        {
                                          "name": "Neuropsychological Impairment",
                                          "elements": []
                                        }
                                      ]
                                    }
                                  ]
                                }
                              ]
                            },
                            {
                              "name": "Epidemiology",
                              "elements": [
                                {
                                  "name": "Classification",
                                  "elements": [
                                    {
                                      "name": "Supplemental",
                                      "elements": []
                                    }
                                  ]
                                },
                                {
                                  "name": "Domain",
                                  "elements": [
                                    {
                                      "name": "Outcomes and End Points",
                                      "elements": [
                                        {
                                          "name": "Neuropsychological Impairment",
                                          "elements": []
                                        }
                                      ]
                                    }
                                  ]
                                }
                              ]
                            },
                            {
                              "name": "Moderate/Severe TBI: Rehabilitation",
                              "elements": [
                                {
                                  "name": "Classification",
                                  "elements": [
                                    {
                                      "name": "Supplemental",
                                      "elements": []
                                    }
                                  ]
                                },
                                {
                                  "name": "Domain",
                                  "elements": [
                                    {
                                      "name": "Outcomes and End Points",
                                      "elements": [
                                        {
                                          "name": "Neuropsychological Impairment",
                                          "elements": []
                                        }
                                      ]
                                    }
                                  ]
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "name": "Unruptured Cerebral Aneurysms and Subarachnoid Hemorrhage",
                          "elements": [
                            {
                              "name": "Classification",
                              "elements": [
                                {
                                  "name": "Exploratory",
                                  "elements": []
                                }
                              ]
                            },
                            {
                              "name": "Domain",
                              "elements": [
                                {
                                  "name": "Outcomes and End Points",
                                  "elements": [
                                    {
                                      "name": "Cognitive Impairment: Cognitive Speed",
                                      "elements": []
                                    },
                                    {
                                      "name": "Cognitive Impairment: Executive Functions",
                                      "elements": []
                                    }
                                  ]
                                }
                              ]
                            }
                          ]
                        }
                      ],
                      "name": "Disease"
                    },
                    {
                      "elements": [
                        {
                          "name": "Outcomes and End Points",
                          "elements": [
                            {
                              "name": "Cognitive",
                              "elements": []
                            },
                            {
                              "name": "Cognitive Assessment",
                              "elements": []
                            },
                            {
                              "name": "Cognitive Impairment: Cognitive Speed",
                              "elements": []
                            },
                            {
                              "name": "Cognitive Impairment: Executive Functions",
                              "elements": []
                            },
                            {
                              "name": "Neuropsychological Impairment",
                              "elements": []
                            }
                          ]
                        }
                      ],
                      "name": "Domain"
                    },
                    {
                      "elements": [
                        {
                          "name": "Adult",
                          "elements": []
                        }
                      ],
                      "name": "Population"
                    }
                  ]
                }
              ],
              "sources": [
                {
                  "imported": "2019-12-27T15:39:49.926Z",
                  "sourceName": "NINDS",
                  "updated": "2017-02-13T05:00:00.000Z",
                  "datatype": "File"
                }
              ],
              "definitions": [
                {
                  "tags": [],
                  "definition": "Source of Part A image used, as part of the Delis-Kaplan Executive Function System (D-KEFS) Trail-Making Test (TMT)"
                }
              ],
              "designations": [
                {
                  "tags": [],
                  "designation": "Trail Making Test (TMT)- Part A image used source"
                }
              ],
              "lastMigrationScript": "load NINDS on 12 April 2021",
              "changeNote": "load NINDS on 27 December 2019",
              "sourcesNew": {},
              "NIH_Endorsed": false,
              "cdeTinyIds": [],
              "steward": "NINDS",
              "classificationSize": 1,
              "noRenderAllowed": false
            }
            """;

    public final static String externallyDefinedCDE_NIH = """
            {
              "stewardOrg": {
                "name": "GRDR"
              },
              "createdBy": {
                "username": "NIH CDE_NIH Repository Team"
              },
              "property": {
                "concepts": []
              },
              "dataElementConcept": {
                "concepts": []
              },
              "objectClass": {
                "concepts": []
              },
              "valueDomain": {
                "definition": "Yes\\nNo\\nRefused\\nDon\\u2019t know",
                "datatype": "Externally Defined",
                "permissibleValues": [],
                "identifiers": [],
                "ids": []
              },
              "partOfBundles": [],
              "nihEndorsed": false,
              "tinyId": "5fMmMU7v81C",
              "imported": "2015-05-07T17:43:55.179Z",
              "source": "GRDR",
              "version": "1",
              "registrationState": {
                "registrationStatus": "Qualified",
                "administrativeStatus": "Published"
              },
              "ids": [
                {
                  "source": "GRDR",
                  "id": "GRDR011"
                }
              ],
              "classification": [
                {
                  "stewardOrg": {
                    "name": "GRDR"
                  },
                  "elements": [
                    {
                      "name": "Requirement",
                      "elements": [
                        {
                          "name": "Required"
                        }
                      ]
                    },
                    {
                      "name": "Category",
                      "elements": [
                        {
                          "name": "Socio-Demographic"
                        }
                      ]
                    }
                  ]
                }
              ],
              "attachments": [],
              "comments": [],
              "dataSets": [],
              "derivationRules": [],
              "history": [],
              "properties": [],
              "referenceDocuments": [],
              "sources": [
                {
                  "sourceName": "GRDR"
                }
              ],
              "__v": 6,
              "lastMigrationScript": "fixDataElement",
              "archived": false,
              "definitions": [
                {
                  "definition": "Yes\\nNo\\nRefused\\nDon\\u2019t know",
                  "tags": [
                    "Health"
                  ]
                }
              ],
              "designations": [
                {
                  "tags": [
                    "Question"
                  ],
                  "designation": "Vital Status"
                },
                {
                  "tags": [
                    "Health"
                  ],
                  "designation": "Has the participant died?"
                }
              ],
              "elementType": "cde",
              "views": 3,
              "NIH_Endorsed": false,
              "sourcesNew": {},
              "cdeTinyIds": [],
              "steward": "GRDR",
              "classificationSize": 1,
              "noRenderAllowed": false
            },
            {
              "stewardOrg": {
                "name": "NINDS"
              },
              "createdBy": {
                "username": "NIH CDE_NIH Repository Team"
              },
              "updatedBy": {
                "username": "NIH CDE_NIH Repository Team"
              },
              "property": {
                "concepts": []
              },
              "dataElementConcept": {
                "concepts": []
              },
              "objectClass": {
                "concepts": []
              },
              "valueDomain": {
                "identifiers": [],
                "ids": [],
                "datatype": "Date",
                "permissibleValues": []
              },
              "partOfBundles": [],
              "nihEndorsed": false,
              "elementType": "cde",
              "sourcesNew": {},
              "history": [],
              "archived": false,
              "views": 3,
              "registrationState": {
                "registrationStatus": "Qualified",
                "administrativeStatus": "Published"
              },
              "tinyId": "abmAnDXu5my",
              "imported": "2021-04-13T00:09:34.880Z",
              "source": "NINDS",
              "version": "3.1",
              "changeNote": "load NINDS on 12 April 2021",
              "__v": 0,
              "lastMigrationScript": "load NINDS on 12 April 2021",
              "derivationRules": [],
              "referenceDocuments": [
                {
                  "source": "NINDS",
                  "document": "Karlsson AK, Krassioukov A, Alexander MS, Donovan W, Biering-S&Oslash;rensen F. International Spinal Cord Injury Skin and Thermoregulation Function Basic Data Set. Spinal Cord. 2012; 50(7):512-6."
                },
                {
                  "source": "NINDS",
                  "document": "Karlsson AK, Krassioukov A, Alexander MS, Donovan W, Biering-S&Oslash;rensen F. International Spinal Cord Injury Skin and Thermoregulation Function Basic Data Set. Spinal Cord. 2012 Jan 31. [Epub ahead of print]"
                }
              ],
              "attachments": [],
              "dataSets": [],
              "ids": [
                {
                  "source": "NINDS caDSR",
                  "id": "2829808"
                },
                {
                  "source": "NINDS",
                  "id": "C01519",
                  "version": "3"
                },
                {
                  "source": "BRICS Variable Name",
                  "id": "VitalSgnDateTime"
                }
              ],
              "properties": [],
              "classification": [
                {
                  "stewardOrg": {
                    "name": "NINDS"
                  },
                  "elements": [
                    {
                      "elements": [
                        {
                          "name": "Amyotrophic Lateral Sclerosis",
                          "elements": [
                            {
                              "name": "Classification",
                              "elements": [
                                {
                                  "name": "Core",
                                  "elements": []
                                }
                              ]
                            },
                            {
                              "name": "Domain",
                              "elements": [
                                {
                                  "name": "Assessments and Examinations",
                                  "elements": [
                                    {
                                      "name": "Vital Signs and Other Body Measures",
                                      "elements": []
                                    }
                                  ]
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "name": "Duchenne/Becker Muscular Dystrophy",
                          "elements": [
                            {
                              "name": "Classification",
                              "elements": [
                                {
                                  "name": "Supplemental-Highly Recommended",
                                  "elements": []
                                }
                              ]
                            },
                            {
                              "name": "Domain",
                              "elements": [
                                {
                                  "name": "Assessments and Examinations",
                                  "elements": [
                                    {
                                      "name": "Vital Signs and Other Body Measures",
                                      "elements": []
                                    }
                                  ]
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "name": "Epilepsy",
                          "elements": [
                            {
                              "name": "Classification",
                              "elements": [
                                {
                                  "name": "Supplemental",
                                  "elements": []
                                }
                              ]
                            },
                            {
                              "name": "Domain",
                              "elements": [
                                {
                                  "name": "Assessments and Examinations",
                                  "elements": [
                                    {
                                      "name": "Vital Signs and Other Body Measures",
                                      "elements": []
                                    }
                                  ]
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "name": "Friedreich's Ataxia",
                          "elements": [
                            {
                              "name": "Classification",
                              "elements": [
                                {
                                  "name": "Core",
                                  "elements": []
                                }
                              ]
                            },
                            {
                              "name": "Domain",
                              "elements": [
                                {
                                  "name": "Assessments and Examinations",
                                  "elements": [
                                    {
                                      "name": "Vital Signs and Other Body Measures",
                                      "elements": []
                                    }
                                  ]
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "name": "General (For all diseases)",
                          "elements": [
                            {
                              "name": "Classification",
                              "elements": [
                                {
                                  "name": "Supplemental",
                                  "elements": []
                                }
                              ]
                            },
                            {
                              "name": "Domain",
                              "elements": [
                                {
                                  "name": "Assessments and Examinations",
                                  "elements": [
                                    {
                                      "name": "Vital Signs and Other Body Measures",
                                      "elements": []
                                    }
                                  ]
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "name": "Headache",
                          "elements": [
                            {
                              "name": "Classification",
                              "elements": [
                                {
                                  "name": "Core",
                                  "elements": []
                                }
                              ]
                            },
                            {
                              "name": "Domain",
                              "elements": [
                                {
                                  "name": "Assessments and Examinations",
                                  "elements": [
                                    {
                                      "name": "Vital Signs and Other Body Measures",
                                      "elements": []
                                    }
                                  ]
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "name": "Mitochondrial Disease",
                          "elements": [
                            {
                              "name": "Classification",
                              "elements": [
                                {
                                  "name": "Supplemental",
                                  "elements": []
                                }
                              ]
                            },
                            {
                              "name": "Domain",
                              "elements": [
                                {
                                  "name": "Assessments and Examinations",
                                  "elements": [
                                    {
                                      "name": "Vital Signs and Other Body Measures",
                                      "elements": []
                                    }
                                  ]
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "name": "Multiple Sclerosis",
                          "elements": [
                            {
                              "name": "Classification",
                              "elements": [
                                {
                                  "name": "Supplemental",
                                  "elements": []
                                }
                              ]
                            },
                            {
                              "name": "Domain",
                              "elements": [
                                {
                                  "name": "Assessments and Examinations",
                                  "elements": [
                                    {
                                      "name": "Vital Signs and Other Body Measures",
                                      "elements": []
                                    }
                                  ]
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "name": "Myasthenia Gravis",
                          "elements": [
                            {
                              "name": "Classification",
                              "elements": [
                                {
                                  "name": "Core",
                                  "elements": []
                                }
                              ]
                            },
                            {
                              "name": "Domain",
                              "elements": [
                                {
                                  "name": "Assessments and Examinations",
                                  "elements": [
                                    {
                                      "name": "Vital Signs and Other Body Measures",
                                      "elements": []
                                    }
                                  ]
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "name": "Spinal Cord Injury",
                          "elements": [
                            {
                              "name": "Classification",
                              "elements": [
                                {
                                  "name": "Supplemental",
                                  "elements": []
                                }
                              ]
                            },
                            {
                              "name": "Domain",
                              "elements": [
                                {
                                  "name": "The International SCI Data Sets",
                                  "elements": [
                                    {
                                      "name": "The International SCI Data Sets",
                                      "elements": []
                                    }
                                  ]
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "name": "Spinal Muscular Atrophy",
                          "elements": [
                            {
                              "name": "Classification",
                              "elements": [
                                {
                                  "name": "Core",
                                  "elements": []
                                }
                              ]
                            },
                            {
                              "name": "Domain",
                              "elements": [
                                {
                                  "name": "Assessments and Examinations",
                                  "elements": [
                                    {
                                      "name": "Vital Signs and Other Body Measures",
                                      "elements": []
                                    }
                                  ]
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "name": "Stroke",
                          "elements": [
                            {
                              "name": "Classification",
                              "elements": [
                                {
                                  "name": "Core",
                                  "elements": []
                                }
                              ]
                            },
                            {
                              "name": "Domain",
                              "elements": [
                                {
                                  "name": "Assessments and Examinations",
                                  "elements": [
                                    {
                                      "name": "Vital Signs and Other Body Measures",
                                      "elements": []
                                    }
                                  ]
                                }
                              ]
                            }
                          ]
                        }
                      ],
                      "name": "Disease"
                    },
                    {
                      "elements": [
                        {
                          "name": "Assessments and Examinations",
                          "elements": [
                            {
                              "name": "Vital Signs and Other Body Measures",
                              "elements": []
                            }
                          ]
                        },
                        {
                          "name": "The International SCI Data Sets",
                          "elements": [
                            {
                              "name": "The International SCI Data Sets",
                              "elements": []
                            }
                          ]
                        }
                      ],
                      "name": "Domain"
                    },
                    {
                      "elements": [
                        {
                          "name": "Adult",
                          "elements": []
                        },
                        {
                          "name": "Pediatric",
                          "elements": []
                        }
                      ],
                      "name": "Population"
                    }
                  ]
                },
                {
                  "stewardOrg": {
                    "name": "NHLBI"
                  },
                  "elements": [
                    {
                      "elements": [
                        {
                          "name": "Domain",
                          "elements": [
                            {
                              "name": "Assessments and Examinations",
                              "elements": [
                                {
                                  "name": "Vital Signs and Other Body Measures",
                                  "elements": []
                                }
                              ]
                            }
                          ]
                        },
                        {
                          "name": "Classification",
                          "elements": [
                            {
                              "name": "Proposed",
                              "elements": []
                            }
                          ]
                        }
                      ],
                      "name": "Sickle Cell Disease"
                    }
                  ]
                }
              ],
              "sources": [
                {
                  "imported": "2019-12-27T15:39:49.926Z",
                  "sourceName": "NINDS",
                  "updated": "2013-08-28T20:08:00.453Z",
                  "datatype": "Date or Date & Time"
                }
              ],
              "definitions": [
                {
                  "tags": [],
                  "definition": "Date (and time, if applicable and known) the vital signs and other body measurements were taken by indicating the month, day, and year for the date and am, pm, or 24 hour clock for time"
                }
              ],
              "designations": [
                {
                  "tags": [],
                  "designation": "Vital signs date and time"
                }
              ],
              "NIH_Endorsed": false,
              "cdeTinyIds": [],
              "steward": "NINDS",
              "classificationSize": 2,
              "noRenderAllowed": false
            }
            """;

    public final static String valueListCDE_NIH = """
            {
              "stewardOrg": {
                "name": "Project 5 (COVID-19)"
              },
              "createdBy": {
                "username": "NIH CDE_NIH Repository Team"
              },
              "property": {
                "concepts": []
              },
              "dataElementConcept": {
                "concepts": [
                  {
                    "name": "Address",
                    "origin": "NCI Thesaurus",
                    "originId": "C25407"
                  },
                  {
                    "name": "State",
                    "origin": "NCI Thesaurus",
                    "originId": "C87194"
                  },
                  {
                    "name": "Code",
                    "origin": "NCI Thesaurus",
                    "originId": "C25162"
                  }
                ]
              },
              "objectClass": {
                "concepts": []
              },
              "valueDomain": {
                "identifiers": [],
                "ids": [],
                "datatype": "Value List",
                "permissibleValues": [
                  {
                    "permissibleValue": "AL",
                    "valueMeaningDefinition": "Alabama C43479",
                    "conceptId": "C43479",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "AK",
                    "valueMeaningDefinition": "Alaska C43506",
                    "conceptId": "C43506",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "AZ",
                    "valueMeaningDefinition": "Arizona C43505",
                    "conceptId": "C43505",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "AR",
                    "valueMeaningDefinition": "Arkansas C43495",
                    "conceptId": "C43495",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "CA",
                    "valueMeaningDefinition": "California C43509",
                    "conceptId": "C43509",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "CO",
                    "valueMeaningDefinition": "Colorado C43501",
                    "conceptId": "C43501",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "CT",
                    "valueMeaningDefinition": "Connecticut C43466",
                    "conceptId": "C43466",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "DC",
                    "valueMeaningDefinition": "Delaware C43969",
                    "conceptId": "C43969",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "DE",
                    "valueMeaningDefinition": "District of Columbia C108007",
                    "conceptId": "C108007",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "FL",
                    "valueMeaningDefinition": "Florida C43478",
                    "conceptId": "C43478",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "GA",
                    "valueMeaningDefinition": "Georgia C43477",
                    "conceptId": "C43477",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "HI",
                    "valueMeaningDefinition": "Hawaii C43510",
                    "conceptId": "C43510",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "ID",
                    "valueMeaningDefinition": "Idaho C43499",
                    "conceptId": "C43499",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "IL",
                    "valueMeaningDefinition": "Illinois C43491",
                    "conceptId": "C43491",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "IN",
                    "valueMeaningDefinition": "Indiana C43483",
                    "conceptId": "C43483",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "IA",
                    "valueMeaningDefinition": "Iowa C43487",
                    "conceptId": "C43487",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "KS",
                    "valueMeaningDefinition": "Kansas C43493",
                    "conceptId": "C43493",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "KY",
                    "valueMeaningDefinition": "Kentucky C43484",
                    "conceptId": "C43484",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "LA",
                    "valueMeaningDefinition": "Louisiana C43496",
                    "conceptId": "C43496",
                    "conceptSource": "NCI Thesaurus"
                  },
                  {
                    "permissibleValue": "ME",
                    "valueMeaningDefinition": "Maine C43457",
                    "conceptId": "C43457",
                    "conceptSource": "NCI Thesaurus"
                  }
                ]
              },
              "elementType": "cde",
              "nihEndorsed": true,
              "tinyId": "6AT_JFxD1",
              "designations": [
                {
                  "designation": "Address State Code",
                  "tags": []
                },
                {
                  "designation": "State",
                  "tags": [
                    "Preferred Question Text"
                  ]
                }
              ],
              "definitions": [
                {
                  "definition": "The state for the address to describe where a mail piece is intended to be delivered.",
                  "tags": []
                }
              ],
              "sources": [
                {
                  "sourceName": "Project 5 (COVID-19)",
                  "imported": "2022-03-29T15:13:59.754Z"
                }
              ],
              "sourcesNew": {},
              "created": "2022-03-29T15:13:59.754Z",
              "imported": "2022-03-29T15:13:59.754Z",
              "changeNote": "load Project5 on 29 March 2022",
              "registrationState": {
                "registrationStatus": "Qualified",
                "administrativeStatus": "Org Approve"
              },
              "classification": [
                {
                  "stewardOrg": {
                    "name": "Project 5 (COVID-19)"
                  },
                  "elements": [
                    {
                      "elements": [],
                      "name": "Tier 1"
                    }
                  ]
                }
              ],
              "properties": [
                {
                  "key": "Tags/Keywords",
                  "value": "Person, Demographics, Address, SDOH Geographic Location",
                  "source": "Project 5 (COVID-19)"
                },
                {
                  "key": "Project 5 (COVID-19) Source",
                  "value": "Project 5 WG2",
                  "source": "Project 5 (COVID-19)"
                }
              ],
              "ids": [],
              "attachments": [],
              "history": [],
              "archived": false,
              "views": 273,
              "referenceDocuments": [],
              "dataSets": [],
              "derivationRules": [],
              "__v": 1,
              "partOfBundles": [],
              "cdeTinyIds": [],
              "steward": "Project 5 (COVID-19)",
              "classificationSize": 1,
              "noRenderAllowed": false
            }
            """;
}
