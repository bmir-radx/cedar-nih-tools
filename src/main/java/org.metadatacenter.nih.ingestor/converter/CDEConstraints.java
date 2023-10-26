package org.metadatacenter.nih.ingestor.converter;

public sealed interface CDEConstraints permits TextConstraints, NumberConstraints,
        DateConstraints, ValueListConstraints {
}
