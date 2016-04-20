/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.qhodok.nlp.MachinTranslation.model;

import id.co.qhodok.nlp.MachinTranslation.Utils.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Andika
 */
public class LinkGrammarProcessor {

    protected final String rule = ""
            + "S noun::verb\n"
            + "O verb::object\n"
            + "A adjective::noun\n"
            + "D determiner::noun\n"
            + "J preposition::object\n"
            + "S noun::verb\n"
            + "I noun::inversion-case\n"
            + "B verb::wh-word\n"
            + "NN noun::noun\n"
            + "PRPN PRP::noun\n"
            + "ANN A::NN\n"
            + "APRPN A::PRPN\n"
            + "NNPRPN NN::PRPN";
    protected final String basicClass = "";

    protected HashMap<String, List<HashMap<String, String>>> connectionLink;
    protected HashMap<String, String> generalClass;
    protected List<String> reorderingRule;

    public LinkGrammarProcessor() {
        this.initConnectionLink();
        this.initGeneralClass();
    }

    protected void initConnectionLink() {
        this.connectionLink = new HashMap<>();
        String connectionResult;
        for (String ruleSentence : rule.trim().split("\n")) {
            String[] ruleInfo = ruleSentence.split("\\s+");
            String[] classInfo = ruleInfo[1].split("::");
            HashMap<String, String> link = new HashMap<String, String>();
            link.put("result", ruleInfo[0]);
            link.put("rightPhrase", classInfo[1]);
            if (!this.connectionLink.containsKey(classInfo[0])) {
                this.connectionLink.put(classInfo[0], new ArrayList<HashMap<String, String>>());
            }
            this.connectionLink.get(classInfo[0]).add(link);
        }
    }

    protected void initGeneralClass() {
        this.generalClass = new HashMap<>();
    }

    protected void reorderingRule() {
        this.reorderingRule = new ArrayList<>();
    }

    public String reordering(String sentence) {
        LinkGrammar linkGrammar = this.createLinkGrammar(sentence);
        for (int i = 0; i < linkGrammar.phrases.size(); i++) {
            linkGrammar.phrases.add(i, reordering(linkGrammar.phrases.remove(i)));
        }
        return this.linkGrammarExtraction(linkGrammar);
    }

    protected Grammar reordering(Grammar phrase) {
        if (phrase != null) {
            if (this.reorderingRule.contains(phrase.classes.get(0))) {
                Grammar temp = phrase.leftGrammar;
                phrase.leftGrammar = phrase.rightGrammar;
                phrase.rightGrammar = temp;
            }
            phrase.leftGrammar = reordering(phrase.leftGrammar);
            phrase.rightGrammar = reordering(phrase.rightGrammar);
        }
        return phrase;
    }

    public String linkGrammarExtraction(LinkGrammar linkGrammar) {
        String sentence = "";
        for (Grammar phrase : linkGrammar.phrases) {
            if (phrase.leftGrammar == null) {
                sentence += phrase.caption + " ";
            } else {
                sentence += this.linkGrammarExtraction(phrase);
            }
        }
        return sentence.replaceAll("\\s+", " ").trim();
    }

    public String linkGrammarExtraction(Grammar phrase) {
        String sentence = "";
        if (phrase.leftGrammar == null) {
            sentence += phrase.caption + " ";
        } else {
            sentence += this.linkGrammarExtraction(phrase);
        }
        return sentence.replaceAll("\\s+", " ").trim();
    }

    protected LinkGrammar createLinkGrammar(String sentence) {
        LinkGrammar linkGrammar = this.createLinkGrammar(LinkGrammar.initLinkGrammar(sentence), 0);
        int size = linkGrammar.phrases.size();
        while (true) {
            linkGrammar = this.createLinkGrammar(linkGrammar, 0);
            if (size == linkGrammar.phrases.size()) {
                break;
            }
        }
        return linkGrammar;
    }

    protected LinkGrammar createLinkGrammar(LinkGrammar linkGrammar, int index) {
        if (index < linkGrammar.phrases.size() - 2) {
            linkGrammar = this.createLinkGrammar(linkGrammar, index + 1);
        }
        boolean found = false;
        String connectionClass = null;
        for (String leftWordClass : linkGrammar.phrases.get(index).classes) {
            for (String rightWordClass : linkGrammar.phrases.get(index + 1).classes) {
                connectionClass = checkConnection(leftWordClass, rightWordClass);
                if (connectionClass != null) {
                    Grammar phrase = new Grammar("", connectionClass);
                    phrase.leftGrammar = linkGrammar.phrases.remove(index);
                    phrase.rightGrammar = linkGrammar.phrases.remove(index);
                    linkGrammar.phrases.add(index, phrase);
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        return linkGrammar;
    }

    protected String checkConnection(String class1, String class2) {
        return "";
    }
}
