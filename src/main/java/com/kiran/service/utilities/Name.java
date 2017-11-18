package com.kiran.service.utilities;

/**
 * @author Kiran
 * @since 11/17/17
 */
public enum Name {
    KIRAN("Ki", "Ran"),
    GARIMA("Garima","G."),
    ANDRII("Andrii","Z."),
    URVI("Urvi","P."),
    GANESH("Ganesh","M."),
    SAADAT("Saadat","I."),
    PETER("Peter","P.");

    private String firstName;
    private String lastName;

    Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
