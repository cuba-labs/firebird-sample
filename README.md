# firebird
Cuba app-component provided support Firebird database.

Basic classes:

* FirebirdDdlGenerator - This class is used by Studio at design time for working with Firebird.
It provides a set of properties and methods for generation of database init and update scripts, and for creating model from an existing database.
* FirebirdDbProperties - This class is used by Studio at design time for working with Firebird. 
It defines custom database properties that are needed to configure projects using Firebird.
* FirebirdDbmsFeatures - DbmsFeatures implementation for Firebird.
* FirebirdDbTypeConverter - DbTypeConverter implementation for Firebird.
* FirebirdSequenceSupport - SequenceSupport implementation for Firebird.
