package sk.tuke.gamestudio.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

public class Occupation implements Serializable {
    // ziak, student, zamestnanec, zivnostnik, nezamestnany, dochodca, invalid

    @Id
    @GeneratedValue
    private long ident;

    @Column(nullable = false, length = 32, unique = true)
    private String occupation;

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

