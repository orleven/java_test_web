package msf;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.List;

public class MemoryBufferURLStreamHandler extends URLStreamHandler
{
    private List files = new ArrayList();

    protected URLConnection openConnection(URL uRL ) throws IOException {
        return new MemoryBufferURLConnection( uRL );
    }

    public List getFiles() {
        return this.files;
    }
}