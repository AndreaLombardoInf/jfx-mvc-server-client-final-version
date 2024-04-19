package com.example.jfx;

import java.io.Serializable;


/**
 *
 * The class {@code User} provides a simple user model.
 *
 **/
public final class User implements Serializable
{

    private final String username;
    private final String password;


    /**
     * Class constructor.
     *
     * @param username  the first name.
     * @param password  the password.
     */
    public User(final String username, final String password) {
        this.username = username;
        this.password  = password;
    }



    /**
     * Gets the first name.
     *
     * @return the content.
     *
     **/
    public String getUsername() {
        return this.username;
    }


    /**
     * Gets the password.
     *
     * @return the password.
     *
     **/
    public String getPassword() {
        return this.password;
    }



}
