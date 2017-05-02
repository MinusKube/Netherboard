package fr.minuskube.netherboard.api;

import java.util.Map;

public interface PlayerBoard {

    String get(int score);

    void set(String name, int score);
    void remove(int score);

    void delete();

    String getName();
    void setName(String name);

    Map<Integer, String> getLines();

}
