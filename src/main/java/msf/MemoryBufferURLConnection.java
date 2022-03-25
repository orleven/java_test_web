package msf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemoryBufferURLConnection extends URLConnection {

    private static List files;
    private final byte[] data;
    private final String contentType;

    protected MemoryBufferURLConnection( URL uRL ){
        super( uRL );
        String string = uRL.getFile();
        int n = string.indexOf( 47 );
        List list = files;
        synchronized (list){
            this.data = (byte[]) files.get( Integer.parseInt( string.substring( 0, n ) ) );
        }
        this.contentType = string.substring( n + 1 );
    }

    static {
        files = new ArrayList();
        try {
            Map map;
            Field field;
            try {
                field = URL.class.getDeclaredField( "handlers" );
            }
            catch (NoSuchFieldException noSuchFieldException ) {
                try{
                    field = URL.class.getDeclaredField( "ph_cache" );
                }
                catch ( NoSuchFieldException noSuchFieldException2 ){
                    throw noSuchFieldException;
                }
            }
            field.setAccessible( true );
            Map map2 = map = (Map) field.get( null );
            synchronized ( map2 ){
                Object object;
                if ( map.containsKey( "metasploitmembuff" )) {
                    object = map.get( "metasploitmembuff" );
                }
                else {
                    object = new MemoryBufferURLStreamHandler();
                    map.put( "metasploitmembuff", object );
                }
                files = (List) object.getClass().getMethod( "getFiles", new Class[0] ).invoke(object, new Object[0] );
            }
        }
        catch ( Exception exception ) {
            throw new RuntimeException( exception.toString() );
        }
    }

    public void connect() throws IOException {
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream( this.data );
    }

    public static URL createURL(byte[] arrby, String string)throws MalformedURLException {
        List list = files;
        synchronized (list) {
            files.add( arrby );
            return new URL( "metasploitmembuff", "", "" + ( files.size() - 1 ) + "/" + string );
        }
    }

    public int getContentLength(){
        return this.data.length;
    }

    public String getContentType(){
        return this.contentType;
    }
}