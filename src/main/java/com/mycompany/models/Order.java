/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.models;

import java.time.LocalDateTime;
import java.util.Map;

/**
 *
 * @author LOQ
 */

import java.util.List;

public class Order {
    private int orderId;
    private int customerId;
    private List<Map<String, Object>> items; // <-- list of maps instead of Map<Integer, Integer>
    private double totalAmount;

    public Order() {}

    public Order(int orderId, int customerId, List<Map<String, Object>> items,double totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount=totalAmount;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
