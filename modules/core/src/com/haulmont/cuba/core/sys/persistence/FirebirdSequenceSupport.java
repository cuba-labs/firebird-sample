package com.haulmont.cuba.core.sys.persistence;

/**
 * SequenceSupport implementation for Firebird.
 */
@SuppressWarnings("unused")
public class FirebirdSequenceSupport implements SequenceSupport {

    @Override
    public String sequenceExistsSql(String sequenceName) {
        return "SELECT * FROM RDB\\$GENERATORS WHERE RDB\\$GENERATOR_NAME = '" + sequenceName.toUpperCase() + "'";
    }

    @Override
    public String createSequenceSql(String sequenceName, long startValue, long increment) {
        return "create sequence " + sequenceName.toUpperCase() + " ;\n" +
                "alter sequence " + sequenceName.toUpperCase() + " restart with " + startValue + ";";
    }

    @Override
    public String modifySequenceSql(String sequenceName, long startWith) {
        return "alter sequence " + sequenceName + " restart with " + startWith;
    }

    @Override
    public String deleteSequenceSql(String sequenceName) {
        return "drop sequence " + sequenceName.toUpperCase();
    }

    @Override
    public String getNextValueSql(String sequenceName) {
        return "select next value for " + sequenceName.toUpperCase() + "FROM RDB$DATABASE";
    }

    @Override
    public String getCurrentValueSql(String sequenceName) {
        return "SELECT GEN_ID(" + sequenceName.toUpperCase() + ", 0) FROM RDB$DATABASE";
    }
}