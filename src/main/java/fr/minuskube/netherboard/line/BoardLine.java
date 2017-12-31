package fr.minuskube.netherboard.line;

import fr.minuskube.netherboard.impl.BoardLineImpl;
import fr.minuskube.netherboard.text.TextAnimation;

import java.util.Optional;

public interface BoardLine {

    boolean hasAnimation();
    Optional<TextAnimation> getAnimation();

    String getText();

    static BoardLine of(String text) { return new BoardLineImpl(text); }
    static BoardLine of(TextAnimation animation) { return new BoardLineImpl(animation); }

}
