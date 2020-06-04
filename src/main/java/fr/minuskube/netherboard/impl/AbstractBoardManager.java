package fr.minuskube.netherboard.impl;

import com.google.common.base.Preconditions;
import fr.minuskube.netherboard.BoardManager;
import fr.minuskube.netherboard.PlayerBoard;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractBoardManager implements BoardManager {

    protected Map<UUID, PlayerBoard> boards = new HashMap<>();

    @Override
    public PlayerBoard newBoard(Object player) {
        deleteBoard(player);

        UUID uuid = getUniqueId(player);
        PlayerBoard board = createBoard(uuid);

        boards.put(uuid, board);
        return board;
    }

    @Override
    public Optional<PlayerBoard> getBoard(Object player) {
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
        Optional<PlayerBoard> board = getBoard(uuid);

        if(!board.isPresent()) {
            return false;
        }

        board.get().delete();
        boards.remove(uuid);
        return true;
    }

    protected abstract PlayerBoard createBoard(UUID uuid);

    protected UUID getUniqueId(Object player) {
        Preconditions.checkNotNull(player, "The given player must not be null.");

        if(player instanceof UUID) {
            return (UUID) player;
        }

        throw new IllegalArgumentException("The given player type is not supported. " +
                "It must be one of these types: UUID");
    }

}
