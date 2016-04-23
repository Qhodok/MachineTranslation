/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.qhodok.nlp.MachinTranslation.model;

import id.co.qhodok.nlp.MachinTranslation.Utils.Util;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.json.XML;

/**
 *
 * @author Andika
 */
public class TranslationModel {

    protected HashMap<String, HashMap<String, Double>> wordTranslation;

    public TranslationModel() {
        this.wordTranslation = new HashMap<>();
    }

    public TranslationModel(String pathfile) {
        try {
            this.wordTranslation = (HashMap<String, HashMap<String, Double>>) XML.toJSONObject(Util.read(pathfile + File.separator + "translation.dict")).getMap();
        } catch (IOException ex) {
            Logger.getLogger(TranslationModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void save(String pathfile) {
        Util.write(pathfile + "translation.dict", XML.toString(new JSONObject(this.wordTranslation)));
    }

    public void generateTranslationModel(String sourceCorpus, String targetCorpus, String delimeter) {
        String[] sourceSentences = sourceCorpus.split(delimeter);
        String[] targetSentences = targetCorpus.split(delimeter);
        String[] sourceWords, targetWords;
        int sourceIndex = 0, targetIndex = 0;
        String prevUnknowWord = null;
        int prevIndexUnkonWord = -1;
        double totalValue = 0.D;
        HashMap<String, Double> translation;
        if (sourceSentences.length == targetSentences.length) {
            for (int i = 0; i < sourceSentences.length; i++) {
                sourceWords = sourceSentences[i].trim().split("\\s+");
                targetWords = targetSentences[i].trim().split("\\s+");
                targetIndex = 0;
                for (sourceIndex = 0; sourceIndex < sourceWords.length; sourceIndex++) {
                    if (Util.DICTIONARY.has(sourceWords[sourceIndex])) {
                        if (!this.addTranslation(sourceIndex, targetIndex, sourceWords, targetWords)) {
                            //handle cross translation
                        } else {
                            targetIndex++;
                            //break;
                        }
                    } else {
                        System.out.println("not in dict " + sourceWords[sourceIndex]);
                    }
                }
            }

            for (String word : this.wordTranslation.keySet()) {
                totalValue = 0;
                translation = this.wordTranslation.get(word);
                for (double value : translation.values()) {
                    totalValue += value;
                }
                for (String token : translation.keySet()) {
                    translation.put(token, translation.get(token) / totalValue);
                }
            }
        } else {
        }
    }

    protected boolean addTranslation(int sourceIndex, int targetIndex, String[] sourceWords, String[] targetWords) {
        if (Util.DICTIONARY.has(sourceWords[sourceIndex])) {
            if (this.wordTranslation.containsKey(sourceWords[sourceIndex])) {
                if (this.wordTranslation.get(sourceWords[sourceIndex]).containsKey(targetWords[targetIndex])) {
                    this.wordTranslation.get(sourceWords[sourceIndex]).put(targetWords[targetIndex], this.wordTranslation.get(sourceWords[sourceIndex]).get(targetWords[targetIndex]) + 1);
                } else {
                    this.wordTranslation.get(sourceWords[sourceIndex]).put(targetWords[targetIndex], 1D);
                }
            } else {
                this.wordTranslation.put(sourceWords[sourceIndex], new HashMap<String, Double>());
                this.wordTranslation.get(sourceWords[sourceIndex]).put(targetWords[targetIndex], 1D);
            }
            return true;
        } else {
            return false;
        }
    }

    public HashMap<String[], Double> computeTranslation(String targetCase) {
        HashMap<String[], Double> result = new HashMap<>();
        this.generateTranslationCombination(targetCase.split("\\s+"), 0, new String[targetCase.split("\\s+").length], result, new double[targetCase.split("\\s+").length]);
        return result;
    }

    protected void generateTranslationCombination(String[] target, int index, String[] partialResult, HashMap<String[], Double> result, double[] score) {
        if (index < target.length) {
            if (!this.wordTranslation.containsKey(target[index])) {
                partialResult[index] = target[index];
                score[index] = 1.D;
                this.generateTranslationCombination(target, index + 1, partialResult, result, score);
            } else if (this.wordTranslation.get(target[index]).size() == 1) {
                partialResult[index] = (String) this.wordTranslation.get(target[index]).keySet().toArray()[0];
                score[index] = this.wordTranslation.get(target[index]).get(partialResult[index]);
                this.generateTranslationCombination(target, index + 1, partialResult, result, score);
            } else {
                for (String word : this.wordTranslation.get(target[index]).keySet()) {
                    partialResult[index] = word;
                    if (this.wordTranslation.get(target[index]).containsKey(word)) {
                        score[index] = this.wordTranslation.get(target[index]).get(word);
                    }
                    this.generateTranslationCombination(target, index + 1, partialResult, result, score);
                }
            }
        } else {
            double resultScore = 1D;
            for (double data : score) {
                resultScore *= data;
            }
            result.put(partialResult, resultScore);
        }
    }

}
