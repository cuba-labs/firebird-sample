# firebird
Cuba app-component provided support [Firebird](https://firebirdsql.org/) database.

Basic classes:
* [FirebirdDdlGenerator](https://github.com/comru/firebird/blob/master/modules/core/src/com/haulmont/studio/db/firebird/FirebirdDdlGenerator.groovy) -
 This class is used by Studio at design time for working with Firebird. It provides a set of properties and methods for generation of database init and update scripts, and for creating model from an existing database.
* [FirebirdDbProperties](https://github.com/comru/firebird/blob/master/modules/core/src/com/haulmont/studio/db/firebird/FirebirdDbProperties.groovy) -
 This class is used by Studio at design time for working with Firebird. It defines custom database properties that are needed to configure projects using Firebird.
* [FirebirdDbmsFeatures](https://github.com/comru/firebird/blob/master/modules/core/src/com/haulmont/cuba/core/sys/persistence/FirebirdDbmsFeatures.java) -
 DbmsFeatures implementation for Firebird.
* [FirebirdDbTypeConverter](https://github.com/comru/firebird/blob/master/modules/core/src/com/haulmont/cuba/core/sys/persistence/FirebirdDbTypeConverter.java) -
 DbTypeConverter implementation for Firebird.
* [FirebirdSequenceSupport](https://github.com/comru/firebird/blob/master/modules/core/src/com/haulmont/cuba/core/sys/persistence/FirebirdSequenceSupport.java) -
 SequenceSupport implementation for Firebird.

Some CUBA entities contains attributes with reserved RDBMS keyword for Firebird DDL.
 It is for this reason needed override com.haulmont.cuba.core.sys.AppContextLoader and com.haulmont.cuba.core.app.ConfigStorage.
 Overrided classes:

* [FirebirdAppContextLoader](https://github.com/comru/firebird/blob/master/modules/core/src/com/company/firebird4/core/FirebirdAppContextLoader.java) -
 change mapping column name for reserved RDBMS keywords.
* [FirebirdConfigStorage](https://github.com/comru/firebird/blob/master/modules/core/src/com/company/firebird4/core/FirebirdConfigStorage.java) -
 change native query with reserved RDBMS keywords.

How to run project?
1. Install Fierbird DB server.
2. Open the project in CUBA Studio (Import -> Git)
3. Create DB (Run -> Create database). В build.gradle используется собственный тип задачи `crateDb` - FierbirdDbCreation,
 который переопределяет поведение удаления и создания БД. Это сделанно, по причине того, что стандартное поведение
  CubaDbCreation расчитывает на возможность удалить и создать БД c помощью JDBC (ant.sql).
  FierbirdDbCreation использует абсолютный путь до расположения Firebird `aliases.conf` файла и
  до места создания нового файла *.FDB БД. FierbirdDbCreation#dropAndCreateDatabase нужно и можно менять.
4. Start project.

Use as app-component:

1. Install Fierbird DB server.
2. Open the project in CUBA Studio (Import -> Git).
3. Execute 'install app component' task.
4. Create new a project or open already exists project.
5. Add the project as app component. (Project properties -> add custom component).
6. Select Firebird db type
6. Go to the build.gradle file, add FierbirdDbCreation class and change type of `createDb` task
7. Add Firebird dependecy to buildscript dependencies block.
8. Add FirebirdAppContextLoader class and register it in \core\web\WEB-INF\web.xml
9. Create DB.
10. Start project.


