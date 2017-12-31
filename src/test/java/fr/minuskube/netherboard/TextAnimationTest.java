package fr.minuskube.netherboard;

import fr.minuskube.netherboard.text.BlinkTextAnimation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TextAnimationTest {

    @Test
    public void blinkTest() {
        final int interval = 20;
        final String firstFrameValue = "First Test";
        final String secondFrameValue = "Second Test";

        BlinkTextAnimation blinkAnimation = BlinkTextAnimation.builder()
                .interval(interval)
                .property(BlinkTextAnimation.FIRST_FRAME_PROPERTY, firstFrameValue)
                .property(BlinkTextAnimation.SECOND_FRAME_PROPERTY, secondFrameValue)
                .build();

        assertNotNull( "The built animation is null.", blinkAnimation);
        assertEquals("The animation interval is wrong.", interval, blinkAnimation.getInterval());

        assertEquals("The animation first frame property is wrong.", firstFrameValue,
                blinkAnimation.getProperty(BlinkTextAnimation.FIRST_FRAME_PROPERTY));
        assertEquals("The animation second frame property is wrong.", secondFrameValue,
                blinkAnimation.getProperty(BlinkTextAnimation.SECOND_FRAME_PROPERTY));

        assertEquals("The first animation's frame is not the expected one.",
                firstFrameValue, blinkAnimation.getCurrentText());

        blinkAnimation.update();

        assertEquals("The second animation's frame is not the expected one.",
                secondFrameValue, blinkAnimation.getCurrentText());

        blinkAnimation.update();

        assertEquals("The third animation's frame is not the expected one.",
                firstFrameValue, blinkAnimation.getCurrentText());
    }

}
