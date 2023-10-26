package org.metadatacenter.nih.ingestor.converter;

import java.util.ArrayList;
import java.util.Optional;

public non-sealed class ValueListConstraints implements CDEConstraints {

    private ArrayList<String> permissibleValues;

    public ValueListConstraints(ArrayList<String> permissibleValues) {
        this.permissibleValues = permissibleValues;
    }

    public ArrayList<String> getPermissibleValues() {
        return permissibleValues;
    }

    public static class ValueListConstraintsBuilder {
        private ArrayList<String> permissibleValues;

        public ValueListConstraints.ValueListConstraintsBuilder withPermissibleValues(ArrayList<String> permissibleValues) {
            this.permissibleValues = permissibleValues;
            return this;
        }

        public ValueListConstraints build() {
            return new ValueListConstraints(permissibleValues);
        }
    }
}
