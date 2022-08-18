package sk.tuke.gamestudio.entity;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueUsernameAndFullname", columnNames = {"userName", "fullName"})})
public class Player {

    @Id
    @GeneratedValue
    private long ident;

    @Column(nullable = false, length = 32)
    private String userName;

    @Column(nullable = false, length = 128)
    private String fullName;

    @Column(columnDefinition = "INT CHECK(player.self_evaluation BETWEEN 1 AND 10) NOT NULL")
    private int selfEvaluation;

    @ManyToOne
    @JoinColumn(name = "Country.country", nullable = false)
    private Country country;

    @ManyToOne
    @JoinColumn(name = "Occupation.occupation", nullable = false)
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

    @Override
    public String toString() {
        return "Player{" +
                "userName='" + userName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", selfEvaluation=" + selfEvaluation +
                ", country=" + country +
                ", occupation=" + occupation +
                '}';
    }
}
