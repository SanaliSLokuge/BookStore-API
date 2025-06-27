/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.models;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LOQ
 */
public class Cart {
    private int customerId;
    private Map<Integer, Integer> OrderItems = new HashMap<>();

    public Cart() {
    }
    
    public Cart(int customerId) {
        this.customerId = customerId;
    }
    
    public void addItem(int bookId, int quantity) {
        OrderItems.merge(bookId, quantity, Integer::sum);
    }

    public void updateItem(int bookId, int quantity) {
        OrderItems.put(bookId, quantity);
    }

    public void removeItem(int bookId) {
        OrderItems.remove(bookId);
    }
    
    public Map<Integer, Integer> getItems() {
        return OrderItems;
    }

    public void clear() {
        OrderItems.clear();
    }
}
