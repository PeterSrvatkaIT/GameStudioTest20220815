package sk.tuke.gamestudio.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Occupation implements Serializable {
    @Id
    @GeneratedValue
    private long ident;
    @Column(nullable = false, length = 32, unique = true)
    private String occupation;
    @OneToMany(mappedBy = "ident")
    private List<Player> players;

    // ziak, student, zamestnanec, zivnostnik, nezamestnany, dochodca, invalid
    public Occupation() {
    }


    public Occupation(String occupation) {
        this.occupation = occupation;
    }

    @Override
    public String toString() {
        return "Occupation{" +
                "ident=" + ident +
                ", occupation='" + occupation + '\'' +
                '}';
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}

