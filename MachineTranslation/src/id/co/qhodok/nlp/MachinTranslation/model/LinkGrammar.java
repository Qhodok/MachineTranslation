/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package id.co.qhodok.nlp.MachinTranslation.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andika Permana
 */
public class LinkGrammar {
    public List<Grammar> phrases;
    public LinkGrammar(){
        this.phrases = new ArrayList<>();
    }
    public static LinkGrammar initLinkGrammar(String sentence){
        LinkGrammar linkGrammar = new LinkGrammar();
        for(String token : sentence.split("\\s+")){
            linkGrammar.phrases.add(new Grammar(token,"noun"));
        }
        return linkGrammar;
    } 
    public static LinkGrammar initLinkGrammar(String[] tokens){
        LinkGrammar linkGrammar = new LinkGrammar();
        for(String token : tokens){
            linkGrammar.phrases.add(new Grammar(token,"noun"));
        }
        return linkGrammar;
    } 
}
