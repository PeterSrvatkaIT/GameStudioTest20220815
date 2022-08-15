package sk.tuke.gamestudio.entity;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueUserNameAndFullName", columnNames = {"userName", "fullName"})})
public class Player {

    @Id
    @GeneratedValue
    private long ident;

    @Column(nullable = false, length = 32)
    private String userName;

    @Column(nullable = false, length = 128)
    private String fullName;

    @Column(columnDefinition = "INT CHECK(selfEvaluation BETWEEN 1 AND 10) NOT NULL")
    private int selfEvaluation;

    @ManyToOne
    @JoinColumn(name = "Country.ident")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "Occupation.ident")
    private Occupation occupation;

    public Player() {
    }

    public Player(String userName, String fullName, int selfEvaluation, Country country, Occupation occupation) {
        this.userName = userName;
        this.fullName = fullName;
        this.selfEvaluation = selfEvaluation;
        this.country = country;
        this.occupation = occupation;
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

    public int getSelfEvaluation() {
        return selfEvaluation;
    }

    public Country getCountry() {
        return country;
    }

    public Occupation getOccupation() {
        return occupation;
    }
}
