package com.example.jfx;

import java.io.Serializable;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.Map;

/**
 *
 * The class {@code Message} defines a simple message model.
 *
 **/

public final class Message implements Serializable
{
    private static final long serialVersionUID = 1L;


    private String content;
    private List<Product> lista;

    private Map<String,String> utenti;

    private Product product;



    public Message( final List<Product> l)
    {
        this.product = null;
        this.utenti = null;
        this.content = null;
        this.lista = l;

    }
    public Message( final String c)
    {
        this.product = null;
        this.utenti = null;
        this.content = c;
        this.lista = null;

    }
    public Message( final String c, final Product product)
    {
        this.utenti = null;
        this.content = c;
        this.product = product;
        this.lista = null;

    }
    public Message( final String c,final Map<String,String> l)
    {
        this.content = c;
        this.lista = null;
        this.utenti = l;

    }

    public Message(final Map<String,String> l)
    {
        this.content = null;
        this.lista = null;
        this.utenti = l;

    }

    public String getContent()
    {
        return this.content;
    }

    public List<Product> getList(){
        return this.lista;
    }
    public Product getProduct(){return this.product;}
    public Map<String,String> getMap(){
        return this.utenti;
    }


}
