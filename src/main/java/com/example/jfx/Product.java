package com.example.jfx;

import java.io.Serializable;

public class Product implements Serializable{
	
	private static final long serialVersionUID = 1000000l;
	  
    private String name;
    private double price;
    private int id;

    public Product (String name, double price, int id) {
        this.name = name;
        this.price = price;
        this.id = id;
    }

    @Override
    public String toString() {
        return  name +" "+ price +" "+ id;
    }
    
    public String getName()
    {
      return this.name;
    }
    
    public double getPrice()
    {
      return this.price;
    }
    
    public int getId()
    {
      return this.id;
    }
    
}

