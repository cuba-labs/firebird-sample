# firebird
This CUBA app-component provides support for the [Firebird](https://firebirdsql.org/) database.

Integration is implemented in the following classes:

* [FirebirdDdlGenerator](https://github.com/comru/firebird/blob/master/modules/core/src/com/haulmont/studio/db/firebird/FirebirdDdlGenerator.groovy) -
 Implements functionality for generating DDL scripts and SQL statements for DB versioning.
* [FirebirdDbProperties](https://github.com/comru/firebird/blob/master/modules/core/src/com/haulmont/studio/db/firebird/FirebirdDbProperties.groovy) -
 Defines custom database properties required for using Firebird in a project.
* [FirebirdDbmsFeatures](https://github.com/comru/firebird/blob/master/modules/core/src/com/haulmont/cuba/core/sys/persistence/FirebirdDbmsFeatures.java) -
 Implements the DbmsFeatures interface for Firebird.
* [FirebirdDbTypeConverter](https://github.com/comru/firebird/blob/master/modules/core/src/com/haulmont/cuba/core/sys/persistence/FirebirdDbTypeConverter.java) -
 Implements the DbTypeConverter interface for Firebird.
* [FirebirdSequenceSupport](https://github.com/comru/firebird/blob/master/modules/core/src/com/haulmont/cuba/core/sys/persistence/FirebirdSequenceSupport.java) -
 Implements the SequenceSupport interface for Firebird.

Some CUBA entities contains attributes with reserved RDBMS keyword for Firebird DDL. As a workaround it is required to override the following classes:

* com.haulmont.cuba.core.sys.AppContextLoader is overridden by the
[FirebirdAppContextLoader](https://github.com/comru/firebird/blob/master/modules/core/src/com/company/firebird4/core/FirebirdAppContextLoader.java) class -
modifies column names which cannot be used in Firebird.
* com.haulmont.cuba.core.app.ConfigStorage is overridden by the
[FirebirdConfigStorage](https://github.com/comru/firebird/blob/master/modules/core/src/com/company/firebird4/core/FirebirdConfigStorage.java) class -
modifies SQL query that conflicts with reserved keywords convention of Firebird.

## Running the project

1. Install Fierbird DB server.
2. Open the project in CUBA Studio (Import -> Git)
3. Modify the [build.gradle](https://github.com/comru/firebird/blob/master/build.gradle#L230) file and set correct paths
to the aliases.conf and fdb files.
4. Execute the create db task from Studio
5. Start the project from the Studio

## Using as app-component



1. Install Fierbird DB server.
2. Open the project in CUBA Studio (Import -> Git).
3. Execute the 'install app component' task.
4. Create new a project or open already exists project.
5. Add recently installed app component to your project. (Project properties -> add custom component).
6. Select Firebird db type.
3. In build.gradle - create a FierbirdDbCreation class which extends the AbstractDbCreation class and override
the dropAndCreateDatabase method. Find an example [here](https://github.com/comru/firebird/blob/master/build.gradle#L230).
Note, that this implementation refers to the full path to the aliases.conf and fdb files.
7. Add Firebird dependecy to buildscript dependencies block. Find an example
 [here](https://github.com/comru/firebird/blob/master/build.gradle#L17).
8. In build.gradle - remove masterUrl, createDbSql, dropDbSql property of `createDb` task.
9. Add the FirebirdAppContextLoader class and register it in \core\web\WEB-INF\web.xml. Find an example
 [here](https://github.com/comru/firebird/blob/master/modules/core/web/WEB-INF/web.xml#L19).
10. Execute the create db task from Studio.
11. Start the project from the Studio.


