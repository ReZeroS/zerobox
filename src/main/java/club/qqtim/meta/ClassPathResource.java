package club.qqtim.meta;


import club.qqtim.util.reflect.ClassUtils;
import club.qqtim.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ClassPathResource implements Resource {

    private static final String CLASSPATH_ADDRESS = "classpath:";

    private final String path;
    private final ClassLoader classLoader;


    public ClassPathResource(String path) {
        this(path, null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        this.path = StringUtils.removeStart(path, CLASSPATH_ADDRESS);
        this.classLoader = (classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader();
    }


    @Override
    public InputStream getInputStream() throws IOException {
        InputStream inputStream = this.classLoader.getResourceAsStream(this.path);
        if (inputStream == null) {
            throw new FileNotFoundException(path + " can not be opened");
        }
        return inputStream;
    }


    @Override
    public String getDescription() {
        return this.path;
    }
}
