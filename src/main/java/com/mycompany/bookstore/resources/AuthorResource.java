/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstore.resources;

/**
 *
 * @author LOQ
 */
import com.mycompany.exceptions.AuthorNotFoundException;
import com.mycompany.exceptions.InvalidInputException;
import com.mycompany.models.Author;
import com.mycompany.models.Book;
import java.util.logging.Logger;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    private static final Logger logger = Logger.getLogger(AuthorResource.class.getName());
    
    private static Map<Integer, Author> authors = new HashMap<>();
    private static Map<Integer, Book> books = BookResource.getBooks(); // Access shared books map
    private static int authorIdCounter = 1;
    
    public AuthorResource(){}
    
    @POST
    public Response addAuthor(Author input) {
        if (input.getName() == null || input.getName().isBlank()) {
            logger.warning("Failed to add author : missing name.");
            throw new InvalidInputException("Author name is required");
        }
        if (input.getBiography() == null || input.getBiography().isBlank()) {
            logger.warning("Failed to add author : missing biography.");
            throw new InvalidInputException("Biography is required");
        }

        int existingId = findAuthorIdByName(input.getName());
        if (existingId != 0) {
            logger.info("Author already exists with ID: " + existingId);
            return Response.ok(authors.get(existingId)).build(); 
        }

        int id = authorIdCounter++;
        Author author = new Author(id, input.getName(), input.getBiography());
        authors.put(id, author);
        logger.info("Author added with ID: " + id);
        return Response.status(Response.Status.CREATED).entity(author).build(); 
    }


    
    @GET
    public Response getAllAuthors() {
        logger.info("Fetching the authors list.");
        return Response.ok(new ArrayList<>(authors.values())).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getAuthorById(@PathParam("id") int id) {
        Author author = authors.get(id);
        if (author == null) {
            logger.warning("Author not found for ID: " + id);
            throw new AuthorNotFoundException("Author with ID " + id + " not found");
        }
        logger.info("Fetched author with ID: " + id);
        return Response.ok(author).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") int id, Author updatedAuthor) {
        Author existing = authors.get(id);
        if (existing == null) {
            logger.warning("Failed to update author with ID: " + id);
            throw new AuthorNotFoundException("Author with ID " + id + " not found");
        }
        if (existing.getName() == null || existing.getName().isBlank()) {
            throw new InvalidInputException("Author name is required");
        }
        if (existing.getBiography()==null||existing.getBiography().isBlank()){
            throw new InvalidInputException("Biography is required");
        }
        
        updatedAuthor = new Author(id, updatedAuthor.getName(),updatedAuthor.getBiography());
        authors.put(id, updatedAuthor);
        logger.info("Author updated with ID: " + id);
        return Response.ok(updatedAuthor).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        Author removed = authors.remove(id);
        if (removed == null) {
            logger.warning("Failed to delete author with ID: " + id);
            throw new AuthorNotFoundException("Author with ID " + id + " not found");
        }
        logger.info("Deleted author with ID: " + id);
        return Response.noContent().build();
    }
    
    @GET
    @Path("/{id}/books")
    public Response getBooksByAuthor(@PathParam("id") int id) {
        Author author = authors.get(id);
        if (author == null) {
            logger.warning("Author not found with ID: " + id);
            throw new AuthorNotFoundException("Author with ID " + id + " not found");
        }

        String authorName = author.getName();
        List<Book> booksOfAuthor = new ArrayList<>();
        for (Book book : BookResource.getBooks().values()) {
            if (authorName.equals(book.getAuthor())) {
                booksOfAuthor.add(book);
            }
        }

        if (booksOfAuthor.isEmpty()) {
            logger.info("No books found for author ID: " + id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Collections.singletonMap("message", "This author has no books published yet."))
                    .build();
        }

        logger.info("Fetched books for author ID: " + id);
        return Response.ok(booksOfAuthor).build();
    }

    
    private int findAuthorIdByName(String name) {
        for (Map.Entry<Integer, Author> entry : authors.entrySet()) {
            if (entry.getValue().getName().equalsIgnoreCase(name)) {
                return entry.getKey();
            }
        }
        return 0;
    }


}
