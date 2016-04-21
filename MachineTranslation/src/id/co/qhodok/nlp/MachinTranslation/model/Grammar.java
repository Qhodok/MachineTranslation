/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package id.co.qhodok.nlp.MachinTranslation.model;

import id.co.qhodok.nlp.MachinTranslation.Utils.Util;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

/**
 *
 * @author Andika Permana
 */
public class Grammar {
    public String caption;
    public JSONArray classes;
    public Grammar leftGrammar;
    public Grammar rightGrammar;
    public Grammar(String caption,String defaultClass){
        this.caption = caption;
        this.classes = new JSONArray();
        if(Util.DICTIONARY.has(this.caption)){
            this.classes = Util.DICTIONARY.getJSONObject(this.caption).getJSONArray("class");
        }
        if(this.classes.length() == 0){
            this.classes.put(defaultClass);
        }
        this.leftGrammar = null;
        this.rightGrammar = null;
    }
}
