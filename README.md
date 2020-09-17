# Firebird integration into the CUBA Studio
This application component provides support for the [Firebird](https://firebirdsql.org/) database.

Additional documentation:
* CUBA Studio User Guide - [Integrating with Custom Database](https://doc.cuba-platform.com/studio/#custom_db)
* CUBA Platform Developer’s Manual - [Support for Other DBMSs](https://doc.cuba-platform.com/manual-7.2/arbitrary_dbms.html)

The integration is implemented using the following classes:

* [FirebirdDdlGenerator](https://github.com/cuba-labs/firebird-sample/blob/master/modules/core/src/com/haulmont/studio/db/firebird/FirebirdDdlGenerator.groovy) -
 Implements functionality for generating DDL scripts and SQL statements for DB versioning.
* [FirebirdDbProperties](https://github.com/cuba-labs/firebird-sample/blob/master/modules/core/src/com/haulmont/studio/db/firebird/FirebirdDbProperties.groovy) -
 Defines custom database properties required for using Firebird in a project.
* [FirebirdDbmsFeatures](https://github.com/cuba-labs/firebird-sample/blob/master/modules/core/src/com/haulmont/cuba/core/sys/persistence/FirebirdDbmsFeatures.java) -
 Implements the DbmsFeatures interface for Firebird.
* [FirebirdDbTypeConverter](https://github.com/cuba-labs/firebird-sample/blob/master/modules/core/src/com/haulmont/cuba/core/sys/persistence/FirebirdDbTypeConverter.java) -
 Implements the DbTypeConverter interface for Firebird.
* [FirebirdSequenceSupport](https://github.com/cuba-labs/firebird-sample/blob/master/modules/core/src/com/haulmont/cuba/core/sys/persistence/FirebirdSequenceSupport.java) -
 Implements the SequenceSupport interface for Firebird.
* [build.gradle](https://github.com/cuba-labs/firebird-sample/blob/master/build.gradle) - contains Firebird-specific database creation logic. 

## Running the project

1. Download and install Firebird DB server.
2. Download Jaybird JDBC driver (https://firebirdsql.org/en/jdbc-driver/). Extract ZIP archive and copy necessary driver files to the `{user home}/.haulmont/studio/lib` folder. E.g. for the `jaybird-4.0.1.java8.zip` version the list of files to be copied is: `jaybird-4.0.1.java8.jar`, `antlr-runtime-4.7.2.jar`, `connector-api-1.5.jar`, `jna-5.5.0.jar`.
3. Open the project in the CUBA Studio (*File* -> *New Project from Version Control*)
4. Modify the [app.properties](https://github.com/cuba-labs/firebird-sample/blob/master/modules/core/src/com/company/firebird4/app.properties#L31) file and set correct paths in the `cuba.dataSource.jdbcUrl` and other nearby properties. Alternatively - change database URL in the Main Data Store Settings dialog (main menu: *CUBA* -> *Main Data Store Settings*). 
5. Execute the *CUBA* -> *Create Database* task in the main menu.
6. Start the project from the Studio.

## Using as app-component

1. Execute steps mentioned above with the firebird-sample project to test that your environment is configured correctly.
2. Run *CUBA* -> *Advanced* -> *Install App Component* task.
3. Create new or open already existing project that needs Firebird support.
4. Add just installed app component to your project (*CUBA* -> *Marketplace* -> *Install Add-On manually* button). Default add-on coordinates defined in this sample project are `com.company.firebird4:firebird-global:0.1-SNAPSHOT`.
5. In the *Project Properties* dialog select *Firebird* database type. Specify database URL in the `jdbc:firebirdsql://{HOST}/{PATH_TO_DB_FILE}?encoding=UTF8` format.
6. Make changes to the `build.gradle` script:
* Add Firebird dependency to the `buildscript` - `dependencies` block. Find an example [here](https://github.com/cuba-labs/firebird-sample/blob/master/build.gradle#L14). 
* Create a `FirebirdDbCreation` class which extends the AbstractDbCreation class and overrides
the dropAndCreateDatabase method. Find an example [here](https://github.com/cuba-labs/firebird-sample/blob/master/build.gradle#L191).
* Replace the type of the `createDb` task with `FirebirdDbCreation` instead of `CubaDbCreation` class.
* Remove `masterUrl`, `createDbSql`, `dropDbSql` property of the `createDb` task (or just make them empty).
7. Execute the *CUBA* -> *Create Database* task in the main menu.
8. Start the project from the Studio.

## Linux usage notes
Directory where database files are located should be owned by the `firebird` user running Firebird server.
