/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.qhodok.nlp.MachineTranslation.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

/**
 *
 * @author Andika Permana
 */
public final class Util {

    public static JSONObject DICTIONARY;
    public static JSONObject GRAMMAR;
    public static JSONArray REORDER_RULE;
    public static HashMap<String, HashMap<String, String>> GRAMMAR_RELATION;

    public static void init(String pathfile) throws IOException {
        JSONObject dataSource = XML.toJSONObject(read(pathfile).toLowerCase());
        DICTIONARY = dataSource.getJSONObject("word");
        GRAMMAR = new JSONObject();
        for (String classes : dataSource.getJSONObject("class").keySet()) {
            if (dataSource.getJSONObject("class").getJSONObject(classes).has("subclass")) {
                for (Object subclass : dataSource.getJSONObject("class").getJSONObject(classes).getJSONArray("subclass").getValues()) {
                    GRAMMAR.put((String) subclass, classes);
                }
            } else {
                GRAMMAR.put(classes, classes);
            }
        }
        REORDER_RULE = dataSource.getJSONObject("reorder-rule").getJSONArray("rule");
        GRAMMAR_RELATION = new HashMap<>();
        for (String key : dataSource.getJSONObject("link-grammar").keySet()) {
            String left_grammar = dataSource.getJSONObject("link-grammar").getJSONObject(key).getString("left-grammar");
            if(!GRAMMAR_RELATION.containsKey(left_grammar)){
                GRAMMAR_RELATION.put(left_grammar, new HashMap<String, String>());
            }
            GRAMMAR_RELATION.get(left_grammar).put(dataSource.getJSONObject("link-grammar").getJSONObject(key).getString("right-grammar"), key);
        }
    }

    public static String read(String path) throws FileNotFoundException, IOException {
        File file = new File(path);
        long l = file.length();
        byte[] b = new byte[(int) l];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(b);
        return new String(b);
    }

    public static void write(String Url, String Teks) {
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
