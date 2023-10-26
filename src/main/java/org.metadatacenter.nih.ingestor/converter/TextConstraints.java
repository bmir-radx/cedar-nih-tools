package org.metadatacenter.nih.ingestor.converter;

import java.util.Optional;

public non-sealed class TextConstraints implements CDEConstraints {

    private Optional<Integer> minLength;
    private Optional<Integer> maxLength;

    public TextConstraints(Optional<Integer> minLength, Optional<Integer> maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
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


    public static class TextConstraintsBuilder {
        private Optional<Integer> minLength;
        private Optional<Integer> maxLength;

        public TextConstraintsBuilder withMinLength(Integer minLength) {
            this.minLength = Optional.ofNullable(minLength);
            return this;
        }

        public TextConstraintsBuilder withMaxLength(Integer maxLength) {
            this.maxLength = Optional.ofNullable(maxLength);
            return this;
        }

        public TextConstraints build() {
            return new TextConstraints(minLength, maxLength);
        }
    }
}