package fr.minuskube.netherboard.api.text;

import com.google.common.collect.ImmutableMap;

public class BlinkTextAnimation extends AbstractTextAnimation {

    // TODO: Add the real properties (basically, the 2 text frames)
    public static final Property<BlinkTextAnimation, String> TEST_PROPERTY = new Property<>();

    private byte state = 0;

    protected BlinkTextAnimation() {
        super(20, ImmutableMap.of(
                TEST_PROPERTY, "Potato"
        ));
    }

    @Override
    public String getCurrentText() {
        return state == 0 ? getProperty(TEST_PROPERTY) : "";
    }

    @Override
    public void update() {
        state = (byte) (++state % 2);
    }

    public static Builder<BlinkTextAnimation> builder() {
        return TextAnimation.builder(BlinkTextAnimation.class);
    }

}
