package org.metadatacenter.nih.ingestor.converter;

import org.metadatacenter.artifacts.model.core.NumericType;

import java.util.Optional;

public non-sealed class NumberConstraints implements CDEConstraints {

    private Optional<Number> minValue;
    private Optional<Number> maxValue;
    private Optional<Integer> precision; // 0, 1, 2, 3, 4
    private NumericType numericType;

    public NumberConstraints(Optional<Number> minValue, Optional<Number> maxValue, Optional<Integer> precision) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.precision = precision;
        numericType = precision.isPresent() ? NumericType.DOUBLE : NumericType.INTEGER;
    }

    public NumericType getNumericType() {
        return numericType;
    }

    public boolean hasPrecision() {
        return precision.isPresent();
    }

    public Number getPrecision() {
        return precision.get();
    }

    public boolean hasMinValue() {
        return minValue.isPresent();
    }

    public Number getMinValue() {
        return minValue.get();
    }

    public boolean hasMaxValue() {
        return maxValue.isPresent();
    }

    public Number getMaxValue() {
        return maxValue.get();
    }


    public static class NumberConstraintsBuilder {
        private Optional<Number> minValue = Optional.empty();
        private Optional<Number> maxValue = Optional.empty();
        private Optional<Integer> precision = Optional.empty(); // decimalPlaces

        public NumberConstraintsBuilder withMinValue(Integer minValue) {
            this.minValue = Optional.ofNullable(minValue);
            return this;
        }

        public NumberConstraintsBuilder withMaxValue(Integer maxValue) {
            this.maxValue = Optional.ofNullable(maxValue);
            return this;
        }

        public NumberConstraintsBuilder withPrecision(Integer precision) {
            this.precision = Optional.ofNullable(precision);
            return this;
        }

        public NumberConstraints build() {
            return new NumberConstraints(minValue, maxValue, precision);
        }
    }
}
