/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.qhodok.nlp.MachinTranslation.model;

import id.co.qhodok.nlp.MachinTranslation.Utils.Util;
import java.util.HashMap;

/**
 *
 * @author Andika
 */
public class TranslationModel {

    protected HashMap<String, HashMap<String, Double>> wordRelation;

    public TranslationModel() {
        this.wordRelation = new HashMap<>();
    }

    public void generateTranslationModel(String sourceCorpus, String targetCorpus,  String delimeter) {
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

            for (String word : this.wordRelation.keySet()) {
                totalValue = 0;
                translation = this.wordRelation.get(word);
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
            if (this.wordRelation.containsKey(sourceWords[sourceIndex])) {
                if (this.wordRelation.get(sourceWords[sourceIndex]).containsKey(targetWords[targetIndex])) {
                    this.wordRelation.get(sourceWords[sourceIndex]).put(targetWords[targetIndex], this.wordRelation.get(sourceWords[sourceIndex]).get(targetWords[targetIndex]) + 1);
                } else {
                    this.wordRelation.get(sourceWords[sourceIndex]).put(targetWords[targetIndex], 1D);
                }
            } else {
                this.wordRelation.put(sourceWords[sourceIndex], new HashMap<String, Double>());
                this.wordRelation.get(sourceWords[sourceIndex]).put(targetWords[targetIndex], 1D);
            }
            return true;
        } else {
            return false;
        }
    }

    public HashMap<String[], Double> computeTranslation(String targetCase) {
        HashMap<String[], Double> result = new HashMap<>();
        this.junction(targetCase.split("\\s+"), 0, new String[targetCase.split("\\s+").length], result, new double[targetCase.split("\\s+").length]);
        return result;
    }

    protected void junction(String[] target, int index, String[] partialResult, HashMap<String[], Double> result, double[] score) {
        if (index < target.length) {
            if (this.wordRelation.get(target[index]).size() > 1) {
                for (String word : this.wordRelation.get(target[index]).keySet()) {
                    partialResult[index] = word;
                    score[index] = this.wordRelation.get(target[index]).get(word);
                    this.junction(target, index + 1, partialResult, result, score);
                }
            } else {
                partialResult[index] = (String) this.wordRelation.get(target[index]).keySet().toArray()[0];
                score[index] = this.wordRelation.get(target[index]).get(partialResult[index]);
                this.junction(target, index + 1, partialResult, result, score);
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
