package club.qqtim.definition.support.liquibase;

import club.qqtim.definition.support.liquibase.tag.CreateTable;
import club.qqtim.definition.support.liquibase.tag.Tag;
import lombok.Data;

import java.util.List;


@Data
public class ChangeSet {

    private String id;

    private String author;

    private List<CreateTable> createTable;

}
