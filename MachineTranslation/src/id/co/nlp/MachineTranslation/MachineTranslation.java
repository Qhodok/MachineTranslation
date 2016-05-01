/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.nlp.MachineTranslation;

import id.co.nlp.MachineTranslation.Utils.Util;
import id.co.nlp.MachineTranslation.model.LanguageModel;
import id.co.nlp.MachineTranslation.model.TranslationModel;
import id.co.nlp.MachineTranslation.model.WordReordering;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andika
 */
public class MachineTranslation {

    protected TranslationModel translationModel;
    protected LanguageModel languageModel;
    protected String dictFile;

    public MachineTranslation() {
        this.translationModel = new TranslationModel();
        this.languageModel = new LanguageModel();
    }

    public MachineTranslation(String pathfile) {
        try {
            Util.init(pathfile + File.separator + "dict.xml");
            this.translationModel = (TranslationModel) Util.deserializing(pathfile + File.separator + "tm.ser");
            this.languageModel = (LanguageModel) Util.deserializing(pathfile + File.separator + "lm.ser");
        } catch (Exception ex) {

        }
    }

    public void training(String sourceCospus, String targetCorpus, String dict, int maxOrder, boolean useReordering) {
        try {
            this.dictFile = dict;
            Util.init(dict);
        } catch (IOException ex) {
            Logger.getLogger(MachineTranslation.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (useReordering) {
            String temp = "";
            for (String sentence : sourceCospus.split("\n")) {
                temp += WordReordering.reordering(sentence) + "\n";
            }
            sourceCospus = temp;
        }
        this.translationModel.generateTranslationModel(sourceCospus, targetCorpus, "\n");
        this.languageModel.addCorpus(sourceCospus);
        this.languageModel.addCorpus(targetCorpus);
        this.languageModel.addDelimeter("\n+");
        this.languageModel.generateLM(maxOrder);
    }

    public void save(String pathfile) {
        try {
            Util.serializing(pathfile + File.separator + "lm.ser", this.languageModel);
            Util.serializing(pathfile + File.separator + "tm.ser", this.translationModel);
            Util.write(pathfile + File.separator + "dict.xml", Util.read(this.dictFile));
            System.out.println("saved");
        } catch (IOException ex) {
            Logger.getLogger(MachineTranslation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String translation(String sentence, boolean useReordering) {
        if (useReordering) {
            sentence = WordReordering.reordering(sentence);
        }
        String result = "";
        double score = 0, tempScore = 0;

        HashMap<String[], Double> computeTranslation = this.translationModel.computeTranslation(WordReordering.reordering(sentence));
        for (String[] listOfWord : computeTranslation.keySet()) {
            double sourceProbabilities = languageModel.computeProbabilities(sentence.trim().split("\\s+"));
            double resultProbabilities = languageModel.computeProbabilities(listOfWord);
            tempScore = (computeTranslation.get(listOfWord) * sourceProbabilities) / resultProbabilities;
            if (tempScore > score) {
                score = tempScore;
                result = arrayToString(listOfWord);
            }
        }
        return result;
    }

    protected String arrayToString(String[] words) {
        String result = "";
        for (String word : words) {
            result += word + " ";
        }
        return result.trim();
    }
}
