/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstore.resources;

import com.mycompany.exceptions.CartNotFoundException;
import com.mycompany.exceptions.OutOfStockException;
import com.mycompany.models.Book;
import com.mycompany.models.Cart;
import com.mycompany.models.Order;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author LOQ
 */
@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private static final Map<Integer, Cart> carts = CartResource.getCarts(); 
    private static final Map<Integer, List<Order>> orders = new HashMap<>();
    private static int orderIdCounter = 1;
    private int customerId;
    private double totalAmount;
    private static final Logger LOG = Logger.getLogger(OrderResource.class.getName());
     
    
    public OrderResource() {
    }
    
    @POST
    public Response placeOrder(@PathParam("customerId") int customerId) {
        Cart cart = carts.get(customerId);
        if (cart == null) {
            LOG.warning("Failed to get the cart: No cart ");
            throw new CartNotFoundException("Cart for customer " + customerId + " not found.");
        }

        if (cart.getItems().isEmpty()) {
            LOG.warning("Failed to get the cart: Empty cart ");
            throw new OutOfStockException("Cart is empty. Cannot place order.");
        }

        Map<Integer, Book> bookMap = BookResource.getBooks();
        List<Map<String, Object>> orderItems = new ArrayList<>();
        double totalAmount = 0.0; // **local, not instance variable**

        // Check stock first
        for (Map.Entry<Integer, Integer> entry : cart.getItems().entrySet()) {
            int bookId = entry.getKey();
            int quantity = entry.getValue();

            Book book = bookMap.get(bookId);
            if (book == null) {
                LOG.warning("Failed to get the order: Invalid book ");
                throw new OutOfStockException("Book with ID " + bookId + " not found.");
            }
            if (book.getStockQuantity() < quantity) {
                throw new OutOfStockException("Not enough stock for book: " + book.getId());
            }
        }

        // Update stock and create order items
        for (Map.Entry<Integer, Integer> entry : cart.getItems().entrySet()) {
            int bookId = entry.getKey();
            int quantity = entry.getValue();

            Book book = bookMap.get(bookId);
            book.setStockQuantity(book.getStockQuantity() - quantity);

            double price = book.getPrice();

            Map<String, Object> item = new HashMap<>();
            item.put("bookId", bookId);
            item.put("title", book.getTitle());
            item.put("quantity", quantity);
            item.put("price", price);
            item.put("subtotal", price * quantity);
            orderItems.add(item);

            totalAmount += price * quantity;
        }

        Order order = new Order(orderIdCounter++, customerId, orderItems, totalAmount);
        orders.computeIfAbsent(customerId, k -> new ArrayList<>()).add(order);

        cart.clear(); // **important: properly empty the cart here**

        LOG.info("Added order from the cart");
        return Response.status(Response.Status.CREATED).entity(order).build();
    }


   
    
    @GET
    public Response getAllOrders(@PathParam("customerId") int customerId) {
        List<Order> customerOrders = orders.get(customerId);
        if (customerOrders == null || customerOrders.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No orders found for customer " + customerId).build();
        }
        LOG.info("Got all orders from the cart");
        return Response.ok(customerOrders).build();
    }
    
    @GET
    @Path("/{orderId}")
    public Response getOrderById(@PathParam("customerId") int customerId,@PathParam("orderId") int orderId) {
        List<Order> customerOrders = orders.get(customerId);
        if (customerOrders == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("No orders found for customer " + customerId).build();
        }

        for (Order order : customerOrders) {
            if (order.getOrderId() == orderId) {
                return Response.ok(order).build();
            }
        }
        LOG.info("Got the order from ID");
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("Order " + orderId + " not found for customer " + customerId).build();
    }
    
}
