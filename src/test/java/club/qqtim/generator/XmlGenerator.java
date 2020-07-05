package club.qqtim.generator;

import club.qqtim.dto.IndexConstraint;
import club.qqtim.factory.ProcessException;
import club.qqtim.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 1726542850@qq.com
 * rezeros.github.io
 * description:
 */
@Slf4j
public class XmlGenerator {

    private static final String ROOT_ELEMENT_NAME = "root";

    private static final String CHANGE_SET_NAME = "changeSet";

    private static final String MODULE_NAME = "wbc_";

    private static final String INDEX_SUFFIX = "_n";

    private final Element root;

    private static final String TIME_PREFIX;

    static {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        TIME_PREFIX = formatter.format(new Date());
    }

    public XmlGenerator(){
        Document document = DocumentHelper.createDocument();
        this.root = document.addElement(ROOT_ELEMENT_NAME);
    }



    public void generateXml(List<IndexConstraint> list) {
        log.debug(list.toString());
        for (IndexConstraint item : list) {
            String tableName = item.getTableName();
            String indexNamePrefix = item.getTableName().substring(MODULE_NAME.length());

            if (StringUtils.isNotEmpty(item.getFirstIndex())) {
                Element changeSet = this.root.addElement(CHANGE_SET_NAME)
                        .addAttribute("author", "jie.li")
                        .addAttribute("id", MODULE_NAME + TIME_PREFIX + String.format("%03d", item.getIdxNo()));

                changeSet.addElement("createIndex")
                        .addAttribute("tableName", tableName)
                        .addAttribute("indexName", indexNamePrefix + INDEX_SUFFIX +  1);
            }

            if (StringUtils.isNotEmpty(item.getSecondIndex())) {
                Element changeSet = this.root.addElement(CHANGE_SET_NAME)
                        .addAttribute("author", "jie.li")
                        .addAttribute("id", MODULE_NAME + TIME_PREFIX + String.format("1%02d", item.getIdxNo()));

                changeSet.addElement("createIndex")
                        .addAttribute("tableName", tableName)
                        .addAttribute("indexName", indexNamePrefix + INDEX_SUFFIX +  1);
            }

            if (StringUtils.isNotEmpty(item.getThirdIndex())) {
                Element changeSet = this.root.addElement(CHANGE_SET_NAME)
                        .addAttribute("author", "jie.li")
                        .addAttribute("id", MODULE_NAME + TIME_PREFIX + String.format("2%02d", item.getIdxNo()));

                changeSet.addElement("createIndex")
                        .addAttribute("tableName", tableName)
                        .addAttribute("indexName", indexNamePrefix + INDEX_SUFFIX +  1);
            }

            if (StringUtils.isNotEmpty(item.getFourthIndex())) {
                Element changeSet = this.root.addElement(CHANGE_SET_NAME)
                        .addAttribute("author", "jie.li")
                        .addAttribute("id", MODULE_NAME + TIME_PREFIX + String.format("3%02d", item.getIdxNo()));

                changeSet.addElement("createIndex")
                        .addAttribute("tableName", tableName)
                        .addAttribute("indexName", indexNamePrefix + INDEX_SUFFIX +  1);
            }

            if (StringUtils.isNotEmpty(item.getFifthIndex())) {
                Element changeSet = this.root.addElement(CHANGE_SET_NAME)
                        .addAttribute("author", "jie.li")
                        .addAttribute("id", MODULE_NAME + TIME_PREFIX + String.format("4%02d", item.getIdxNo()));

                changeSet.addElement("createIndex")
                        .addAttribute("tableName", tableName)
                        .addAttribute("indexName", indexNamePrefix + INDEX_SUFFIX +  1);
            }
        }
    }


    public void persistXmlFile() {
        try (FileWriter out = new FileWriter("wbc.xml")){
            root.write(out);
        } catch (IOException e) {
            throw new ProcessException("Persist xml file failed");
        }
    }

}
