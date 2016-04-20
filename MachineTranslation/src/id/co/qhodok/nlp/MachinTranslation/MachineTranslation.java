/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package id.co.qhodok.nlp.MachinTranslation;

import id.co.qhodok.nlp.MachinTranslation.model.LanguageModel;
import id.co.qhodok.nlp.MachinTranslation.model.TranslationModel;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Andika
 */
public class MachineTranslation {
    protected TranslationModel translationModel;
    protected LanguageModel sourceLanguageModel;
    protected LanguageModel targetLanguageModel;
    public MachineTranslation(){
        this.translationModel = new TranslationModel();
        this.sourceLanguageModel = new LanguageModel();
        this.targetLanguageModel = new LanguageModel();
    }
    public void training(String sourceCospus, String targetCorpus, HashMap<String, List<String>>dictionary,int maxOrder){
        this.translationModel.generateTranslationModel(sourceCospus, targetCorpus, dictionary, "\n");
        this.sourceLanguageModel.addCorpus(sourceCospus);
        this.sourceLanguageModel.addDelimeter("\n+");
        this.sourceLanguageModel.generateLM(maxOrder);
        this.targetLanguageModel.addCorpus(targetCorpus);
        this.targetLanguageModel.addDelimeter("\n+");
        this.targetLanguageModel.generateLM(maxOrder);
    }
    public String translation(String sentence){
        HashMap<String[], Double> computeTranslation = this.translationModel.computeTranslation(sentence);
        for(String[] listOfWord:computeTranslation.keySet()){
            double computeProbabilities = sourceLanguageModel.computeProbabilities(sentence.trim().split("\\s+"));
            double computeProbabilities1 = targetLanguageModel.computeProbabilities(listOfWord);
            System.out.println(arrayToString(listOfWord) + " == "+((computeTranslation.get(listOfWord)*computeProbabilities))/computeProbabilities1);
        }
        return "";
    }
            
    protected String arrayToString(String[] words) {
        String result = "";
        for (String word : words) {
            result += word + " ";
        }
        return result.trim();
    }
}
