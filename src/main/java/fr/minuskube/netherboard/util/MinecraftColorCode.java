package fr.minuskube.netherboard.util;

import com.google.common.base.Preconditions;

public class MinecraftColorCode {

    private static final char[] COLOR_CODES = {
            '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f',
            'k', 'l', 'm', 'n', 'o', 'r'
    };

    public static char fromIndex(int index) {
        Preconditions.checkPositionIndex(index, COLOR_CODES.length, "Invalid color code index");

        return COLOR_CODES[index];
    }

}
