/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.nlp.MachineTranslation.model;

import id.co.nlp.MachineTranslation.Utils.Util;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andika Permana
 */
public class LinkGrammar {

    public List<Grammar> phrases;

    public LinkGrammar() {
        this.phrases = new ArrayList<>();
    }

    /**
     *
     * @param sentence kalimat yg akan ditambahkan keterangan katanya
     * @return function yg berfungsi untuk menambahkan keterang kata pada sebuah
     * string
     */
    public static LinkGrammar initLinkGrammar(String sentence) {
        LinkGrammar linkGrammar = new LinkGrammar();
        for (String token : sentence.split("\\s+")) {
            linkGrammar.phrases.add(new Grammar(token, "kb"));
        }
        return linkGrammar;
    }

    /**
     *
     * @param tokens kalimat yg akan ditambahkan keterangan katanya
     * @return function yg berfungsi untuk menambahkan keterang kata pada sebuah
     * string
     */
    public static LinkGrammar initLinkGrammar(String[] tokens) {
        LinkGrammar linkGrammar = new LinkGrammar();
        for (String token : tokens) {
            linkGrammar.phrases.add(new Grammar(token, "kb"));
        }
        return linkGrammar;
    }

    /**
     *
     * @param sentence kalimat yg akan ditambahkan hubungan antar kata
     * @return function yg berfungsi untuk membuat hubungan atau aturan dari
     * kata kata yg berdekatan
     */
    public LinkGrammar generateLinkGrammar(String sentence) {
        LinkGrammar linkGrammar = this.generateLinkGrammar(LinkGrammar.initLinkGrammar(sentence), 0);
        int size = linkGrammar.phrases.size();
        while (true) {
            linkGrammar = this.generateLinkGrammar(linkGrammar, 0);
            if (size == linkGrammar.phrases.size()) {
                break;
            }
        }
        return linkGrammar;
    }

    /**
     *
     * @param linkGrammar
     * @param index nomor urutan kata yg akan diprosess
     * @return function yg berfungsi untuk membuat hubungan atau aturan dari
     * kata kata yg berdekatan
     *
     */
    protected LinkGrammar generateLinkGrammar(LinkGrammar linkGrammar, int index) {
        if (index < linkGrammar.phrases.size() - 2) {
            linkGrammar = this.generateLinkGrammar(linkGrammar, index + 1);
        }
        if (linkGrammar.phrases.size() > 1) {
            boolean found = false;
            String connectionClass = null;
            for (Object leftWordClass : linkGrammar.phrases.get(index).classes.getValues()) {
                for (Object rightWordClass : linkGrammar.phrases.get(index + 1).classes.getValues()) {
                    connectionClass = checkConnection((String) leftWordClass, (String) rightWordClass);
                    if (connectionClass != null) {
                        Grammar phrase = new Grammar("", connectionClass);
                        phrase.leftGrammar = linkGrammar.phrases.remove(index);
                        phrase.rightGrammar = linkGrammar.phrases.remove(index);
                        linkGrammar.phrases.add(index, phrase);
                        found = true;
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }
        }
        return linkGrammar;
    }

    /**
     *
     * @param left_class kata pertama
     * @param right_class kata setelahnya
     * @return mencari hungungan dari 2 kata. jika 2 kata itu memiliki hungan
     * maka, function akan mengembalikan nama hubungan kata tersebut
     */
    protected String checkConnection(String left_class, String right_class) {
        //System.out.print("check "+left_class+" == "+right_class+" ");
        left_class = Util.GRAMMAR.getString(left_class);
        right_class = Util.GRAMMAR.getString(right_class);
        if (Util.GRAMMAR_RELATION.containsKey(left_class) && Util.GRAMMAR_RELATION.get(left_class).containsKey(right_class)) {
            //System.out.println("get "+Util.GRAMMAR_RELATION.get(left_class).get(right_class));
            return Util.GRAMMAR_RELATION.get(left_class).get(right_class);
        } else {
            //System.out.println("empty");
            return null;
        }
    }

    /**
     *
     * @param linkGrammar
     * @return konversi dari linkgramer kedalam bentuk single string
     */
    public String toString(LinkGrammar linkGrammar) {
        String sentence = "";
        for (Grammar phrase : linkGrammar.phrases) {
            if (phrase.leftGrammar == null) {
                sentence += " " + phrase.caption + " ";
            } else {
                sentence += " " + this.toString(phrase.leftGrammar) + " " + this.toString(phrase.rightGrammar);
            }
        }
        return sentence.replaceAll("\\s+", " ").trim();
    }

    /**
     *
     * @param phrase
     * @return konversi dari grammar kedalam bentuk single string
     */
    public String toString(Grammar phrase) {
        String sentence = "";
        if (phrase.leftGrammar == null) {
            sentence += " " + phrase.caption + " ";
        } else {
            sentence += " " + this.toString(phrase.leftGrammar) + " " + this.toString(phrase.rightGrammar);
        }
        return sentence.replaceAll("\\s+", " ").trim();
    }
}
