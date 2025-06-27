/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.models;

/**
 *
 * @author LOQ
 */
public class Author {
    private int id;
    private String name;
    private String biography;

    public Author() {
    }

    public Author(int id,String name, String biography) {
        this.id=id;
        this.name = name;
        this.biography = biography;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBiography() {
        return biography;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
    

    @Override
    public String toString() {
        return "Author{" + "id=" + id + ", name=" + name + ", biography=" + biography + '}';
    }
    
    
}
