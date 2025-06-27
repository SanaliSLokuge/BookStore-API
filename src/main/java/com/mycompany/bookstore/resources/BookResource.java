/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstore.resources;

/**
 *
 * @author LOQ
 */

import com.mycompany.exceptions.BookNotFoundException;
import com.mycompany.exceptions.InvalidInputException;
import com.mycompany.models.Book;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.logging.Logger;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    private static final Logger LOG = Logger.getLogger(BookResource.class.getName());
    
    private static Map<Integer, Book> books = new HashMap<>();
    private static int bookIdCounter = 1;
    
    public BookResource(){}
    
    @POST
    public Response addBook(Book book){
        validateBook(book);

        int existingId = findBookIdByTitle(book.getTitle());

        if (existingId != 0) {
            Book existingBook = books.get(existingId);
            existingBook.setStockQuantity(existingBook.getStockQuantity() + book.getStockQuantity());
            LOG.info("Book already exists. Updated stock for book ID: " + existingId);
            return Response.ok(existingBook).build(); 
        } else {
            book = new Book(bookIdCounter++, book.getTitle(), book.getAuthor(), book.getIsbn(),
                    book.getPublicationYear(), book.getPrice(), book.getStockQuantity());
            books.put(book.getId(), book);
            LOG.info("Added new book with ID: " + book.getId());
            return Response.status(Response.Status.CREATED).entity(book).build();
        }
    }
    
    @GET
    public Response getAllBooks() {
        LOG.info("Fetching all books");
        return Response.ok(new ArrayList<>(books.values())).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") int id) {
        Book book = books.get(id);
        if (book == null) {
            LOG.warning("Book with ID " + id + " not found");
            throw new BookNotFoundException("Book with ID " + id + " not found");
        }
        LOG.info("Fetched book with ID: " + id);
        return Response.ok(book).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") int id, Book updatedBook) {
        Book existingBook = books.get(id);
        if (existingBook == null) {
            LOG.warning("Book with ID " + id + " not found for update");
            throw new BookNotFoundException("Book with ID " + id + " not found");
        }
        
        validateBook(updatedBook);
        
        updatedBook = new Book(id, updatedBook.getTitle(), updatedBook.getAuthor(), updatedBook.getIsbn(), updatedBook.getPublicationYear(), updatedBook.getPrice(), updatedBook.getStockQuantity());
        books.put(id, updatedBook);
        LOG.info("Updated book with ID: " + id);
        return Response.ok(updatedBook).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id){
        Book removed = books.remove(id);
        if (removed == null) {
            LOG.warning("Book with ID " + id + " not found for deletion");
            throw new BookNotFoundException("Book with ID " + id + " not found");
        }
        LOG.info("Book deleted successfully for ID: " + id);
        return Response.noContent().build();
    }
    
    public static Map<Integer, Book> getBooks() {
        return books;
    }   
    
    public void validateBook(Book book){
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            LOG.warning("Invalid input: Title is missing");
            throw new InvalidInputException("Title must be provided");
        }
        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            LOG.warning("Invalid input: Author is missing");
            throw new InvalidInputException("Author must be provided");
        }
        if (book.getIsbn() == null || book.getIsbn().isBlank()) {
            LOG.warning("Invalid input: ISBN is missing");
            throw new InvalidInputException("ISBN must be provided");
        }
        if(book.getPublicationYear()<=0||book.getPublicationYear() > Calendar.getInstance().get(Calendar.YEAR)){
            LOG.warning("Invalid input: Publication year not positive");
            throw new InvalidInputException("Publication Year should be valid");
        }
        if (book.getPrice() <= 0) {
            LOG.warning("Invalid input: Price not positive");
            throw new InvalidInputException("Price should be positive");
        }
        if (book.getStockQuantity() <= 0) {
            LOG.warning("Invalid input: Stock quantity not positive");
            throw new InvalidInputException("Stock quantity cannot be zero/negative");
        }
    }  
    
    private Integer findBookIdByTitle(String title) {
        for (Map.Entry<Integer, Book> entry : BookResource.getBooks().entrySet()) {
            if (entry.getValue().getTitle().equalsIgnoreCase(title)) {
                return entry.getKey();
            }
        }
        return 0;
    }
}
