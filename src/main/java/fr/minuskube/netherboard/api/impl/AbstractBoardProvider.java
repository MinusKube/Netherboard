package fr.minuskube.netherboard.api.impl;

import fr.minuskube.netherboard.api.BoardProvider;
import fr.minuskube.netherboard.api.PlayerBoard;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractBoardProvider<V, S, N> implements BoardProvider<V, S, N> {

    protected Map<UUID, PlayerBoard<V, S, N>> boards = new HashMap<>();

    @Override
    public PlayerBoard<V, S, N> newBoard(Object player) {
        deleteBoard(player);

        UUID uuid = getUniqueId(player);
        PlayerBoard<V, S, N> board = createBoard(uuid);

        boards.put(uuid, board);
        return board;
    }

    @Override
    public Optional<PlayerBoard<V, S, N>> getBoard(Object player) {
        UUID uuid = getUniqueId(player);
        return Optional.ofNullable(boards.get(uuid));
    }

    @Override
    public boolean hasBoard(Object player) {
        return getBoard(player).isPresent();
    }

    @Override
    public boolean deleteBoard(Object player) {
        UUID uuid = getUniqueId(player);
        Optional<PlayerBoard<V, S, N>> board = getBoard(uuid);

        if(!board.isPresent())
            return false;

        board.get().delete();
        boards.remove(uuid);
        return true;
    }

    protected abstract PlayerBoard<V, S, N> createBoard(UUID uuid);

    protected UUID getUniqueId(Object player) {
        if(player instanceof UUID)
            return (UUID) player;

        throw new IllegalArgumentException("The given player type is not supported. " +
                "It must be at least one these types: Player, UUID, String (the player's name)");
    }

}
