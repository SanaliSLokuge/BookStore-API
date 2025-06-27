/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.exceptions;

/**
 *
 * @author LOQ
 */

import javax.ws.rs.NotFoundException;

public class OutOfStockException extends NotFoundException{
    public OutOfStockException(String message) {
        super(message);
    }
    
    
}
