package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) {
        Rating rating2Write = null;

        try {
            rating2Write = (Rating) entityManager
                    .createQuery("SELECT r from Rating r where r.username = :user and r.game = :game")
                    .setParameter("user", rating.getUsername())
                    .setParameter("game", rating.getGame())
                    .getSingleResult();
            rating2Write.setRating(rating.getRating());
            rating2Write.setRatedOn(rating.getRatedOn());
        } catch (NoResultException e) {
            entityManager.persist(new Rating(rating.getGame(), rating.getUsername(), rating.getRating(), rating.getRatedOn()));
        }
    }

    @Override
    public int getAverageRating(String game) {
        try {
            return ((Number) entityManager
                    .createQuery("SELECT ROUND(AVG(rating)) FROM Rating where game= :myGame")
                    .setParameter("myGame", game)
                    .getSingleResult())
                    .intValue();
        } catch (NoResultException | NullPointerException e) {
            return 0;
        }
    }

    @Override
    public int getRating(String game, String username) {
        try {
            return ((Number) entityManager
                    .createQuery("SELECT rating FROM Rating WHERE game= :myGame and username= :myUser")
                    .setParameter("myGame", game)
                    .setParameter("myUser", username)
                    .getSingleResult())
                    .intValue();
        } catch (NoResultException | NullPointerException e) {
            return 0;
        }
    }

    @Override
    public void reset() {
        entityManager
                .createNativeQuery("DELETE FROM rating")
                .executeUpdate();
    }
}