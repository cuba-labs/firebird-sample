-- begin SYS_CONFIG
create table SYS_CONFIG (
    ID varchar(36),
    VERSION integer,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    --
    NAME varchar(255),
    VALUE_ varchar(255),
    --
    primary key (ID)
)^
-- end SYS_CONFIG
-- begin SEC_GROUP_HIERARCHY
create table SEC_GROUP_HIERARCHY (
    ID varchar(36),
    --
    GROUP_ID varchar(36) not null,
    PARENT_ID varchar(36) not null,
    HIERARCHY_LEVEL integer,
    --
    primary key (ID)
)^
-- end SEC_GROUP_HIERARCHY
-- begin SYS_LOCK_CONFIG
create table SYS_LOCK_CONFIG (
    ID varchar(36),
    --
    NAME varchar(100) not null,
    TIMEOUT_SEC integer not null,
    --
    primary key (ID)
)^
-- end SYS_LOCK_CONFIG
-- begin SEC_SEARCH_FOLDER
create table SEC_SEARCH_FOLDER (
    FOLDER_ID varchar(36),
    FILTER_COMPONENT varchar(255),
    FILTER_XML varchar(255),
    APPLY_DEFAULT smallint,
    --
    USER_ID varchar(36),
    PRESENTATION_ID varchar(36),
    IS_SET smallint,
    ENTITY_TYPE varchar(255),
    --
    primary key (FOLDER_ID)
)^
-- end SEC_SEARCH_FOLDER
-- begin SEC_CONSTRAINT
create table SEC_CONSTRAINT (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CHECK_TYPE varchar(50) not null,
    OPERATION_TYPE varchar(50) not null,
    CODE varchar(255),
    ENTITY_NAME varchar(255) not null,
    JOIN_CLAUSE varchar(500),
    WHERE_CLAUSE varchar(1000),
    GROOVY_SCRIPT blob sub_type text,
    FILTER_XML blob sub_type text,
    IS_ACTIVE smallint,
    GROUP_ID varchar(36),
    --
    primary key (ID)
)^
-- end SEC_CONSTRAINT
-- begin SYS_FOLDER
create table SYS_FOLDER (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    FOLDER_TYPE varchar(31),
    --
    PARENT_ID varchar(36),
    NAME varchar(100),
    SORT_ORDER integer,
    TAB_NAME varchar(255),
    --
    primary key (ID)
)^
-- end SYS_FOLDER
-- begin SEC_ROLE
create table SEC_ROLE (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    LOC_NAME varchar(255),
    DESCRIPTION varchar(1000),
    ROLE_TYPE integer,
    IS_DEFAULT_ROLE smallint,
    --
    primary key (ID)
)^
-- end SEC_ROLE
-- begin SEC_PERMISSION
create table SEC_PERMISSION (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    PERMISSION_TYPE integer,
    TARGET varchar(100),
    VALUE_ integer,
    ROLE_ID varchar(36),
    --
    primary key (ID)
)^
-- end SEC_PERMISSION
-- begin SYS_SCHEDULED_EXECUTION
create table SYS_SCHEDULED_EXECUTION (
    ID varchar(36),
    --
    TASK_ID varchar(36),
    SERVER varchar(255),
    START_TIME timestamp,
    FINISH_TIME timestamp,
    RESULT varchar(255),
    --
    primary key (ID)
)^
-- end SYS_SCHEDULED_EXECUTION
-- begin SEC_USER_SUBSTITUTION
create table SEC_USER_SUBSTITUTION (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    USER_ID varchar(36) not null,
    SUBSTITUTED_USER_ID varchar(36) not null,
    START_DATE date,
    END_DATE date,
    --
    primary key (ID)
)^
-- end SEC_USER_SUBSTITUTION
-- begin SYS_CATEGORY
create table SYS_CATEGORY (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    DISCRIMINATOR integer,
    --
    NAME varchar(255) not null,
    ENTITY_TYPE varchar(255) not null,
    IS_DEFAULT smallint,
    LOCALE_NAMES varchar(255),
    SPECIAL varchar(255),
    --
    primary key (ID)
)^
-- end SYS_CATEGORY
-- begin SYS_ENTITY_SNAPSHOT
create table SYS_ENTITY_SNAPSHOT (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    --
    ENTITY_ID varchar(36),
    STRING_ENTITY_ID varchar(255),
    INT_ENTITY_ID integer,
    LONG_ENTITY_ID bigint,
    --
    VIEW_XML varchar(255),
    SNAPSHOT_XML varchar(255),
    ENTITY_META_CLASS varchar(255),
    SNAPSHOT_DATE timestamp not null,
    AUTHOR_ID varchar(36) not null,
    --
    primary key (ID)
)^
-- end SYS_ENTITY_SNAPSHOT
-- begin SYS_FTS_QUEUE
create table SYS_FTS_QUEUE (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    --
    ENTITY_ID varchar(36),
    STRING_ENTITY_ID varchar(255),
    INT_ENTITY_ID integer,
    LONG_ENTITY_ID bigint,
    ENTITY_NAME varchar(255),
    CHANGE_TYPE varchar(50),
    SOURCE_HOST varchar(255),
    INDEXING_HOST varchar(255),
    FAKE smallint,
    --
    primary key (ID)
)^
-- end SYS_FTS_QUEUE
-- begin SYS_ENTITY_STATISTICS
create table SYS_ENTITY_STATISTICS (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    --
    NAME varchar(50),
    INSTANCE_COUNT bigint,
    FETCH_UI integer,
    MAX_FETCH_UI integer,
    LAZY_COLLECTION_THRESHOLD integer,
    LOOKUP_SCREEN_THRESHOLD integer,
    --
    primary key (ID)
)^
-- end SYS_ENTITY_STATISTICS
-- begin SEC_LOGGED_ENTITY
create table SEC_LOGGED_ENTITY (
    ID varchar(36),
    --
    NAME varchar(100),
    AUTO smallint,
    MANUAL smallint,
    --
    primary key (ID)
)^
-- end SEC_LOGGED_ENTITY
-- begin SEC_ENTITY_LOG
create table SEC_ENTITY_LOG (
    ID varchar(36),
    --
    ENTITY_ID varchar(36),
    STRING_ENTITY_ID varchar(255),
    INT_ENTITY_ID integer,
    LONG_ENTITY_ID bigint,
    --
    EVENT_TS timestamp,
    USER_ID varchar(36),
    CHANGE_TYPE varchar(50),
    ENTITY varchar(100),
    CHANGES varchar(255),
    --
    primary key (ID)
)^
-- end SEC_ENTITY_LOG
-- begin SYS_FILE
create table SYS_FILE (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(500) not null,
    EXT varchar(20),
    FILE_SIZE bigint,
    CREATE_DATE timestamp,
    --
    primary key (ID)
)^
-- end SYS_FILE
-- begin SYS_QUERY_RESULT
create table SYS_QUERY_RESULT (
    ID bigint,
    SESSION_ID varchar(36),
    QUERY_KEY integer,
    ENTITY_ID varchar(36),
    STRING_ENTITY_ID varchar(255),
    INT_ENTITY_ID integer,
    LONG_ENTITY_ID bigint,
    --
    primary key (ID)
)^
-- end SYS_QUERY_RESULT
-- begin SYS_JMX_INSTANCE
create table SYS_JMX_INSTANCE (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NODE_NAME varchar(255),
    ADDRESS varchar(500) not null,
    LOGIN varchar(50) not null,
    PASSWORD varchar(255),
    --
    primary key (ID)
)^
-- end SYS_JMX_INSTANCE
-- begin SEC_GROUP
create table SEC_GROUP (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    PARENT_ID varchar(36),
    --
    primary key (ID)
)^
-- end SEC_GROUP
-- begin SEC_LOGGED_ATTR
create table SEC_LOGGED_ATTR (
    ID varchar(36),
    --
    ENTITY_ID varchar(36),
    NAME varchar(255),
    --
    primary key (ID)
)^
-- end SEC_LOGGED_ATTR
-- begin SYS_SENDING_MESSAGE
create table SYS_SENDING_MESSAGE (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ADDRESS_TO varchar(255),
    ADDRESS_FROM varchar(255),
    CAPTION varchar(500),
    CONTENT_TEXT varchar(255),
    CONTENT_TEXT_FILE_ID varchar(36),
    STATUS integer,
    DATE_SENT timestamp,
    ATTACHMENTS_NAME varchar(255),
    DEADLINE timestamp,
    ATTEMPTS_COUNT integer,
    ATTEMPTS_MADE integer,
    EMAIL_HEADERS varchar(255),
    --
    primary key (ID)
)^
-- end SYS_SENDING_MESSAGE
-- begin SYS_REFRESH_TOKEN
create table SYS_REFRESH_TOKEN (
    ID varchar(36),
    --
    CREATE_TS timestamp,
    TOKEN_VALUE varchar(255),
    TOKEN_BYTES blob,
    AUTHENTICATION_BYTES blob,
    EXPIRY timestamp,
    USER_LOGIN varchar(255),
    --
    primary key (ID)
)^
-- end SYS_REFRESH_TOKEN
-- begin SEC_PRESENTATION
create table SEC_PRESENTATION (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    --
    COMPONENT varchar(255),
    NAME varchar(255),
    XML varchar(4000),
    USER_ID varchar(36),
    IS_AUTO_SAVE smallint,
    --
    primary key (ID)
)^
-- end SEC_PRESENTATION
-- begin SYS_SENDING_ATTACHMENT
create table SYS_SENDING_ATTACHMENT (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    MESSAGE_ID varchar(36),
    CONTENT blob,
    CONTENT_FILE_ID varchar(36),
    NAME varchar(500),
    CONTENT_ID varchar(50),
    DISPOSITION varchar(50),
    TEXT_ENCODING varchar(50),
    --
    primary key (ID)
)^
-- end SYS_SENDING_ATTACHMENT
-- begin SEC_REMEMBER_ME
create table SEC_REMEMBER_ME (
    ID varchar(36),
    --
    USER_ID varchar(36) not null,
    TOKEN varchar(32) not null,
    --
    primary key (ID)
)^
-- end SEC_REMEMBER_ME
-- begin SYS_ACCESS_TOKEN
create table SYS_ACCESS_TOKEN (
    ID varchar(36),
    --
    CREATE_TS timestamp,
    TOKEN_VALUE varchar(255),
    TOKEN_BYTES blob,
    AUTHENTICATION_KEY varchar(255),
    AUTHENTICATION_BYTES blob,
    EXPIRY timestamp,
    USER_LOGIN varchar(255),
    LOCALE varchar(255),
    REFRESH_TOKEN_VALUE varchar(255),
    --
    primary key (ID)
)^
-- end SYS_ACCESS_TOKEN
-- begin SEC_FILTER
create table SEC_FILTER (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    COMPONENT varchar(255),
    NAME varchar(255),
    CODE varchar(200),
    XML blob sub_type text,
    USER_ID varchar(36),
    GLOBAL_DEFAULT smallint,
    --
    primary key (ID)
)^
-- end SEC_FILTER
-- begin SEC_USER_ROLE
create table SEC_USER_ROLE (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    USER_ID varchar(36) not null,
    ROLE_ID varchar(36) not null,
    --
    primary key (ID)
)^
-- end SEC_USER_ROLE
-- begin SYS_SERVER
create table SYS_SERVER (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    --
    NAME varchar(255),
    IS_RUNNING smallint,
    DATA varchar(255),
    --
    primary key (ID)
)^
-- end SYS_SERVER
-- begin SEC_USER
create table SEC_USER (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    LOGIN varchar(50) not null,
    LOGIN_LC varchar(50) not null,
    PASSWORD varchar(255),
    NAME varchar(255),
    FIRST_NAME varchar(255),
    LAST_NAME varchar(255),
    MIDDLE_NAME varchar(255),
    POSITION_ varchar(255),
    EMAIL varchar(100),
    LANGUAGE_ varchar(20),
    TIME_ZONE varchar(255),
    TIME_ZONE_AUTO smallint,
    ACTIVE smallint,
    CHANGE_PASSWORD_AT_LOGON smallint,
    GROUP_ID varchar(36) not null,
    IP_MASK varchar(200),
    --
    primary key (ID)
)^
-- end SEC_USER
-- begin SEC_SCREEN_HISTORY
create table SEC_SCREEN_HISTORY (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    --
    USER_ID varchar(36),
    SUBSTITUTED_USER_ID varchar(36),
    CAPTION varchar(255),
    URL varchar(4000),
    ENTITY_ID varchar(36),
    --
    primary key (ID)
)^
-- end SEC_SCREEN_HISTORY
-- begin SYS_CATEGORY_ATTR
create table SYS_CATEGORY_ATTR (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DEFAULT_ENTITY_VALUE varchar(36),
    DEFAULT_STR_ENTITY_VALUE varchar(255),
    DEFAULT_INT_ENTITY_VALUE integer,
    DEFAULT_LONG_ENTITY_VALUE bigint,
    --
    CATEGORY_ID varchar(36) not null,
    CATEGORY_ENTITY_TYPE varchar(255),
    NAME varchar(255) not null,
    CODE varchar(50) not null,
    ENUMERATION varchar(255),
    DATA_TYPE varchar(50),
    ENTITY_CLASS varchar(255),
    ORDER_NO integer,
    SCREEN varchar(255),
    REQUIRED smallint,
    LOOKUP smallint,
    TARGET_SCREENS varchar(255),
    DEFAULT_STRING varchar(255),
    DEFAULT_INT integer,
    DEFAULT_DOUBLE double precision,
    DEFAULT_BOOLEAN smallint,
    DEFAULT_DATE timestamp,
    DEFAULT_DATE_IS_CURRENT smallint,
    WIDTH varchar(20),
    ROWS_COUNT integer,
    IS_COLLECTION smallint,
    WHERE_CLAUSE varchar(255),
    JOIN_CLAUSE varchar(255),
    FILTER_XML varchar(255),
    LOCALE_NAMES varchar(255),
    ENUMERATION_LOCALES varchar(255),
    --
    primary key (ID)
)^
-- end SYS_CATEGORY_ATTR
-- begin SEC_USER_SETTING
create table SEC_USER_SETTING (
    ID varchar(36),
    --
    USER_ID varchar(36),
    CLIENT_TYPE varchar(50),
    NAME varchar(255),
    VALUE_ blob sub_type text,
    --
    primary key (ID)
)^
-- end SEC_USER_SETTING
-- begin SEC_SESSION_ATTR
create table SEC_SESSION_ATTR (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(50),
    STR_VALUE varchar(1000),
    DATATYPE varchar(20),
    GROUP_ID varchar(36),
    --
    primary key (ID)
)^
-- end SEC_SESSION_ATTR
-- begin SYS_SCHEDULED_TASK
create table SYS_SCHEDULED_TASK (
    ID varchar(36),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DEFINED_BY varchar(50),
    BEAN_NAME varchar(255),
    METHOD_NAME varchar(255),
    CLASS_NAME varchar(255),
    SCRIPT_NAME varchar(255),
    USER_NAME varchar(255),
    IS_SINGLETON smallint,
    IS_ACTIVE smallint,
    PERIOD integer,
    TIMEOUT integer,
    START_DATE timestamp,
    CRON varchar(255),
    SCHEDULING_TYPE varchar(50),
    TIME_FRAME integer,
    START_DELAY integer,
    PERMITTED_SERVERS varchar(255),
    LOG_START smallint,
    LOG_FINISH smallint,
    LAST_START_TIME timestamp,
    LAST_START_SERVER varchar(255),
    METHOD_PARAMS varchar(255),
    DESCRIPTION varchar(1000),
    --
    primary key (ID)
)^
-- end SYS_SCHEDULED_TASK
-- begin SYS_APP_FOLDER
create table SYS_APP_FOLDER (
    FOLDER_ID varchar(36),
    FILTER_COMPONENT varchar(255),
    FILTER_XML varchar(255),
    APPLY_DEFAULT smallint,
    --
    VISIBILITY_SCRIPT varchar(200),
    QUANTITY_SCRIPT varchar(200),
    --
    primary key (FOLDER_ID)
)^
-- end SYS_APP_FOLDER
-- begin SEC_SESSION_LOG
create table SEC_SESSION_LOG (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    SESSION_ID varchar(36) not null,
    SUBSTITUTED_USER_ID varchar(36) not null,
    USER_ID varchar(36) not null,
    USER_DATA blob sub_type text,
    LAST_ACTION integer not null,
    CLIENT_INFO varchar(255),
    ADDRESS varchar(255),
    STARTED_TS timestamp,
    FINISHED_TS timestamp,
    CLIENT_TYPE varchar(50),
    SERVER_ID varchar(255),
    --
    primary key (ID)
)^
-- end SEC_SESSION_LOG
-- begin SYS_ATTR_VALUE
create table SYS_ATTR_VALUE (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ENTITY_ID varchar(36),
    STRING_ENTITY_ID varchar(255),
    INT_ENTITY_ID integer,
    LONG_ENTITY_ID bigint,
    ENTITY_VALUE varchar(36),
    STRING_ENTITY_VALUE varchar(255),
    INT_ENTITY_VALUE integer,
    LONG_ENTITY_VALUE bigint,
    --
    CATEGORY_ATTR_ID varchar(36) not null,
    CODE varchar(255),
    STRING_VALUE varchar(255),
    INTEGER_VALUE integer,
    DOUBLE_VALUE double precision,
    BOOLEAN_VALUE smallint,
    DATE_VALUE timestamp,
    PARENT_ID varchar(36),
    --
    primary key (ID)
)^
-- end SYS_ATTR_VALUE
-- begin SEC_LOCALIZED_CONSTRAINT_MSG
create table SEC_LOCALIZED_CONSTRAINT_MSG (
    ID varchar(36),
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ENTITY_NAME varchar(255) not null,
    OPERATION_TYPE varchar(50) not null,
    VALUES_ blob sub_type text,
    --
    primary key (ID)
)^
-- end SEC_LOCALIZED_CONSTRAINT_MSG

----------------------------------------------------------------------------------------------------------

create procedure newid
    returns (res varchar(36))
    as
    begin
        res = uuid_to_char(gen_uuid());
        suspend;
    end ^

----------------------------------------------------------------------------------------------------------

insert into SEC_GROUP (ID, CREATE_TS, VERSION, NAME, PARENT_ID)
values ('0fa2b1a5-1d68-4d69-9fbd-dff348347f93', CURRENT_TIMESTAMP, 0, 'Company', null)^

insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, LOGIN_LC, PASSWORD, NAME, GROUP_ID, ACTIVE)
values ('60885987-1b61-4247-94c7-dff348347f93', CURRENT_TIMESTAMP, 0, 'admin', 'admin',
'cc2229d1b8a052423d9e1c9ef0113b850086586a',
'Administrator', '0fa2b1a5-1d68-4d69-9fbd-dff348347f93', 1)^

insert into SEC_USER (ID, CREATE_TS, VERSION, LOGIN, LOGIN_LC, PASSWORD, NAME, GROUP_ID, ACTIVE)
values ('a405db59-e674-4f63-8afe-269dda788fe8', CURRENT_TIMESTAMP, 0, 'anonymous', 'anonymous', null,
'Anonymous', '0fa2b1a5-1d68-4d69-9fbd-dff348347f93', 1)^

insert into SEC_ROLE (ID, CREATE_TS, VERSION, NAME, ROLE_TYPE)
values ('0c018061-b26f-4de2-a5be-dff348347f93', CURRENT_TIMESTAMP, 0, 'Administrators', 10)^

insert into SEC_USER_ROLE (ID, CREATE_TS, VERSION, USER_ID, ROLE_ID)
values ('c838be0a-96d0-4ef4-a7c0-dff348347f93', CURRENT_TIMESTAMP, 0, '60885987-1b61-4247-94c7-dff348347f93', '0c018061-b26f-4de2-a5be-dff348347f93')^

insert into SEC_FILTER (ID, CREATE_TS, CREATED_BY, VERSION, COMPONENT, NAME, XML, USER_ID, GLOBAL_DEFAULT)
values ('b61d18cb-e79a-46f3-b16d-eaf4aebb10dd', CURRENT_TIMESTAMP, 'admin', 0, '[sec$User.browse].genericFilter', 'Search by role',
  '<?xml version="1.0" encoding="UTF-8"?><filter><and><c name="UrMxpkfMGn" class="com.haulmont.cuba.security.entity.Role" type="CUSTOM" locCaption="Role" entityAlias="u" join="join u.userRoles ur">ur.role.id = :component$genericFilter.UrMxpkfMGn32565<param name="component$genericFilter.UrMxpkfMGn32565">NULL</param></c></and></filter>',
  '60885987-1b61-4247-94c7-dff348347f93', 0)^
