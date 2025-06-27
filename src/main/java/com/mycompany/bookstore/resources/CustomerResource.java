/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstore.resources;

/**
 *
 * @author LOQ
 */
import com.mycompany.exceptions.CustomerNotFoundException;
import com.mycompany.exceptions.InvalidInputException;
import com.mycompany.models.Customer;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.logging.Logger;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private static final Logger LOG = Logger.getLogger(CustomerResource.class.getName());
    
    private static Map<Integer, Customer> customers = new HashMap<>();
    private static int customerIdCounter = 1;

    public CustomerResource() {
    }

    @POST
    public Response addCustomer(Customer customer) {
        customer = new Customer(customerIdCounter,customer.getName(), customer.getEmail(), customer.getPassword());
        if (customer.getName() == null || customer.getName().isBlank()) {
            LOG.warning("Failed to add customer: invalid name");
            throw new InvalidInputException("Customer name is required");
        }
        if (customer.getEmail() == null || !customer.getEmail().contains("@")) {
            LOG.warning("Failed to add customer: invalid email");
            throw new InvalidInputException("Valid email is required");
        }
        if (customer.getPassword() == null || customer.getPassword().length() < 6) {
            LOG.warning("Failed to add customer: invalid password");
            throw new InvalidInputException("Password must be at least 6 characters");
        }
        
        int id = customerIdCounter++;
        customers.put(id, customer);
        LOG.info("Added Customer with ID:" + id);
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }
    
    @GET
    public Response getAllCustomers() {
        LOG.info("Fetching all the Customers");
        return Response.ok(new ArrayList<>(customers.values())).build();
    }

    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") int id) {
        Customer customer = customers.get(id);
        if (customer == null) {
            LOG.warning("Failed to fetch customer: not registered");
            throw new CustomerNotFoundException("Customer with ID " + id + " not found");
        }
        LOG.info("Fetched the Customer with ID:"+id);
        return Response.ok(customer).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") int id, Customer updatedCustomer) {
        Customer existing = customers.get(id);
        if (existing == null) {
            LOG.warning("Failed to update customer: Customer not registered");
            throw new CustomerNotFoundException("Customer with ID " + id + " not found");
        }
        updatedCustomer = new Customer(id,updatedCustomer.getName(), updatedCustomer.getEmail(), updatedCustomer.getPassword());
        customers.put(id, updatedCustomer);
        LOG.info("Updated the Customer with ID:"+id);
        return Response.ok(updatedCustomer).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        Customer removed = customers.remove(id);
        if (removed == null) {
            LOG.warning("Failed to delete customer: customer not registered");
            throw new CustomerNotFoundException("Customer with ID " + id + " not found");
        }
        LOG.info("Deleted the Customer with ID:"+id);
        return Response.noContent().build();
    }

}
