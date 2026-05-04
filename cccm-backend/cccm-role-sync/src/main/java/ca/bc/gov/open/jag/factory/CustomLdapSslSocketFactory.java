package ca.bc.gov.open.jag.factory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class CustomLdapSslSocketFactory extends SSLSocketFactory
{
    private final SSLSocketFactory sslSocketFactory;

    private static volatile CustomLdapSslSocketFactory singletonCustLdapSslSockFact;


    private CustomLdapSslSocketFactory() throws KeyManagementException, KeyStoreException, FileNotFoundException, NoSuchAlgorithmException, CertificateException, IOException
    {
        sslSocketFactory = loadTrustStoreProgrammatically();
    }


    private static CustomLdapSslSocketFactory getSingletonInstance() throws KeyManagementException, KeyStoreException, FileNotFoundException, NoSuchAlgorithmException, CertificateException, IOException
    {
        if(CustomLdapSslSocketFactory.singletonCustLdapSslSockFact == null)
        {
            synchronized(CustomLdapSslSocketFactory.class)
            {
                if(CustomLdapSslSocketFactory.singletonCustLdapSslSockFact == null)
                {
                    CustomLdapSslSocketFactory.singletonCustLdapSslSockFact = new CustomLdapSslSocketFactory();
                }
            }
        }

        return CustomLdapSslSocketFactory.singletonCustLdapSslSockFact;
    }




    public static SocketFactory getDefault() //this method is called by Ldap implementations to create the custom SSL socket factory. See: https://docs.oracle.com/javase/jndi/tutorial/ldap/security/ssl.html
    {
        /*
        There are times when you need to have more control over the SSL sockets, or sockets in general, used by the LDAP service provider.
        To set the socket factory implementation used by the LDAP service provider, set the "java.naming.ldap.factory.socket" property to the
        fully qualified class name of the socket factory.
        This class must extend the javax.net.SocketFactory abstract class and provide an implementation of the getDefault() method that
        returns an instance of the custom socket factory.
        See:
        https://docs.oracle.com/javase/jndi/tutorial/ldap/security/ssl.html
        */
        CustomLdapSslSocketFactory custLdapSslSockFact = null;

        try
        {
            //custLdapSslSockFact = new CustomLdapSslSocketFactory(); //returns a new instance each time
            custLdapSslSockFact = CustomLdapSslSocketFactory.getSingletonInstance(); //returns the same instance each time (singleton pattern)
        }
        catch(Exception e)
        {
            throw new RuntimeException("Failed create CustomSslSocketFactory. Exception: " + e.getClass().getSimpleName() + ". Reason: " + e.getMessage(), e);
        }

        return custLdapSslSockFact;
    }



    private SSLSocketFactory loadTrustStoreProgrammatically() throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, KeyManagementException, CertificateException
    {
        //Now, reference the custom user-defined system properties defined in your ldap query method above.

        String trustStoreType = System.getProperty("custom.ldap.truststore.type");
        String trustStoreLoc = System.getProperty("custom.ldap.truststore.loc");
        char[] trustStorePasswordCharArr = System.getProperty("custom.ldap.truststore.password").toCharArray();
        String sslContextProtocol = System.getProperty("custom.ldap.ssl.protocol");

        KeyStore trustStore = KeyStore.getInstance(trustStoreType);

        try(BufferedInputStream bisTrustStore = new BufferedInputStream(new FileInputStream(trustStoreLoc)))
        {
            trustStore.load(bisTrustStore, trustStorePasswordCharArr); // if your does not have a password specify null
        }

        // initialize a trust manager factory with the trusted store
        TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustFactory.init(trustStore);

        // get the trust managers from the factory
        TrustManager[] trustManagers = trustFactory.getTrustManagers();

        // initialize an ssl context to use these managers
        SSLContext sslContext = SSLContext.getInstance(sslContextProtocol); //.getInstance("SSL"); or TLS, etc.
        sslContext.init(null, trustManagers, null);

        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        return sslSocketFactory;
    }





    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException
    {
        return sslSocketFactory.createSocket(s, host, port, autoClose);
    }

    @Override
    public String[] getDefaultCipherSuites()
    {
        return sslSocketFactory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites()
    {
        return sslSocketFactory.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException
    {
        return sslSocketFactory.createSocket(host, port);
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException
    {
        return sslSocketFactory.createSocket(host, port);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException
    {
        return sslSocketFactory.createSocket(localHost, port, localHost, localPort);
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException
    {
        return sslSocketFactory.createSocket(address, port, localAddress, localPort);
    }
}
