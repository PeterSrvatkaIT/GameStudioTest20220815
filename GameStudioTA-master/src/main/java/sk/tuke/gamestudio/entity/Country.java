package sk.tuke.gamestudio.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Country implements Serializable {

    @Id
    @GeneratedValue
    private long ident;

    @Column(nullable = false, length = 128, unique = true)
    private String country;

    public Country() {
    }

    public Country(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Country{" +
                "ident=" + ident +
                ", country='" + country + '\'' +
                '}';
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
