package club.qqtim.meta;

import java.io.IOException;
import java.io.InputStream;

public interface Resource {

    /**
     * @return input stream of the resource
     * @throws IOException
     */
    InputStream getInputStream() throws IOException;

    /**
     * @return path of the resource
     */
    String getDescription();

}
