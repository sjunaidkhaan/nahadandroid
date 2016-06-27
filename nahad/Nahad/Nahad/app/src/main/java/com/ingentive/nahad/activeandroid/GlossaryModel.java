package com.ingentive.nahad.activeandroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by PC on 18-06-2016.
 */
@Table(name = "GlossaryModel")
public class GlossaryModel extends Model {

    @Column(name = "glossary_id")
    public int glossaryId;

    public int getGlossaryId() {
        return glossaryId;
    }

    public void setGlossaryId(int glossaryId) {
        this.glossaryId = glossaryId;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
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

    @Column(name = "alphabet")
    public String alphabet;

    @Column(name = "word")
    public String word;
    @Column(name = "definition")
    public String definition;
    @Column(name = "file")
    public String file;

    public GlossaryModel(){
        super();
        this.glossaryId=0;
        this.alphabet="";
        this.word="";
        this.definition="";
        this.file="";
    }

}
