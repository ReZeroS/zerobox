package club.qqtim.listener;

import club.qqtim.dto.IndexConstraint;
import club.qqtim.generator.XmlGenerator;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 1726542850@qq.com
 * rezeros.github.io
 * description:
 */
@Slf4j
public class IndexConstraintListener extends AnalysisEventListener<IndexConstraint> {

    private static final int BATCH_COUNT = 10;

    List<IndexConstraint> list = new ArrayList<>(16);

    private final XmlGenerator xmlGenerator;

    public IndexConstraintListener(XmlGenerator xmlGenerator){
        this.xmlGenerator = xmlGenerator;
    }

    @Override
    public void invoke(IndexConstraint data, AnalysisContext analysisContext) {
        list.add(data);
        // reach BATCH_COUNT point, to prevent [Out Of Memory]
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // clear the list after save data
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // ensure the rest of data also could be persisted
        saveData();
        xmlGenerator.persistXmlFile();
        log.info("All analysed!");
    }

    private void saveData() {
        log.info("{} line persisting...", list.size());
        xmlGenerator.generateXml(list);
        log.info("persisted successfully!");
    }



}
