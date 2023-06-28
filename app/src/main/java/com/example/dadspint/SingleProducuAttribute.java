package com.example.dadspint;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleProducuAttribute {

    String attribute_id,attribute_name,product_id,salesprice;
    ArrayList<SingleProductVariations> singleProductVariations;
    ArrayList<String> str_singleProductVariations;
    HashMap<String,String> hash_singleProductVariations;

    public SingleProducuAttribute(String attribute_id, String attribute_name, String product_id,
                                  ArrayList<SingleProductVariations> singleProductVariations,
                                  ArrayList<String> str_singleProductVariations,
                                  HashMap<String,String> hash_singleProductVariations,String salesprice) {

        this.attribute_id = attribute_id;
        this.attribute_name = attribute_name;
        this.product_id = product_id;
        this.singleProductVariations = singleProductVariations;
        this.str_singleProductVariations = str_singleProductVariations;
        this.hash_singleProductVariations = hash_singleProductVariations;
        this.salesprice = salesprice;
    }

    public String getAttribute_id() {
        return attribute_id;
    }

    public void setAttribute_id(String attribute_id) {
        this.attribute_id = attribute_id;
    }

    public String getAttribute_name() {
        return attribute_name;
    }

    public void setAttribute_name(String attribute_name) {
        this.attribute_name = attribute_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public ArrayList<SingleProductVariations> getSingleProductVariations() {
        return singleProductVariations;
    }

    public void setSingleProductVariations(ArrayList<SingleProductVariations> singleProductVariations) {
        this.singleProductVariations = singleProductVariations;
    }

    public ArrayList<String> getStr_singleProductVariations() {
        return str_singleProductVariations;
    }

    public void setStr_singleProductVariations(ArrayList<String> str_singleProductVariations) {
        this.str_singleProductVariations = str_singleProductVariations;
    }

    public HashMap<String, String> getHash_singleProductVariations() {
        return hash_singleProductVariations;
    }

    public void setHash_singleProductVariations(HashMap<String, String> hash_singleProductVariations) {
        this.hash_singleProductVariations = hash_singleProductVariations;
    }

    public String getSalesprice() {
        return salesprice;
    }

    public void setSalesprice(String salesprice) {
        this.salesprice = salesprice;
    }
}
