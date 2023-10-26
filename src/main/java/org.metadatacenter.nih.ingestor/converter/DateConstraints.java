package org.metadatacenter.nih.ingestor.converter;

import java.util.Optional;

public non-sealed class DateConstraints implements CDEConstraints {

    private Optional<String> precision; // "Day" or "Minute"

    public DateConstraints(Optional<String> precision) {
        this.precision = precision;
    }

    public boolean hasPrecision() {
        return precision.isPresent();
    }

    public String getPrecision() {
        return precision.get();
    }

    public static class DateConstraintsBuilder {
        private Optional<String> precision = Optional.empty();

        public DateConstraintsBuilder withPrecision(String precision) {
            this.precision = Optional.ofNullable(precision);
            return this;
        }

        public DateConstraints build() {
            return new DateConstraints(precision);
        }
    }
}
