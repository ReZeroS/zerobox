package club.qqtim.definition.support.liquibase.tag;

import java.util.List;
import java.util.Map;

/**
 * 1726542850@qq.com
 * rezeros.github.io
 * description:
 */
public abstract class Tag {

    private String name;

    private Map<String, String> attributes;

    private List<Tag> childTags;

}
