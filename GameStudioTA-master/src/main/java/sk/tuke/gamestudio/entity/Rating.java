package sk.tuke.gamestudio.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(uniqueConstraints =
        {@UniqueConstraint(name = "UniqueGameAndUsername", columnNames = {"game", "username"})})
public class Rating {
    @Id
    @GeneratedValue
    private long ident;

    @Column(nullable = false, length = 64)
    private String game;

    @Column(nullable = false, length = 64)
    private String username;

    @Column(columnDefinition = "INT CHECK(rating BETWEEN 1 AND 5) NOT NULL")
    private int rating;

    @Column(nullable = false)
    private Date ratedOn;

    public Rating() {
    }

    public Rating(String game, String username, int rating, Date ratedOn) {
        this.game = game;
        this.username = username;
        this.rating = rating;
        this.ratedOn = ratedOn;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "ident=" + ident +
                ", game='" + game + '\'' +
                ", username='" + username + '\'' +
                ", rating=" + rating +
                ", ratedOn=" + ratedOn +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getRatedOn() {
        return ratedOn;
    }

    public void setRatedOn(Date ratedOn) {
        this.ratedOn = ratedOn;
    }
}
