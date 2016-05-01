/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.nlp.MachineTranslation.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andika
 */
public class LanguageModel implements java.io.Serializable {

    protected Map<Integer, HashMap<String, Double>> Ngram;
    protected Map<Integer, HashMap<String, HashMap<String, Double>>> frequenceWordLink;
    protected String corpus;
    protected String delimeter;

    public LanguageModel() {
        this.Ngram = new HashMap<>();
        this.frequenceWordLink = new HashMap<>();
        this.delimeter = "";
        this.corpus = "";
    }
    
    public void addCorpus(String corpus) {
        this.corpus += corpus + "\n";
    }

    public void addDelimeter(String delimeter) {
        if (this.delimeter.isEmpty()) {
            this.delimeter = "(" + delimeter + ")";
        } else {
            this.delimeter += "|(" + delimeter + ")";
        }
    }

    public void generateLM(int maxgram) {
        String[] tempNode = new String[maxgram];
        String prev = "", next = "";
        for (int i = 0; i < maxgram; i++) {
            this.Ngram.put(i, new HashMap<String, Double>());
            this.frequenceWordLink.put(i, new HashMap<String, HashMap<String, Double>>());
        }
        for (String segmentedCorpus : this.corpus.split(this.delimeter)) {
            for (int i = 0; i < maxgram; i++) {
                segmentedCorpus = "_ " + segmentedCorpus.trim() + " _";
                tempNode[i] = "";
            }
            for (String token : segmentedCorpus.split("(\\s+)")) {
                for (int i = 0; i < maxgram; i++) {
                    tempNode[i] = (tempNode[i] + " " + token).trim();
                    //System.out.println(tempNode[i]);
                    if (tempNode[i].split("\\s+").length > i + 1) {
                        tempNode[i] = tempNode[i].replaceFirst("\\S+\\s+", "");
                    }
                    if (tempNode[i].split("\\s+").length >= i && !tempNode[i].replaceAll("_\\s*", "").trim().isEmpty()) {
                        //System.out.println("if 1");
                        this.Ngram.get(i).put(tempNode[i], 1.D);
                        prev = tempNode[i].replaceFirst("\\S+{" + i + "}$", "").trim();
                        //if (!prev.replaceAll("_\\s*", "").trim().isEmpty()) {
                        //System.out.println("if 2");
                        next = tempNode[i].replaceFirst(prev, "").trim();
                        if (this.frequenceWordLink.get(i).containsKey(prev)) {
                            if (this.frequenceWordLink.get(i).get(prev).containsKey(next)) {
                                this.frequenceWordLink.get(i).get(prev).put(next, this.frequenceWordLink.get(i).get(prev).get(next) + 1);
                            } else {
                                this.frequenceWordLink.get(i).get(prev).put(next, 1.D);
                            }
                        } else {
                            this.frequenceWordLink.get(i).put(prev, new HashMap<String, Double>());
                            this.frequenceWordLink.get(i).get(prev).put(next, 1.D);
                        }
                    }
                }
            }
        }
        this.computeNgram();
        this.frequenceWordLink.clear();
        this.corpus = "";
        this.delimeter = "";
    }

    protected void computeNgram() {
        String prev = "", next = "";
        double currentValue = 0, totalValue = 0;
        for (int index : this.Ngram.keySet()) {
            if (index > 0) {
                for (String ngram : this.Ngram.get(index).keySet()) {
                    try {
                        prev = ngram.trim().replaceFirst("\\S+{" + index + "}$", "").trim();
                        next = ngram.trim().replaceFirst(prev, "").trim();
                        currentValue = this.frequenceWordLink.get(index).get(prev).get(next);
                        totalValue = 0;
                        for (String nextOrder : this.frequenceWordLink.get(index).get(prev).keySet()) {
                            totalValue += this.frequenceWordLink.get(index).get(prev).get(nextOrder);
                        }
                        this.Ngram.get(index).put(ngram, currentValue / totalValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public double computeProbabilities(String[] target) {
        String temp = "";
        double probabilities = 0;
        boolean found = true;
        for (int index = 0; index < target.length; index++) {
            found = false;
            for (int i = this.Ngram.size() - 1; i >= 0; i--) {
                if (index + i < target.length) {
                    temp = this.arrayToString(target, index, index + i);
                    if (this.Ngram.get(i).containsKey(temp)) {
                        if (probabilities == 0) {
                            probabilities = this.Ngram.get(i).get(temp);
                        } else {
                            probabilities *= this.Ngram.get(i).get(temp);
                        }
                        found = true;
                        break;
                    }
                }
            }
        }
        return probabilities;
    }

    protected String arrayToString(String[] words) {
        String result = "";
        for (String word : words) {
            result += word + " ";
        }
        return result.trim();
    }

    protected String arrayToString(String[] words, int start, int stop) {
        String result = "";
        for (int i = start; i <= stop; i++) {
            result += words[i] + " ";
        }
        return result.trim();
    }
}
