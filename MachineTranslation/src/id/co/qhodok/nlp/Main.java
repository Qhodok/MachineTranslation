/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.qhodok.nlp;

import id.co.qhodok.nlp.MachinTranslation.MachineTranslation;
import id.co.qhodok.nlp.MachinTranslation.model.LanguageModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;
import org.json.XML;

/**
 *
 * @author Andika
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        /*String source = "i am andika putra permana\n i live in jakarta\ni am a programer\ni will go with her";
         String target = "saya adalah andika putra permana\n saya tinggal di jakarta\nsaya adalah seorang programer\nsaya akan pergi dengan dia";
         HashMap <String,List<String>> dict = new HashMap<>();
         dict.put("i", new ArrayList<String>());
         dict.get("i").add("saya");
         dict.put("am", new ArrayList<String>());
         dict.get("am").add("seorang");
         dict.put("a", new ArrayList<String>());
         dict.get("a").add("seorang");
         dict.put("andika", new ArrayList<String>());
         dict.get("andika").add("andika");
         dict.put("putra", new ArrayList<String>());
         dict.get("putra").add("putra");
         dict.put("permana", new ArrayList<String>());
         dict.get("permana").add("permana");
         dict.put("live", new ArrayList<String>());
         dict.get("live").add("tinggal");
         dict.put("in", new ArrayList<String>());
         dict.get("in").add("di");
         dict.put("jakarta", new ArrayList<String>());
         dict.get("jakarta").add("jakarta");
         dict.put("programer", new ArrayList<String>());
         dict.get("programer").add("programer");
         dict.put("will", new ArrayList<String>());
         dict.get("will").add("akan");
         dict.put("go", new ArrayList<String>());
         dict.get("go").add("pergi");
         dict.put("with", new ArrayList<String>());
         dict.get("with").add("dengan");
         dict.put("her", new ArrayList<String>());
         dict.get("her").add("dia");
         MachineTranslation machineTranslation = new MachineTranslation();
         machineTranslation.training(source, target, dict, 2);
         machineTranslation.translation("i will go with her");*/
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
