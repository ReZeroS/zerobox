package club.qqtim.version;

import club.qqtim.dto.IndexConstraint;
import club.qqtim.generator.XmlGenerator;
import club.qqtim.listener.IndexConstraintListener;
import club.qqtim.meta.ClassPathResource;
import club.qqtim.meta.Resource;
import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * 1726542850@qq.com
 * rezeros.github.io
 * description:
 */
@Slf4j
public class V1Test {

    @Test
    public void testReadExcel() {
        Resource wbcResource = new ClassPathResource("workbench.xlsx");
        try (InputStream inputStream = wbcResource.getInputStream()) {
            EasyExcel.read(
                    inputStream,
                    IndexConstraint.class,
                    new IndexConstraintListener(new XmlGenerator())).sheet().doRead();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }

}
