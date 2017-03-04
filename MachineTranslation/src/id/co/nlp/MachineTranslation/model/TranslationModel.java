/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.nlp.MachineTranslation.model;

import id.co.nlp.MachineTranslation.Utils.Util;
import java.util.HashMap;

/**
 *
 * @author Andika
 */
public class TranslationModel implements java.io.Serializable {

    protected HashMap<String, HashMap<String, Double>> wordTranslation;

    public TranslationModel() {
        this.wordTranslation = new HashMap<>();
    }
/**
 * 
 * @param sourceCorpus korpus bahasa inggris
 * @param targetCorpus korpus bahasa indonesia
 * @param delimeter karakter pembatas
 * function yg berfungsi untuk membuat model translate. yg mana model ini akan dipakai saat pentranslatan sebuah kalimat. 
 * model ini mengandung bobot bobot dari tiap kombinasi kata yg akan diterjemahkan
 */
    public void generateTranslationModel(String sourceCorpus, String targetCorpus, String delimeter) {
        String[] sourceSentences = sourceCorpus.split(delimeter);
        String[] targetSentences = targetCorpus.split(delimeter);
        String[] sourceWords, targetWords;
        int index = 0, targetIndex = 0;
        double totalValue = 0.D;
        HashMap<String, Double> translation;
        if (sourceSentences.length == targetSentences.length) {
            for (int i = 0; i < sourceSentences.length; i++) {
                sourceWords = sourceSentences[i].trim().split("\\s+");
                targetWords = targetSentences[i].trim().split("\\s+");
                targetIndex = 0;
                for (index = 0; index < sourceWords.length; index++) {
                    if (Util.DICTIONARY.has(sourceWords[index])) {
                        this.addTranslation(index, sourceWords, targetWords);
                    } else {
                        System.out.println("not in dict " + sourceWords[index]);
                    }
                }
            }

            for (String word : this.wordTranslation.keySet()) {
                totalValue = 0;
                translation = this.wordTranslation.get(word);
                for (double value : translation.values()) {
                    totalValue += value;
                }
                for (String value : translation.keySet()) {
                    //System.out.println(word+" = "+value);
                }
                for (String token : translation.keySet()) {
                    translation.put(token, translation.get(token) / totalValue);
                }
            }
        } else {
        }
    }

    /**
     * 
     * @param index index kata yg akan ditambahkan
     * @param sourceWords deret kata yg akan di terjemahkan
     * @param targetWords deret kata hasil terjemahan
     * @return 
     * function yg berfungsi untuk menambahkan padanan kata dan terjemahan beserta bobot dari kombinasi terjemahan
     */
    protected boolean addTranslation(int index, String[] sourceWords, String[] targetWords) {
        if (Util.DICTIONARY.has(sourceWords[index])) {
            if (this.wordTranslation.containsKey(sourceWords[index])) {
                if (this.wordTranslation.get(sourceWords[index]).containsKey(targetWords[index])) {
                    this.wordTranslation.get(sourceWords[index]).put(targetWords[index], this.wordTranslation.get(sourceWords[index]).get(targetWords[index]) + 1);
                } else {
                    this.wordTranslation.get(sourceWords[index]).put(targetWords[index], 1D);
                }
            } else {
                this.wordTranslation.put(sourceWords[index], new HashMap<String, Double>());
                this.wordTranslation.get(sourceWords[index]).put(targetWords[index], 1D);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @param targetCase kalimat yg akan ditranslate
     * @return 
     * menghitung bobot dari masing masing kemungkinan hasil terjemahan yg dilihat dari bobot dari hasil terjemahan terhadap kata yg akan diterjemahkan
     */
    public HashMap<String[], Double> computeTranslation(String targetCase) {
        HashMap<String[], Double> result = new HashMap<>();
        this.generateTranslationCombination(targetCase.split("\\s+"), 0, new String[targetCase.split("\\s+").length], result, new double[targetCase.split("\\s+").length]);
        return result;
    }
    
    /**
     * 
     * @param words deret kata
     * @return 
     * konversi dari array ke single string
     */
    protected String arrayToString(String[] words) {
        String result = "";
        for (String word : words) {
            result += word + " ";
        }
        return result.trim();
    }
/**
 * 
 * @param target deret kata yang akan di translate
 * @param index index kata yg akan di translate
 * @param partialResult hasil sementara dari penterjemahan
 * @param result hasil penterjemahan beserta score
 * @param score score awal
 * function yg berfungsi untuk menerjemahkan target menjadi beberapa hasil terjemahan beserta bobotnya
 */
    protected void generateTranslationCombination(String[] target, int index, String[] partialResult, HashMap<String[], Double> result, double[] score) {
        if (index < target.length) {
            if (!this.wordTranslation.containsKey(target[index])) {
                partialResult[index] = target[index];
                //System.out.println(partialResult[index] + " " + target[index]);
                score[index] = 1.D;
                this.generateTranslationCombination(target, index + 1, partialResult, result, score);
            } else if (this.wordTranslation.get(target[index]).size() == 1) {
                partialResult[index] = (String) this.wordTranslation.get(target[index]).keySet().toArray()[0];
                //System.out.println("else if " + partialResult[index] + " " + target[index]);
                score[index] = this.wordTranslation.get(target[index]).get(partialResult[index]);
                this.generateTranslationCombination(target, index + 1, partialResult, result, score);
            } else {
                for (String word : this.wordTranslation.get(target[index]).keySet()) {
                    partialResult[index] = word;
                    //System.out.println("else "+word + " " + target[index]);
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
            result.put(partialResult.clone(), resultScore);
        }
    }

}
