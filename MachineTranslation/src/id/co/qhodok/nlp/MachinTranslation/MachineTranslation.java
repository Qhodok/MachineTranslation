/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.qhodok.nlp.MachinTranslation;

import id.co.qhodok.nlp.MachinTranslation.Utils.Util;
import id.co.qhodok.nlp.MachinTranslation.model.LanguageModel;
import id.co.qhodok.nlp.MachinTranslation.model.TranslationModel;
import id.co.qhodok.nlp.MachinTranslation.model.WordReordering;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.XML;

/**
 *
 * @author Andika
 */
public class MachineTranslation {

    protected TranslationModel translationModel;
    protected LanguageModel sourceLanguageModel;
    protected LanguageModel targetLanguageModel;

    public MachineTranslation() {
        this.translationModel = new TranslationModel();
        this.sourceLanguageModel = new LanguageModel();
        this.targetLanguageModel = new LanguageModel();
    }
    public MachineTranslation(String pathfile) {
        this.translationModel = new TranslationModel(pathfile);
        this.sourceLanguageModel = new LanguageModel(pathfile);
        this.targetLanguageModel = new LanguageModel(pathfile);
    }

    public void training(String sourceCospus, String targetCorpus, String dict, int maxOrder, boolean useReordering) {
        if (useReordering) {
            String temp = "";
            for (String sentence : sourceCospus.split("\n")) {
                temp += WordReordering.reordering(sentence) + "\n";
            }
            sourceCospus = temp;
        }
        try {
            Util.init(dict);
        } catch (IOException ex) {
            Logger.getLogger(MachineTranslation.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.translationModel.generateTranslationModel(sourceCospus, targetCorpus, "\n");
        this.sourceLanguageModel.addCorpus(sourceCospus);
        this.sourceLanguageModel.addDelimeter("\n+");
        this.sourceLanguageModel.generateLM(maxOrder);
        this.targetLanguageModel.addCorpus(targetCorpus);
        this.targetLanguageModel.addDelimeter("\n+");
        this.targetLanguageModel.generateLM(maxOrder);
    }

    public void save(String pathfile) {
        this.sourceLanguageModel.save(pathfile);
        this.targetLanguageModel.save(pathfile);
        this.translationModel.save(pathfile);
        Util.write(pathfile + "dict.xml", XML.toString(Util.DICTIONARY));
    }

    public String translation(String sentence, boolean useReordering) {
        if (useReordering) {
            sentence = WordReordering.reordering(sentence);
        }
        HashMap<String[], Double> computeTranslation = this.translationModel.computeTranslation(WordReordering.reordering(sentence));
        for (String[] listOfWord : computeTranslation.keySet()) {
            double computeProbabilities = sourceLanguageModel.computeProbabilities(sentence.trim().split("\\s+"));
            double computeProbabilities1 = targetLanguageModel.computeProbabilities(listOfWord);
            System.out.println(arrayToString(listOfWord) + " == " + ((computeTranslation.get(listOfWord) * computeProbabilities)) / computeProbabilities1);
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
