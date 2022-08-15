package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Player;

import java.util.List;

public interface PlayerService {

    void addPlayer(Player player);

    List<Player> getPlayersByUserName(String game);

    void reset();
}
