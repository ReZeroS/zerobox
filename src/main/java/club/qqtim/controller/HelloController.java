package club.qqtim.controller;

import club.qqtim.dimension.InputUnit;
import club.qqtim.dimension.SubUnit;
import club.qqtim.util.PDFGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
public class HelloController {

    @GetMapping("/h")
    public InputUnit export(){

        return new InputUnit(1L, "Li", 8, new SubUnit(1), Arrays.asList("001", "002"));
    }

    @GetMapping(value = "/pdf", produces =
            MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> pdfReport()
            throws IOException {
        final List<InputUnit> inputUnits = Collections.singletonList(new InputUnit(1L, "Li", 8, new SubUnit(1), Arrays.asList("001", "002")));
        final List<String> configList = Arrays.asList("id", "name", "age");
        ByteArrayInputStream bis = PDFGenerator.exportPdf(inputUnits, configList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=employees.pdf");
        return ResponseEntity.ok().headers(headers).contentType
                        (MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

}
