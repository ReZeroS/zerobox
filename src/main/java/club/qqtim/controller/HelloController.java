package club.qqtim.controller;

import club.qqtim.aspect.AClass;
import club.qqtim.dimension.InputUnit;
import club.qqtim.dimension.SubUnit;
import club.qqtim.model.UserObjective;
import club.qqtim.util.PDFGenerator;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;

@RestController
public class HelloController {


    @Autowired
    private AClass AClass;


    @GetMapping("/h")
    public InputUnit export(){
        AClass.fill();
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

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @GetMapping("/es")
    public ResponseEntity<List<UserObjective>> esList() throws IOException {

        final SearchResponse<UserObjective> search = elasticsearchClient.search(s -> s.index("objective").query(q ->
                    q.term(t -> t.field("periodId").value(v -> v.longValue(1L)))
                ), UserObjective.class);

        return ResponseEntity.ok(search.hits().hits().stream().map(Hit::source).collect(Collectors.toList()));
    }

}
