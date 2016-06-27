package com.ingentive.nahad.model;

/**
 * Created by PC on 09-05-2016.
 */
public class GlossaryModel {

    int glossaryId;
    String alphabet;
    String word;
    String definition;
    String file;
    public GlossaryModel(){

    }

    public String getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }

    public int getGlossaryId() {
        return glossaryId;
    }

    public void setGlossaryId(int glossaryId) {
        this.glossaryId = glossaryId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
