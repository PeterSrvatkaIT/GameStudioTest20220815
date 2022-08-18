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
//    insert into occupation(ident,occupation)
//    values
//            (1,'ziak'),
//(2,'student'),
//        (3,'zamestnanec'),
//        (4,'zivnostnik'),
//        (5,'nezamestnany'),
//        (6,'dochodca'),
//        (7,'invalid')







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

