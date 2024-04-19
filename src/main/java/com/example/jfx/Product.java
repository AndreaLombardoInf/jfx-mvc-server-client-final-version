package com.example.jfx;

import java.io.Serializable;

/**
 * Represents a product with a name, price, and ID.
 */

public class Product implements Serializable{
	
	private static final long serialVersionUID = 1000000L;
	  
    private final String name;
    private final double price;
    private final int id;


    /**
     * Constructs a product with the specified name, price, and ID.
     *
     * @param name  The name of the product.
     * @param price The price of the product.
     * @param id    The ID of the product.
     */
    public Product (String name, double price, int id) {
        this.name = name;
        this.price = price;
        this.id = id;
    }



    /**
     * Gets the name of the product.
     *
     * @return The name of the product.
     */
    public String getName() {
        return this.name;
    }


    /**
     * Gets the price of the product.
     *
     * @return The price of the product.
     */
    public double getPrice() {
        return this.price;
    }


    /**
     * Gets the ID of the product.
     *
     * @return The ID of the product.
     */
    public int getId() {
        return this.id;
    }
}
