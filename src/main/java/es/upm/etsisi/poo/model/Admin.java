package es.upm.etsisi.poo.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Admin")
public class Admin extends User {
    @Column(name = "Forename", nullable = false)
    private String forename;
    @Column(name = "Surname", nullable = false)
    private String surname;

    public Admin(String username, String pwd, String forename, String surname) {
        super(username, pwd);
        this.forename = forename;
        this.surname = surname;
    }

    public Admin() {
        super(null, null);
    }

    public String getForename() {
        return forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
