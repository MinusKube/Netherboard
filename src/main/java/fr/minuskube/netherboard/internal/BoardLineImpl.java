package fr.minuskube.netherboard.internal;

import fr.minuskube.netherboard.line.BoardLine;
import fr.minuskube.netherboard.text.TextAnimation;

import java.util.Optional;

public class BoardLineImpl implements BoardLine {

    private TextAnimation animation;
    private String text;

    public BoardLineImpl(String text) {
        this.text = text;
    }

    public BoardLineImpl(TextAnimation animation) {
        this.animation = animation;
    }

    @Override
    public boolean hasAnimation() {
        return animation != null;
    }

    @Override
    public Optional<TextAnimation> getAnimation() {
        return Optional.ofNullable(animation);
    }

    @Override
    public String getText() {
        return hasAnimation() ? animation.getCurrentText() : text;
    }

}
