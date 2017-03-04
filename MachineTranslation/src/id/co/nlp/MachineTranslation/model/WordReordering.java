/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.nlp.MachineTranslation.model;

import id.co.nlp.MachineTranslation.Utils.Util;

/**
 *
 * @author Andika Permana
 */
public class WordReordering {

    public WordReordering() {

    }
    /**
     * 
     * @param sentence kalimat
     * @return kalimat
     * berfungsi untuk membalikan susunan kata sesuai dengan aturannya
     * dalam aturan bahasa inggri terkadang ada deret kata yg ditukar posisinya 
     * seperti "my book" yg artinya "buku saya" posisi antara my(saya) dan book(buku) terbalik
     * maka akan dilakukan formating untuk membalikkan kembali
     */

    public static String reordering(String sentence) {
        LinkGrammar linkGrammar = LinkGrammar.initLinkGrammar(sentence);
        linkGrammar = linkGrammar.generateLinkGrammar(sentence);
        for (int i = 0; i < linkGrammar.phrases.size(); i++) {
            linkGrammar.phrases.add(i, reordering(linkGrammar.phrases.remove(i)));
        }
        return linkGrammar.toString(linkGrammar);
    }

    /**
     * 
     * @param phrase prasa kata
     * @return 
     * berfungsi untuk membalikan susunan kata sesuai dengan aturannya
     * dalam aturan bahasa inggri terkadang ada deret kata yg ditukar posisinya 
     * seperti "my book" yg artinya "buku saya" posisi antara my(saya) dan book(buku) terbalik
     * maka akan dilakukan formating untuk membalikkan kembali
     */
    protected static Grammar reordering(Grammar phrase) {
        if (phrase != null) {
            if (Util.REORDER_RULE.contains(phrase.classes.get(0))) {
                Grammar temp = phrase.leftGrammar;
                phrase.leftGrammar = phrase.rightGrammar;
                phrase.rightGrammar = temp;
            }
            phrase.leftGrammar = reordering(phrase.leftGrammar);
            phrase.rightGrammar = reordering(phrase.rightGrammar);
        }
        return phrase;
    }

}
