package fr.minuskube.netherboard;

import fr.minuskube.netherboard.api.text.BlinkTextAnimation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TextAnimationTest {

    @Test
    public void blinkTest() {
        final int interval = 20;
        final String testPropertyValue = "Test";

        BlinkTextAnimation blinkAnimation = BlinkTextAnimation.builder()
                .interval(interval)
                .property(BlinkTextAnimation.TEST_PROPERTY, testPropertyValue)
                .build();

        assertNotNull( "The built animation is null.", blinkAnimation);
        assertEquals("The animation interval is wrong.", interval, blinkAnimation.getInterval());
        assertEquals("The animation property is wrong.", testPropertyValue,
                blinkAnimation.getProperty(BlinkTextAnimation.TEST_PROPERTY));

        assertEquals("The first animation's frame is not the expected one.",
                testPropertyValue, blinkAnimation.getCurrentText());

        blinkAnimation.update();

        assertTrue("The second animation's frame is not empty.",
                blinkAnimation.getCurrentText().isEmpty());

        blinkAnimation.update();

        assertEquals("The third animation's frame is not the expected one.",
                testPropertyValue, blinkAnimation.getCurrentText());
    }

}
