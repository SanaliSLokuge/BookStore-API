/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstore.resources;

/**
 *
 * @author LOQ
 */

import com.mycompany.exceptions.CartNotFoundException;
import com.mycompany.exceptions.InvalidInputException;
import com.mycompany.models.Cart;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {

    private static final Logger LOG = Logger.getLogger(CartResource.class.getName());
    
    private static final Map<Integer, Cart> carts = new ConcurrentHashMap<>();
    
    public static Map<Integer, Cart> getCarts() {
        return carts;
    }

    @POST
    @Path("/items")
    public Response addCartItem(@PathParam("customerId") int customerId, ItemRequest item) {
        if (item.getBookId() <= 0) {
            LOG.warning("Failed to add the item: Invalid ID");
            throw new InvalidInputException("Book ID be valid");
        }
        if (item.getQuantity() <= 0) {
            LOG.warning("Failed to add the item: Invalid Quantity");
            throw new InvalidInputException("Quantity must be at least 1");
        }

       
        Cart cart;
        if (carts.containsKey(customerId)) {
            cart = carts.get(customerId);
        } else {
            cart = new Cart(customerId);
            carts.put(customerId, cart);
        }
        
        cart.addItem(item.getBookId(), item.getQuantity());
        LOG.info("Added item to cart");
        return Response.status(Response.Status.CREATED).entity("Item added to cart").build();
    }

    @GET
    public Response getCart(@PathParam("customerId") int customerId) {
        Cart cart = carts.get(customerId);
        if (cart == null) {
            LOG.warning("Failed to get the cart: No cart ");
            throw new CartNotFoundException("Cart for customer " + customerId + " not found");
        }
        LOG.info("Fetched the cart");
        return Response.ok(cart).build();
    }

    @PUT
    @Path("/items/{bookId}")
    public Response updateCartItem(@PathParam("customerId") int customerId,
                                   @PathParam("bookId") int bookId,
                                   ItemRequest item) {
        if (item.getQuantity() < 0) {
            LOG.warning("Failed to get the cart: Empty cart");
            throw new InvalidInputException("Quantity cannot be negative");
        }

        Cart cart = carts.get(customerId);
        if (cart == null) {
            LOG.warning("Failed to get the cart: Empty cart ");
            throw new CartNotFoundException(
                "Cart for customer " + customerId + " not found");
        }
        cart.updateItem(bookId, item.getQuantity());
        LOG.info("Fetched item from cart");
        return Response.ok("Item updated").build();
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response removeCartItem(@PathParam("customerId") int customerId,
                                   @PathParam("bookId") int bookId) {
        Cart cart = carts.get(customerId);
        if (cart == null) {
            LOG.warning("Failed to delete the cart: Empty cart ");
            throw new CartNotFoundException(
                "Cart for customer " + customerId + " not found");
        }
        cart.removeItem(bookId);
        LOG.info("Removed item from the cart");
        return Response.noContent().build();
    }


    public static class ItemRequest {
        private int bookId;
        private int quantity;

        public int getBookId() { return bookId; }
        public void setBookId(int bookId) { this.bookId = bookId; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}