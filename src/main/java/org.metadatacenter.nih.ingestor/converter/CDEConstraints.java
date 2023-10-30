package org.metadatacenter.nih.ingestor.converter;

import org.metadatacenter.artifacts.model.core.NumericType;

import java.util.ArrayList;
import java.util.Optional;

public class CDEConstraints {

    private Optional<String> datePrecision; // "Day" or "Minute"
    private Optional<ArrayList<String>> permissibleValues;
    private Optional<Integer> minLength;
    private Optional<Integer> maxLength;
    private Optional<Number> minValue;
    private Optional<Number> maxValue;
    private Optional<Integer> numericPrecision; // 0, 1, 2, 3, 4
    private Optional<NumericType> numericType;

    public CDEConstraints(Optional<String> datePrecision,
                          Optional<ArrayList<String>> permissibleValues,
                          Optional<Integer> minLength,
                          Optional<Integer> maxLength,
                          Optional<Number> minValue,
                          Optional<Number> maxValue,
                          Optional<Integer> numericPrecision,
                          Optional<NumericType> numericType) {
        this.datePrecision = datePrecision;
        this.permissibleValues = permissibleValues;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.numericPrecision = numericPrecision;
        this.numericType = numericType;
    }

    public boolean hasDatePrecision() {
        return datePrecision.isPresent();
    }

    public String getDatePrecision() {
        return datePrecision.get();
    }

    public boolean hasPermissibleValues() {
        return permissibleValues.isPresent();
    }

    public ArrayList<String> getPermissibleValues() {
        return permissibleValues.get();
    }

    public boolean hasMinLength() {
        return minLength.isPresent();
    }

    public Integer getMinLength() {
        return minLength.get();
    }

    public boolean hasMaxLength() {
        return maxLength.isPresent();
    }

    public Integer getMaxLength() {
        return maxLength.get();
    }

    public boolean hasNumericType() {
        return numericType.isPresent();
    }

    public NumericType getNumericType() {
        return numericType.get();
    }

    public boolean hasNumericPrecision() {
        return numericPrecision.isPresent();
    }

    public Integer getNumericPrecision() {
        return numericPrecision.get();
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

    public static class CDEConstraintsBuilder {
        private Optional<String> datePrecision = Optional.empty();
        private Optional<ArrayList<String>> permissibleValues = Optional.empty();
        private Optional<Integer> minLength = Optional.empty();
        private Optional<Integer> maxLength = Optional.empty();
        private Optional<Number> minValue = Optional.empty();
        private Optional<Number> maxValue = Optional.empty();
        private Optional<Integer> numericPrecision = Optional.empty();

        public CDEConstraintsBuilder withDatePrecision(String datePrecision) {
            this.datePrecision = Optional.of(datePrecision);
            return this;
        }

        public CDEConstraintsBuilder withPermissibleValues(ArrayList<String> permissibleValues) {
            this.permissibleValues = Optional.of(permissibleValues);
            return this;
        }

        public CDEConstraintsBuilder withMinLength(Integer minLength) {
            this.minLength = Optional.of(minLength);
            return this;
        }

        public CDEConstraintsBuilder withMaxLength(Integer maxLength) {
            this.maxLength = Optional.of(maxLength);
            return this;
        }

        public CDEConstraintsBuilder withMinValue(Number minValue) {
            this.minValue = Optional.of(minValue);
            return this;
        }

        public CDEConstraintsBuilder withMaxValue(Number maxValue) {
            this.maxValue = Optional.of(maxValue);
            return this;
        }

        public CDEConstraintsBuilder withNumericPrecision(Integer numericPrecision) {
            this.numericPrecision = Optional.of(numericPrecision);
            return this;
        }

        public CDEConstraints build() {
            NumericType numericType = numericPrecision.isPresent() ?
                    NumericType.DECIMAL : NumericType.INTEGER;
            return new CDEConstraints(datePrecision, permissibleValues, minLength, maxLength,
                    minValue, maxValue, numericPrecision, Optional.of(numericType));
        }
    }
}