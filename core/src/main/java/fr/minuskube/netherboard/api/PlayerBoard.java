package fr.minuskube.netherboard.api;

import java.util.Map;

public interface PlayerBoard<V, N, S> {

    V get(N score);

    void set(V name, N score);
    void remove(N score);

    void delete();

    S getName();
    void setName(S name);

    Map<N, V> getLines();

}
