/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

/**
 *
 * @author Andika Permana
 */
public class Xml2JSON {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String xmlData = Read("source.xml");
        JSONObject xml = XML.toJSONObject(xmlData);
        //System.out.println(xml);
        //System.out.println(XML.toString(xml));
        //Write("test.xml", XML.toString(xml));
        JSONObject test = new JSONObject();
        test.put("nilai", new JSONArray());
        for(int i = 0;i<10;i++){
            test.getJSONArray("nilai").put(i);
        }
        System.out.println(XML.toString(test));
    }
    public static String Read(String path) throws FileNotFoundException, IOException {
        File file = new File(path);
        long l = file.length();
        byte[] b = new byte[(int) l];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(b);
        return new String(b);
    }
    
    public static void Write(String Url, String Teks) {
        FileWriter writer;
        try {
            writer = new FileWriter(Url);
            writer.write(Teks);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
