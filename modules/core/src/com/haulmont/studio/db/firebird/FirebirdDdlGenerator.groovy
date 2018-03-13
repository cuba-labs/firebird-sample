package com.haulmont.studio.db.firebird

import java.sql.DatabaseMetaData
import groovy.sql.Sql

/**
 * This class is used by Studio at design time for working with Firebird.
 * It provides a set of properties and methods for generation of database init and update scripts,
 * and for creating model from an existing database.
 */
@SuppressWarnings("GroovyUnusedDeclaration")
class FirebirdDdlGenerator {

    /**
     * An object that contains helper methods and default implementations.
     */
    DdlGeneratorDelegate delegate

    /**
     * Map of attribute's Java type to an SQL column type.
     * <p>
     * The key is a simple class name, e.g. 'Boolean', 'byte[]', the value is column type, e.g. 'tinyint', 'image'.
     */
    Map<String, String> types = [
            'Boolean' : 'smallint',
            'byte[]' : 'blob',
            'Character' : 'char',
            'Date' : 'timestamp',
            'BigDecimal' : 'decimal',
            'Double' : 'double precision',
            'Integer' : 'integer',
            'Long' : 'bigint',
            'String' : 'varchar',
            'UUID' : 'varchar(36)'
    ]

    /**
     * Map of temporal type names to column types.
     * <p>
     * The key is one of the following strings: TIMESTAMP, DATE or TIME.
     * The value is a column type, e.g. 'timestamp'.
     */
    Map<String, String> temporalTypes = [
            'DATE' : 'date',
            'TIME' : 'time',
            'TIMESTAMP' : 'timestamp'
    ]

    /**
     * Map of column types that can be converted without removing and creating a new column,
     * but solely by executing 'alter column'.
     * For example:
     * <pre>
     * text -> varchar, nvarchar, ntext
     * bigint -> int4, int, integer, int identity, smallint
     * </pre>
     * The key is a column type, the value is a collection of column types.
     */
    Map<String, List<String>> convertibleTypes = [:]

    /**
     * Map of attribute's Java type to an SQL column default value.
     * <p>
     * The key is is a simple class name, the value is a default value.
     */
    Map<String, String> defaultValues = [
            'Boolean' : '0',
            'ByteArray' : "''",
            'Date' : 'CURRENT_DATE',
            'DateTime' : 'CURRENT_TIMESTAMP',
            'Time' : 'CURRENT_TIME',
            'BigDecimal' : '0',
            'Double' : '0',
            'Integer' : '0',
            'Long' : '0',
            'String' : "''",
            'UUID' : '(select res from newid)'
    ]
    /**
     * Collection of column type synonyms. A synonym is a column type that can be used for the same Java type.
     * <p>
     * This collection is used when Studio determines whether to generate an update script for a column with changed
     * Java type in the model. For example:
     * <ol>
     * <li>Attribute has changed from String to Integer. Studio picks 'int' type from {@link #types} collection and checks
     * whether the previous 'varchar' type and the new 'int' type are synonyms. They are not, so Studio generates
     * statements for dropping and adding a column with the new type.
     * <li>For a String attribute, a developer has changed type in the database to 'nvarchar'. When Studio generates scripts,
     * it sees that the default 'varchar' type and 'nvarchar' are synonyms, and does not generate updates for the column.
     * </ol>
     */
    List<List<String>> typeSynonyms = [
            ['char', 'character'],
            ['blob'],
            ['timestamp'],
            ['time'],
            ['date'],
            ['decimal', 'numeric'],
            ['double precision', 'float'],
            ['integer', 'int', 'smallint'],
            ['bigint'],
            ['varchar', 'char varying', 'character varying']
    ]

    /**
     * DBMS reserved words.
     */
    List<String> reservedKeywords = [
            'ADD', 'ADMIN', 'ALL', 'ALTER', 'AND', 'ANY', 'AS', 'AT', 'AVG', 'BEGIN', 'BETWEEN', 'BIGINT', 'BIT_LENGTH',
            'BLOB', 'BOTH', 'BY', 'CASE', 'CAST', 'CHAR', 'CHAR_LENGTH', 'CHARACTER', 'CHARACTER_LENGTH', 'CHECK', 'CLOSE', 'COLLATE',
            'COLUMN', 'COMMIT', 'CONNECT', 'CONSTRAINT', 'COUNT', 'CREATE', 'CROSS', 'CURRENT', 'CURRENT_CONNECTION', 'CURRENT_DATE',
            'CURRENT_ROLE', 'CURRENT_TIME', 'CURRENT_TIMESTAMP', 'CURRENT_TRANSACTION', 'CURRENT_USER',
            'CURSOR', 'DATE', 'DAY', 'DEC', 'DECIMAL', 'DECLARE', 'DEFAULT', 'DELETE', 'DISCONNECT', 'DISTINCT', 'DOUBLE',
            'DROP', 'ELSE', 'END', 'ESCAPE', 'EXECUTE', 'EXISTS', 'EXTERNAL', 'EXTRACT', 'FETCH', 'FILTER', 'FLOAT', 'FOR', 'FOREIGN',
            'FROM', 'FULL', 'FUNCTION', 'GDSCODE', 'GLOBAL', 'GRANT', 'GROUP', 'HAVING', 'HOUR', 'IN', 'INDEX', 'INNER', 'INSENSITIVE',
            'INSERT', 'INT', 'INTEGER', 'INTO', 'IS', 'JOIN', 'LEADING', 'LEFT', 'LIKE', 'LONG', 'LOWER', 'MAX', 'MAXIMUM_SEGMENT', 'MERGE',
            'MIN', 'MINUTE', 'MONTH', 'NATIONAL', 'NATURAL', 'NCHAR', 'NO', 'NOT', 'NULL', 'NUMERIC', 'OCTET_LENGTH', 'OF', 'ON', 'ONLY',
            'OPEN', 'OR', 'ORDER', 'OUTER', 'PARAMETER', 'PLAN', 'POSITION', 'POST_EVENT', 'PRECISION', 'PRIMARY', 'PROCEDURE', 'REALY',
            'RECORD_VERSION', 'RECREATE', 'RECURSIVE', 'REFERENCES', 'RELEASE', 'RETURNING_VALUES', 'RETURNS', 'REVOKE', 'RIGHT', 'ROLLBACK',
            'ROW_COUNT', 'ROWS', 'SAVEPOINT', 'SECOND', 'SELECT', 'SENSITIVE', 'SET', 'SIMILAR', 'SMALLINT', 'SOME', 'SQLCODE',
            'START', 'SUM', 'TABLE', 'THEN', 'TIME', 'TIMESTAMP', 'TO', 'TRAILING', 'TRIGGER', 'TRIM', 'UNION', 'UNIQUE', 'UPDATE', 'UPPER',
            'USER', 'USING', 'VALUE', 'VALUES', 'VARCHAR', 'VARIABLE', 'VARYING', 'VIEW', 'WHEN', 'WHERE', 'WHILE', 'WITH', 'YEAR'
    ]

    /**
     * Collection of column types that do not support parameters.
     * <p>
     * Studio generates column definitions by information from {@link DatabaseMetaData}, which always contains
     * column length. But many types do not support explicit definining of length, for example 'int' type has length
     * 2147483647, but `mycol int(2147483647)` won't work.
     */
    List<String> noParameterTypes = [
            "blob", "timestamp", "time", "date", "double precision", "float", "integer", "int", "smallint", "bigint"
    ]

    /**
     * Columns of these types are filled in automatically by DBMS and not mapped to a Java type.
     */
    List<String> automaticallyGeneratedTypes = []

    /**
     * Returns "drop table" statement.
     *
     * @param metaData  JDBC metadata
     * @param schema    database schema
     * @param tableName table name
     * @return a statement
     */
    String getDropTableStatement(DatabaseMetaData metaData, String schema, String tableName) {
        "drop table $tableName ^"
    }

    /**
     * Returns the list of statements for removing constraints before dropping a table.
     * <p>
     * If the database removes constraints automatically, the method should return an empty list.
     *
     * @param metaData  JDBC metadata
     * @param schema    database schema
     * @param tableName table name
     * @return a list of statements
     */
    List<String> getDropTableConstraintStatements(DatabaseMetaData metaData, String schema, String tableName) {
        List<String> result = []
        delegate.defaultGetDropTableConstraintNames(metaData, schema, tableName).each {
            result.add(getDropConstraintStatement(tableName, it.toUpperCase()))
        }
        return result
    }

    /**
     * Returns names of unique and non-unique indexes for a given column.
     * <p>
     * Deafult implementation: {@link DdlGeneratorDelegate#defaultGetColumnIndexNames(java.sql.DatabaseMetaData, java.lang.String, java.lang.String, java.lang.String)}
     *
     * @param metaData      java.sql.DatabaseMetaData object
     * @param schema        database schema
     * @param tableName     table name
     * @param columnName    column name
     * @return collection of index names
     */
    Collection<String> getColumnIndexNames(DatabaseMetaData metaData, String schema, String tableName, String columnName) {
        Set<String> indexes = new LinkedHashSet<>()
        delegate.defaultGetColumnIndexNames(metaData, schema, tableName, columnName).each {
            indexes.add(it)
        }
        return indexes
    }

    /**
     * Returns names of constraints for a given column.
     * <p>
     * Default implementation: {@link DdlGeneratorDelegate#defaultGetColumnConstraintNames(java.sql.DatabaseMetaData, java.lang.String, java.lang.String, java.lang.String)}
     *
     * @param metaData      java.sql.DatabaseMetaData object
     * @param schema        database schema
     * @param tableName     table name
     * @param columnName    column name
     * @return collection of constraint names
     */
    Collection<String> getColumnConstraintNames(DatabaseMetaData metaData, String schema, String tableName,
                                                String columnName) {
        Set<String> constraints = new LinkedHashSet<>()
        delegate.defaultGetColumnConstraintNames(metaData, schema, tableName, columnName).each {
            constraints.add(it)
        }
        return constraints
    }

    /**
     * Returns "drop column" statement.
     *
     * @param tableName     table name
     * @param columnName    column name
     * @return  a statement
     */
    String getDropColumnStatement(String tableName, String columnName) {
        "alter table $tableName drop ${delegate.wrapColumnInQuotesIfNeeded(columnName)} ^"
    }

    /**
     * Returns the list of statements for dropping all constraints for a column.
     * <p>
     * Default implementation: {@link DdlGeneratorDelegate#defaultGetDropColumnConstraintStatements(java.lang.String, java.lang.String)}
     *
     * @param tableName     table name
     * @param columnName    column name
     * @return list of statements
     */
    List<String> getDropColumnConstraintStatements(String tableName, String columnName) {
        delegate.defaultGetDropColumnConstraintStatements(tableName, columnName)
    }

    /**
     * Returns column name for inserting to "add column" script.
     * <p>
     * Default implementation: {@link DdlGeneratorDelegate#defaultPrepareColumnForAddScript(java.lang.String)}
     *
     * @param columnName
     * @return column name
     */
    String prepareColumnForAddScript(String columnName) {
        return delegate.defaultPrepareColumnForAddScript(columnName)
    }

    /**
     * Returns a statement for adding a column to a table creating it from entity and attribute objects.
     *
     * @param entity    entity object
     * @param attribute attribute object
     * @param column    column name
     * @return "alter table add column" statement
     */
    String getAddColumnStatement(def entity, def attribute, String column) {
        if (!attribute.isEmbedded()) {
            StringBuilder sb = new StringBuilder()
            String col = delegate.getColumnDefinition(entity, attribute, column.toUpperCase(), attribute.isMandatory() && attribute.isClass())
            sb.append("alter table ${entity.table} add $col ^")
            delegate.processScriptAddingMandatoryColumn(sb, entity, attribute, column.empty ? attribute.getDdlManipulationColumn() : column)
            return sb.toString()
        } else {
            return delegate.getAddColumnsForEmbeddedAttrStatements(entity, attribute, Collections.emptyList())
        }
    }

    /**
     * Returns a statement for adding a column to a table creating it from table and column names passed as strings.
     *
     * @param tableName     table name
     * @param columnName    column name
     * @param type          attribute type object with the following properties: packageName, className, fqn, label
     * @param notNull       column nullability
     * @return "alter table add column" statement
     */
    String getAddColumnStatement(String tableName, String columnName, def type, boolean notNull) {
        return "alter table ${tableName} add ${columnName} ${delegate.defaultGetColumnType(type.className)} ${notNull ? 'not null ' : ''} ^"
    }

    /**
     * Returns a statement for changing string column length. Used only for String attributes.
     *
     * @param entity        entity object
     * @param attribute     attribute object
     * @param newType       new type name
     * @param columnName    column name
     * @return "alter table alter column" statement
     */
    String getAlterColumnStringLengthStatement(def entity, def attribute, String newType, String columnName) {
        String col = delegate.getColumnName(attribute, columnName)
        Integer length = delegate.getColumnLength(entity, attribute)
        String newLength = (length != null && length != 0) ? "(" + length.toString() + ")" : ""
        return "alter table ${entity.table} alter column ${col} ${newType}${newLength} ^"
    }

    /**
     * Returns a statement for changing decimal column parameters. Used only for BigDecimal attributes.
     *
     * @param entity        entity object
     * @param attribute     attribute object
     * @param newType       new type name
     * @param columnName    column name
     * @return "alter table alter column" statement
     */
    String getAlterColumnDecimalParamsStatement(def entity, def attribute, String newType, String columnName) {
        String col = delegate.getColumnName(attribute, columnName)
        String params = delegate.getDecimalParams(entity, attribute)
        return "alter table ${entity.table} alter column ${col} ${newType}(${params}) ^"
    }

    /**
     * Returns a statement for changing column type.
     *
     * @param entity        entity object
     * @param attribute     attribute object
     * @param newType       new type name
     * @param columnName    column name
     * @return "alter table alter column" statement
     */
    String getAlterColumnTypeStatement(def entity, def attribute, String newType, String columnName) {
        String col = delegate.getColumnName(attribute, columnName)
        return "alter table ${entity.table} alter column ${col} ${newType} ^"
    }

    /**
     * Returns a statement for changing column nullability.
     *
     * @param tableName     table name
     * @param columnName    column name
     * @param columnDef     column parameters
     * @param isMandatory   true if not null
     * @return "alter table alter column" statement
     */
    String getAlterColumnMandatoryStatement(String tableName, String columnName, String columnDef, boolean isMandatory) {
        delegate.showNotification("WARNING", "Firebird unsupported alter mandatory constraints for exists column. " +
                "Table: ${tableName}; Column: ${columnName}")
        return """-- Firebird unsupported alter mandatory constraints for exists column.\n
-- alter table ${tableName} alter column ${columnName} ${(isMandatory ? "set" : "drop")} not null ^"""
    }

    /**
     * Returns a statement for changing column nullability.
     *
     * @param entity        entity object
     * @param attribute     attribute object
     * @param columnName    column name
     * @return "alter table alter column" statement
     */
    String getAlterColumnMandatoryStatement(def entity, def attribute, String columnName) {
        return getAlterColumnMandatoryStatement(entity.table, attribute.column, '', attribute.isMandatory())
    }

    /**
     * Returns a statement for renaming table.
     *
     * @param oldTable old table name
     * @param newTable new table name
     * @return a statement
     */
    String getRenameTableSatement(String oldTable, String newTable) {
        delegate.showNotification("WARNING", "Firebird unsupported rename table. Table - ${oldTable}")
        return """-- Firebird unsupported rename table.
-- alter table ${oldTable} rename to ${newTable} ^"""
    }

    /**
     * Returns a statement for renaming column.
     *
     * @param table     table name
     * @param oldColumn old column name
     * @param newColumn new column name
     * @return a statement
     */
    String getRenameColumnStatement(String table, String oldColumn, String newColumn) {
        return "alter table ${table} alter column ${oldColumn} to ${newColumn} ^\n"
    }

    /**
     * For a BigDecimal attribute, returns true if the precision of the attribute differs from the precision of the
     * database column.
     *
     * @param attributePrecision    attribute precision
     * @param columnPrecision       column precision
     * @param columnType            column type
     * @return true if the precision is different
     */
    boolean isPrecisionDifferent(int attributePrecision, int columnPrecision, String columnType) {
        //default precision for decimal column in mssql is 18
        return attributePrecision != columnPrecision && !(columnPrecision == 18 && attributePrecision == 0)
    }

    /**
     * For a BigDecimal attribute, returns true if the scale of the attribute differs from the scale of the
     * database column.
     * <p>
     * Default implementation: {@link DdlGeneratorDelegate#defaultIsScaleDifferent(int, int, java.lang.String)}
     *
     * @param attributeScale    attribute scale
     * @param columnScale       column scale
     * @param columnType        column type
     * @return true/false
     */
    Boolean isScaleDifferent(int attributeScale, int columnScale, String columnType) {
        delegate.defaultIsScaleDifferent(attributeScale, columnScale, columnType)
    }

    /**
     * Returns a statement for primary key definition.
     * <p>
     * Default implementation: {@link DdlGeneratorDelegate#defaultGetPrimaryKeyStatement(java.lang.Object)}
     *
     * @param entity entity object
     * @return a statement
     */
    String getPrimaryKeyStatement(def entity) {
        delegate.defaultGetPrimaryKeyStatement(entity)
    }

    /**
     * Returns a statement for making an existing column a primary key.
     * <p>
     * Default implementation: {@link DdlGeneratorDelegate#defaultGetPrimaryKeyConstraintStatement(java.lang.Object, boolean)}
     *
     * @param entity   entity object
     * @param notNull  whether to add "not null" to the column
     * @return a statement
     */
    String getPrimaryKeyConstraintStatement(def entity, boolean notNull) {
        delegate.defaultGetPrimaryKeyConstraintStatement(entity, notNull)
    }

    /**
     * Returns true if the current column type is different and not compatible with the old one.
     * <p>
     * Default implementation: {@link DdlGeneratorDelegate#defaultIsTypeDifferent(java.lang.Object, java.lang.String, java.lang.String, int)}
     *
     * @param attribute     attribute object
     * @param currentType   current column type
     * @param oldType       old column type
     * @param oldLength     old column length
     * @return true if different
     */
    Boolean isTypeDifferent(def attribute, String currentType, String oldType, int oldLength) {
        if (currentType.equals("blob") && oldType.equalsIgnoreCase("blob sub_type 0")) {
            return false
        }
        return delegate.defaultIsTypeDifferent(attribute, currentType, oldType, oldLength)
    }

    /**
     * Returns a statement for creating a sequence.
     *
     * @param sequenceName  sequence name
     * @param startValue    start value
     * @param increment     increment
     * @return a statement
     */
    String getCreateSequenceStatement(String sequenceName, long startValue, long increment) {
        return "create sequence ${sequenceName};\n" +
                "alter sequence ${sequenceName} restart with ${startValue};"
    }

    /**
     * Returns a statement for checking if a sequence with the given name exists in the database.
     *
     * @param sequenceName name of sequence
     * @return a statement
     */
    String getSequenceExistsStatement(String sequenceName) {
        return "SELECT * FROM RDB\$GENERATORS WHERE RDB\$GENERATOR_NAME = '${sequenceName}'"
    }

    /**
     * Returns a statement for deleting a sequence with the given name from the database.
     *
     * @param sequenceName name of sequence
     * @return a statement
     */
    String getDeleteSequenceStatement(String sequenceName) {
        return "drop sequence ${sequenceName}"
    }

    /**
     * Returns a maximum length of a string type (varchar and all its synonyms).
     * By default, it is {@link Integer#MAX_VALUE}.
     *
     * @param columnType    column type
     * @return max length
     */
    Integer getMaxVarcharLength(String columnType) {
        if (columnType?.startsWith("blob sub_type")) {
            return 0;
        }
        return Integer.MAX_VALUE
    }

    /**
     * Returns long identity column type.
     *
     * @return type name
     */
    String getLongIdentityType() {
        return 'bigint'
    }

    /**
     * Returns integer identity column type.
     *
     * @return type name
     */
    String getIntegerIdentityType() {
        return 'integer'
    }

    /**
     * Returns the part of a column definition containing parameters and (optional) "not null" clause.
     * Parameters must be enclosed in parentheses.
     * The returning string is used when a table is created and when a column is added or altered.
     *
     * @param entity        entity object
     * @param attribute     attribute object
     * @param notNull       whether to add "not null"
     * @return parameters string
     */
    String getColumnParameters(def entity, def attribute, boolean notNull) {
        if (attribute.isId() || attribute.isClass()) {
            return getPrimaryOrForeignKeyParams(attribute)
        }

        StringBuilder params = new StringBuilder()

        def attrType = attribute.getType()
        if ((attrType.fqn?.equals('java.lang.String') || attrType.fqn == 'java.lang.Character') && attribute.getLength()) {
            Integer length = delegate.getColumnLength(entity, attribute)
            if (length) {
                params.append('(').append(length).append(')')
            }
        } else if (attribute.isEnum() && attrType.getType()?.getId()?.equals('String')) {
            //enum string length
            params.append("(").append(delegate.getDefaultEnumColumnLength()).append(")");
        }

        delegate.processBigDecimalParams(attribute, params)
        //not null
        if ((attribute.mandatory && !attribute.isId()) && notNull) {
            params.append(" not null")
        }
        return params.toString()
    }

    /**
     * For the given column type, returns 'DATE', 'TIME' or 'TIMESTAMP' strings indicating what attribute
     * type should be used for the column.
     * <p>
     * Default implementation: {@link DdlGeneratorDelegate#defaultGetTemporalType(java.lang.String)}
     *
     * @param columnType    column type
     * @return 'DATE', 'TIME', 'TIMESTAMP' or null
     */
    String getTemporalType(String columnType) {
        return delegate.defaultGetTemporalType(columnType)
    }

    /**
     * Returns "drop index" statement.
     * For example:
     * <pre>drop index ${constraintName.toUpperCase()} ^</pre>
     *
     * @param tableName    table name
     * @param indexName    index name
     * @return "drop index" statement
     */
    String getDropIndexStatement(String tableName, String indexName) {
        "drop index ${indexName.toUpperCase()} ^"
    }

    /**
     * Returns statement for dropping a foreign key constraint.
     *
     * @param tableName         table name
     * @param constraintName    constraint name
     * @return a statement
     */
    String getDropConstraintStatement(String tableName, String constraintName) {
        "alter table $tableName drop constraint ${constraintName.toUpperCase()} ^\n"
    }

    /**
     * Returns column type for a given attribute.
     * <p>
     * Default implementation: {@link DdlGeneratorDelegate#defaultGetColumnType(java.lang.String)}
     *
     * @param entity    entity object
     * @param attribute attribute object
     * @return column type
     */
    String getColumnType(def entity, def attr) {
        if (attr.isDatatype()
                && attr.type.fqn?.equals('java.lang.String')
                && !attr.length) {
            return "blob sub_type text"
        }
        delegate.defaultGetColumnType(attr.type.className)
    }

    /**
     * Returns column type for a given attribute if it is a primary or foreign key.
     * Default implementation: {@link DdlGeneratorDelegate#defaultGetPrimaryOrForeignKeyType(java.lang.Object)}
     *
     * @param attribute attribute object
     * @return column type for a primary or foreign key
     */
    String getPrimaryOrForeignKeyType(def attribute) {
        delegate.defaultGetPrimaryOrForeignKeyType(attribute)
    }

    /**
     * Returns the part of a primary or foreign key column definition containing parameters
     * and (optional) "not null" clause.
     * Parameters must be enclosed in parentheses.
     * <p>
     * Default implementation: {@link DdlGeneratorDelegate#defaultGetPrimaryOrForeignKeyParams(java.lang.Object)}
     *
     * @param attribute attribute object
     * @return parameters string
     */
    String getPrimaryOrForeignKeyParams(def attribute) {
        delegate.defaultGetPrimaryOrForeignKeyParams(attribute)
    }

    /**
     * Returns collection of synonyms of the given column type.
     *
     * @param columnType    column type
     * @return collection of synonyms
     */
    Collection<String> getColumnTypeSynonyms(String columnType) {
        def synonyms = typeSynonyms.find { it.contains(columnType) }
        return synonyms ?: []
    }

    /**
     * Whether to include DELETE_TS column in a unique index for soft deleted entities.
     * Return false if the database if the database does not support uniqueness for records containing null,
     * as in HSQLDB.
     */
    boolean isDeleteTsInUniqueIndex() {
        return true
    }

    /**
     * (non-Javadoc)
     *
     * @see {@link DdlGeneratorDelegate#defaultReplaceDelimiter(java.lang.String)}
     */
    String replaceDelimiter(String script) {
        return script.replaceAll('([^\\^])\\^{2}([^\\^]|\\z)','$1\\^$2')
    }

    Integer getIdentifierMaxLength() {
        return 30
    }

    String generateImportInitScript() {
        return '''create procedure newid
returns (res varchar(36))
as
begin
    res = uuid_to_char(gen_uuid());
    suspend;
end ^'''
    }

    boolean isGenerateSeparatelyDropScripts() {
        return false
    }
}