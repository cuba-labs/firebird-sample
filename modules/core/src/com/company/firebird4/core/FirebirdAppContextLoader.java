package com.company.firebird4.core;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.AppContextLoader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FirebirdAppContextLoader extends AppContextLoader {

    private final Logger log = LoggerFactory.getLogger(FirebirdAppContextLoader.class);

    private static final String XMLNS = "http://xmlns.jcp.org/xml/ns/persistence/orm";
    private static final String SCHEMA_LOCATION = XMLNS + " http://xmlns.jcp.org/xml/ns/persistence/orm_2_1.xsd";
    private static final String PERSISTENCE_VER = "2.1";

    private final static List<String> ENTITY_CLASS_NAMES = Arrays.asList(
            "com.haulmont.cuba.core.entity.Config",
            "com.haulmont.cuba.security.entity.Permission",
            "com.haulmont.cuba.security.entity.UserSetting");

    @Override
    protected void beforeInitAppContext() {
        super.beforeInitAppContext();

        String disableOrmGenProp = AppContext.getProperty("cuba.disableOrmXmlGeneration");
        if (Boolean.parseBoolean(disableOrmGenProp)) {
            return;
        }

        String dataDir = AppContext.getProperty("cuba.dataDir");
        //TODO it has not processed *persistence.xml from other data stores other than 'main'
        File persistenceFile = new File(dataDir, "persistence.xml");
        if (!persistenceFile.exists()) {
            return;
        }

        File ormFile = new File(dataDir, "orm.xml");
        Document ormDoc;
        if (ormFile.exists()) {
            ormDoc = Dom4j.readDocument(ormFile);
        } else {
            ormDoc = DocumentHelper.createDocument();
            Element rootEl = ormDoc.addElement("entity-mappings", XMLNS);
            Namespace xsi = new Namespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            rootEl.add(xsi);
            rootEl.addAttribute(new QName("schemaLocation", xsi), SCHEMA_LOCATION);
            rootEl.addAttribute("version", PERSISTENCE_VER);
        }

        Element rootEl = ormDoc.getRootElement();
        boolean ormXmlModified = false;
        for (String className : ENTITY_CLASS_NAMES) {
            Class<Object> aClass = ReflectionHelper.getClass(className);
            Element entityEl = Dom4j.elements(rootEl, "entity").stream()
                    .filter(eEl -> {
                        String classValue = eEl.attributeValue("class");
                        return Objects.equals(classValue, className);
                    }).findFirst().orElse(null);
            if (entityEl == null) {
                entityEl = rootEl.addElement("entity", XMLNS);
                entityEl.addAttribute("class", aClass.getName());
                entityEl.addAttribute("name", aClass.getAnnotation(Entity.class).name());
            }

            Element attributesEl = entityEl.element("attributes");
            if (attributesEl == null) {
                attributesEl = entityEl.addElement("attributes", XMLNS);
            }

            if (processedValueColumn(attributesEl, aClass)) {
                ormXmlModified = true;
            }
        }

        if (ormXmlModified) {
            writeDocument(ormDoc, ormFile);
        }

        Document persistenceDoc = Dom4j.readDocument(persistenceFile);
        Element rootElem = persistenceDoc.getRootElement();
        Element puElem = findPersistenceUnitElement(rootElem);
        if (puElem != null) {
            puElem.addElement("mapping-file").setText("orm.xml");
            writeDocument(persistenceDoc, persistenceFile);
        }
    }

    @Nullable
    private Element findPersistenceUnitElement(Element rootElem) {
        List<Element> puList = Dom4j.elements(rootElem, "persistence-unit");
        if (puList.size() == 1) {
            return puList.get(0);
        } else {
            for (Element element : puList) {
                if ("cuba".equals(element.attributeValue("name"))) {
                    return element;
                }
            }
        }
        return null;
    }

    private boolean processedValueColumn(Element attributesEl, Class aClass) {
        for (Element basicEl : Dom4j.elements(attributesEl, "basic")) {
            Element columnEl = basicEl.element("column");
            String columnName = columnEl.attributeValue("name");
            if (StringUtils.equalsIgnoreCase("value", columnName)) {
                columnEl.addAttribute("name", columnName + "_");
                return true;
            }
            if (StringUtils.equalsIgnoreCase("value_", columnName)) {
                return false;
            }
        }
        Field columnField = Arrays.stream(aClass.getDeclaredFields())
                .filter(field -> {
                    Column columnAn = field.getAnnotation(Column.class);
                    return columnAn != null && StringUtils.equalsIgnoreCase("value", columnAn.name());
                }).findFirst().orElse(null);

        if (columnField != null) {
            Column columnAn = columnField.getAnnotation(Column.class);

            Element basicEl = attributesEl.addElement("basic");
            basicEl.addAttribute("name", columnField.getName());
            Element columnEl = basicEl.addElement("column");
            columnEl.addAttribute("name", columnAn.name() + "_");
            columnEl.addAttribute("unique", String.valueOf(columnAn.unique()));
            columnEl.addAttribute("nullable", String.valueOf(columnAn.nullable()));
            columnEl.addAttribute("insertable", String.valueOf(columnAn.insertable()));
            columnEl.addAttribute("updatable", String.valueOf(columnAn.updatable()));
            columnEl.addAttribute("column-definition", columnAn.columnDefinition());
            columnEl.addAttribute("table", columnAn.table());
            columnEl.addAttribute("length", String.valueOf(columnAn.length()));
            columnEl.addAttribute("precision", String.valueOf(columnAn.precision()));
            columnEl.addAttribute("scale", String.valueOf(columnAn.scale()));
            return true;
        }
        return false;
    }

    private void writeDocument(Document doc, File file) {
        log.info("Creating file " + file);

        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            Dom4j.writeDocument(doc, true, os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }
}
