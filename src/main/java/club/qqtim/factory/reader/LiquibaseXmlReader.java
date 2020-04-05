package club.qqtim.factory.reader;

import club.qqtim.definition.support.liquibase.ChangeSet;
import club.qqtim.definition.support.liquibase.tag.CreateTable;
import club.qqtim.definition.support.liquibase.tag.Tag;
import club.qqtim.definition.support.liquibase.tag.TagConstants;
import club.qqtim.factory.AbstractFactory;
import club.qqtim.factory.ProcessException;
import club.qqtim.factory.support.LiquibaseFactory;
import club.qqtim.meta.ClassPathResource;
import club.qqtim.meta.Resource;
import club.qqtim.util.StringUtils;
import club.qqtim.util.Xml2ObjectParseUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LiquibaseXmlReader implements Reader {


    private Resource resource;

    private LiquibaseFactory registry;

    private static final String DEFAULT_NAMESPACE_URI = "http://www.liquibase.org/xml/ns/dbchangelog";

    private static final String INCLUDE_TAG = "include";

    private static final String CHANGE_SET_TAG = "changeSet";

    private static final String FILE_ATTRIBUTE = "file";

    private static final String ID_ATTRIBUTE = "id";

    private static final String AUTHOR_ATTRIBUTE = "author";


    @Override
    public void loadResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void registryFactory(AbstractFactory abstractFactory) {
        this.registry = (LiquibaseFactory) abstractFactory;
    }

    @Override
    public void loadChangeSets() {
        try (InputStream inputStream = resource.getInputStream()) {
            SAXReader saxReader = new SAXReader();
            Document doc = saxReader.read(inputStream);

            Element root = doc.getRootElement();
            Iterator iterator = root.elementIterator();

            while (iterator.hasNext()) {
                Element element = (Element) iterator.next();
                String namespaceUri = element.getNamespaceURI();
                if (this.isDefaultNamespace(namespaceUri)) {
                    //normal changeSet
                    parseDefaultElement(element);
                } else {
                    throw new ProcessException("Parse xml element error");
                }
            }
        } catch (Exception e) {
            throw new ProcessException("IO Exception parsing XML document", e);
        }
    }

    private void parseDefaultElement(Element element) {
        // if element is include, do scan;
        switch (element.getName()) {
            case INCLUDE_TAG:
                parsePathInclude(element.attributeValue(FILE_ATTRIBUTE));
                break;
            case CHANGE_SET_TAG:
                parseChangeSet(element);
                break;
            default: break;
        }
    }

    private void parseChangeSet(Element element) {
        ChangeSet changeSet = Xml2ObjectParseUtil.parse(element, ChangeSet.class);
        if (changeSet == null) {
            throw new ProcessException("Parse ChangSet error, please check the syntax");
        }
        this.registry.registerChangeSet(element.attributeValue(ID_ATTRIBUTE), changeSet);
    }

    private List<Tag> parseTags(Element root) {
        Iterator iterator = root.elementIterator();
        List<Tag> tags = new ArrayList<>();
        while (iterator.hasNext()) {
            Element element = (Element) iterator.next();
            if (TagConstants.CREATE_TABLE.equals(element.getName())) {
                CreateTable createTable = Xml2ObjectParseUtil.parse(element, CreateTable.class);

            }

        }


        return new ArrayList<>();
    }

    private void parsePathInclude(String path) {
        // path format
        // formatPath(path);
        //
        List<Element> elements = doScan(path);
        elements.forEach(this::parseDefaultElement);
    }

    private List<Element> doScan(String path) {
        List<Element> elements = new ArrayList<>();
        Resource resource = new ClassPathResource(path);
        try (InputStream inputStream = resource.getInputStream()){
            SAXReader saxReader = new SAXReader();
            Document doc = saxReader.read(inputStream);
            Element root = doc.getRootElement();
            Iterator iterator = root.elementIterator();
            while (iterator.hasNext()) {
                Element element = (Element) iterator.next();
                elements.add(element);
            }
            return elements;
        } catch (IOException | DocumentException e) {
            throw new ProcessException("IO Exception parsing XML document", e);
        }
    }

    private boolean isDefaultNamespace(String namespaceUri) {
        return StringUtils.isNotEmpty(namespaceUri) && DEFAULT_NAMESPACE_URI.equals(namespaceUri);
    }


}
