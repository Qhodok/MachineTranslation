/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.qhodok.nlp.MachinTranslation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Andika
 */
public class LanguageModel {

    protected HashMap<Integer, HashMap<String, Double>> Ngram;
    protected HashMap<Integer, HashMap<String, HashMap<String, Double>>> order;
    protected String cospus;
    protected String delimeter;

    public LanguageModel() {
        this.Ngram = new HashMap<>();
        this.order = new HashMap<>();
        this.delimeter = "";
        this.cospus = "";
    }

    public void addCorpus(String corpus) {
        this.cospus += corpus + "\n";
    }

    public void loadLM(String lmData) {
    }

    public void addDelimeter(String delimeter) {
        if (this.delimeter.isEmpty()) {
            this.delimeter = "(" + delimeter + ")";
        } else {
            this.delimeter += "|(" + delimeter + ")";
        }
    }

    public HashMap generateLM(int maxgram) {
        String[] tempNode = new String[maxgram];
        String prev = "", next = "";
        for (int i = 0; i < maxgram; i++) {
            this.Ngram.put(i, new HashMap<String, Double>());
            this.order.put(i, new HashMap<String, HashMap<String, Double>>());
        }
        for (String segmentedCorpus : this.cospus.split(this.delimeter)) {
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
                            if (this.order.get(i).containsKey(prev)) {
                                if (this.order.get(i).get(prev).containsKey(next)) {
                                    this.order.get(i).get(prev).put(next, this.order.get(i).get(prev).get(next) + 1);
                                } else {
                                    this.order.get(i).get(prev).put(next, 1.D);
                                }
                            } else {
                                this.order.get(i).put(prev, new HashMap<String, Double>());
                                this.order.get(i).get(prev).put(next, 1.D);
                            }
                            //System.out.println(tempNode[i] + " prev = " + prev + " :: next = " + next+"\n");
                        //}
                    }
                }
            }
        }
        this.computeNgram();
        return this.Ngram;
    }

    protected void computeNgram() {
        String prev = "", next = "";
        double currentValue = 0, totalValue = 0;
        for (int index : this.Ngram.keySet()) {
            if(index > 0){
            for (String ngram : this.Ngram.get(index).keySet()) {
                try {
                    prev = ngram.trim().replaceFirst("\\S+{" + index + "}$", "").trim();
                    next = ngram.trim().replaceFirst(prev, "").trim();
                    currentValue = this.order.get(index).get(prev).get(next);
                    totalValue = 0;
                    for (String nextOrder : this.order.get(index).get(prev).keySet()) {
                        totalValue += this.order.get(index).get(prev).get(nextOrder);
                    }
                    this.Ngram.get(index).put(ngram, currentValue / totalValue);
                } catch (Exception e) {
                    
                        System.out.println(ngram);
                        System.out.println(prev+" "+next);
                        //System.out.println(this.order);
                        System.out.println(this.order.get(index));
                        System.out.println(prev+" "+this.order.get(index).get(prev));
                        System.out.println(next);//+" "+this.order.get(index).get(prev).get(next));
                        System.out.println("\n\n");
                        //e.printStackTrace();
                }
            }}
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
