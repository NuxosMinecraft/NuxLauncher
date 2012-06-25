package fr.nuxos.minecraft.NuxLauncher.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.security.PublicKey;

import javax.net.ssl.HttpsURLConnection;

import fr.nuxos.minecraft.NuxLauncher.minecraft.MinecraftLogin;

public class Network {
    
    private Network() {
    }
    
    public static String executePostSSL(String targetURL, String urlParameters, String CertifName)
    {
    	
      HttpsURLConnection connection = null;
      
      try {
        URL url = new URL(targetURL);
        
        connection = (HttpsURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
        //connection.setRequestProperty("Content-Language", "en-US"); useless
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);   
        connection.connect();
        
        java.security.cert.Certificate[] certs = connection.getServerCertificates();
   
        byte[] bytes = new byte[294];
        DataInputStream minecraftCert = new DataInputStream(Network.class.getResourceAsStream(CertifName + ".key"));
        minecraftCert.readFully(bytes);
        minecraftCert.close();
   
        java.security.cert.Certificate remoteCert = certs[0];
        PublicKey remoteCertPublicKey = remoteCert.getPublicKey();
        byte[] data = remoteCertPublicKey.getEncoded();
   
        for (int i = 0; i < data.length; i++) {
          if (data[i] == bytes[i]) continue; throw new RuntimeException("Error : key mismatch !");
        }
   
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
   
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
   
        StringBuffer response = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null)
        {
          response.append(line);
          response.append('\r');
        }
        rd.close();
   
        String str1 = response.toString();
        return str1;
      }
      catch (Exception e)
      {
        e.printStackTrace();
        return null;
      }
      finally
      {
        if (connection != null)
          connection.disconnect();
      }
     }

}