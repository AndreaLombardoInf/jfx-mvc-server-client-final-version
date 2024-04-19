package com.example.jfx;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The {@code Message} class defines a simple message model used for communication between client and server.
 * It encapsulates various types of messages such as text content, lists of products, user credentials, and suggestions.
 */
public final class Message implements Serializable
{

    private final String content;
    private final List<Product> list;
    private final Map<String,String> user;
    private Product product;
    private String suggestions;
    private User userName;


    /**
     * Constructs a message containing a list of products.
     *
     * @param l The list of products.
     */
    public Message( final List<Product> l) {
        this.product = null;
        this.user = null;
        this.content = null;
        this.list = l;
    }


    /**
     * Constructs a message with text content.
     *
     * @param c The content of the message.
     */
    public Message( final String c) {
        this.product = null;
        this.user = null;
        this.content = c;
        this.list = null;
    }


    /**
     * Constructs a message with text content and a product.
     *
     * @param c The content of the message.
     * @param product The product contained in the message.
     */
    public Message( final String c, final Product product) {
        this.user = null;
        this.content = c;
        this.product = product;
        this.list = null;
    }


    /**
     * Constructs a Message object with specified parameters.
     *
     * @param c The content type of the message.
     * @param s The suggestion message.
     * @param u The user associated with the message.
     */
    public Message(final String c, final String s, final User u) {
        this.user = null;
        this.product = null;
        this.userName = u;
        this.content = c;
        this.list = null;
        this.suggestions = s;
    }

    /**
     * Constructs a message with a map of user credentials.
     *
     * @param l The map containing user credentials.
     */
    public Message(final Map<String,String> l) {
        this.content = null;
        this.list = null;
        this.user = l;
    }


    /**
     * Gets the content of the message.
     *
     * @return The content of the message.
     */
    public String getContent() {
        return this.content;
    }


    /**
     * Gets the list of products contained in the message.
     *
     * @return The list of products.
     */
    public List<Product> getList() {
        return this.list;
    }


    /**
     * Gets the product contained in the message.
     *
     * @return The product.
     */
    public Product getProduct() {
        return this.product;
    }


    /**
     * Retrieves the user associated with this message.
     *
     * @return The user associated with this message.
     */
    public User getUser() {
        return this.userName;
    }


    /**
     * Gets the map of user credentials contained in the message.
     *
     * @return The map of user credentials.
     */
    public Map<String,String> getMap() {
        return this.user;
    }


    /**
     * Gets the suggestions contained in the message.
     *
     * @return The suggestions.
     */
    public String getSuggestions() {
        return this.suggestions;
    }

}