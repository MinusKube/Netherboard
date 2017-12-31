package fr.minuskube.netherboard.text;

import com.google.common.collect.ImmutableMap;

public class BlinkTextAnimation extends AbstractTextAnimation {

    public static final Property<BlinkTextAnimation, String> FIRST_FRAME_PROPERTY = new Property<>();
    public static final Property<BlinkTextAnimation, String> SECOND_FRAME_PROPERTY = new Property<>();

    private byte state = 0;

    protected BlinkTextAnimation() {
        super(20, ImmutableMap.of(
                FIRST_FRAME_PROPERTY, "Potato",
                SECOND_FRAME_PROPERTY, ""
        ));
    }

    @Override
    public String getCurrentText() {
        return state == 0
                ? getProperty(FIRST_FRAME_PROPERTY)
                : getProperty(SECOND_FRAME_PROPERTY);
    }

    @Override
    public void update() {
        state = (byte) (++state % 2);
    }

    public static Builder<BlinkTextAnimation> builder() {
        return TextAnimation.builder(BlinkTextAnimation.class);
    }

}
