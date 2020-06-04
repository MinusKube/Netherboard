package fr.minuskube.netherboard;

import fr.minuskube.netherboard.line.BoardLine;
import fr.minuskube.netherboard.text.BlinkTextAnimation;
import fr.minuskube.netherboard.text.TextAnimation;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardLineTest {

    @Test
    public void testBoardLine() {
        final String text = "This is a test";
        final TextAnimation animation = BlinkTextAnimation.builder().build();

        BoardLine textLine = BoardLine.of(text);
        BoardLine animationLine = BoardLine.of(animation);

        assertNotNull("The created Text BoardLine is null.", textLine);
        assertNotNull("The created Animation BoardLine is null.", animationLine);

        assertFalse("The Text BoardLine hasAnimation() does not return false.", textLine.hasAnimation());
        assertTrue("The Animation BoardLine hasAnimation() does not return true.", animationLine.hasAnimation());

        assertEquals("The Text BoardLine getText() doesn't match the given text.", text, textLine.getText());
        assertEquals("The Animation BoardLine getText() doesn't match the given text.",
                animation.getCurrentText(), animationLine.getText());

        assertEquals("The Animation BoardLine getAnimation() doesn't match the given animation.",
                animation, animationLine.getAnimation().get());
    }

}
