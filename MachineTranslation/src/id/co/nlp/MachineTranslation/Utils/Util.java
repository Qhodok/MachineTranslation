/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.nlp.MachineTranslation.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
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

    /**
     * @param pathfile lokasi dari kamus berada
     * @throws IOException
     *
     * prosedure untuk menginisiasi atribut atribut yg digunakan
     */
    public static void init(String pathfile) throws IOException {
        JSONObject dataSource = XML.toJSONObject(reading(pathfile).toLowerCase());
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
            if (!GRAMMAR_RELATION.containsKey(left_grammar)) {
                GRAMMAR_RELATION.put(left_grammar, new HashMap<String, String>());
            }
            //System.out.println(left_grammar+" "+dataSource.getJSONObject("link-grammar").getJSONObject(key).getString("right-grammar"));
            GRAMMAR_RELATION.get(left_grammar).put(dataSource.getJSONObject("link-grammar").getJSONObject(key).getString("right-grammar"), key);
        }
    }

    /**
     *
     * @param path lokasi dari file
     * @return isi file
     * @throws FileNotFoundException
     * @throws IOException
     *
     * fuction yg berfungsi untuk membaca sebuah file
     */
    public static String reading(String path) throws FileNotFoundException, IOException {
        File file = new File(path);
        long l = file.length();
        byte[] b = new byte[(int) l];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(b);
        return new String(b);
    }

    /**
     *
     * @param pathfile lokasi penyimpanan
     * @param text text yang akan disimpan
     */
    public static void writing(String pathfile, String text) {
        FileWriter writer;
        try {
            writer = new FileWriter(pathfile);
            writer.write(text);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param pathfile lokasi data berada
     * @return hasil converting dari byte ke object
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     *
     * function yg berfungsi untuk mengkonvert deretan byte menjadi sebuah
     * object
     */
    public static Object deserializing(String pathfile) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(pathfile);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        Object object = in.readObject();
        in.close();
        fileIn.close();
        return object;
    }

    /**
     *
     * @param pathfile lokasi penyimpanan
     * @param object object yg akan dikonvert ke byte untuk disimpan
     * @throws FileNotFoundException
     * @throws IOException
     *
     * proses dimana state daripada objek tersebut dapat kita simpan menjadi
     * bentuk deretan byte
     */
    public static void serializing(String pathfile, Object object) throws FileNotFoundException, IOException {
        FileOutputStream fileOut = new FileOutputStream(pathfile);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(object);
        out.close();
        fileOut.close();
    }
}
