package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Occupation;

import java.util.List;

public interface OccupationService {

    void addOccupation(Occupation occupation);

    List<Occupation> getOccupation(String game);

    public void reset();

    List<Occupation> getOccupations();
}
