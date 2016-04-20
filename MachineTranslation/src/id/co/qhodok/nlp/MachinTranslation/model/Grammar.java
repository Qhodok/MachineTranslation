/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package id.co.qhodok.nlp.MachinTranslation.model;

import id.co.qhodok.nlp.MachinTranslation.Utils.Util;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andika Permana
 */
public class Grammar {
    public String caption;
    public List<String> classes;
    public Grammar leftGrammar;
    public Grammar rightGrammar;
    public Grammar(String caption,String defaultClass){
        this.caption = caption;
        this.classes = new ArrayList<>();
        if(Util.dictionary.containsKey(this.caption)){
            this.classes = (List<String>) Util.dictionary.get(this.caption).get("classes");
        }
        if(this.classes.isEmpty()){
            this.classes.add(defaultClass);
        }
        this.leftGrammar = null;
        this.rightGrammar = null;
    }
}
