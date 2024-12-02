package univ.boussad.pg.game.objects.color;

import android.graphics.Color;

public final class Coloration {

    private final Color colorToUse;
    private final float darkness;

    public Coloration(Color colorToUse, float darkness) {
        this.colorToUse = colorToUse;
        this.darkness = darkness;
    }

    public float[] applyOn(int points) {

        float[] newColors = new float[points * 4]; // R G B A
        float normalizedDarkness = Math.min(darkness * 255.0f, 200.0f);

        for (int i = 0; i < newColors.length; i += 4) {
            newColors[i] = convertRGBtoGL(Math.max(colorToUse.red() - normalizedDarkness, 0f));
            newColors[i + 1] = convertRGBtoGL(Math.max(colorToUse.green() - normalizedDarkness, 0f));
            newColors[i + 2] = convertRGBtoGL(Math.max(colorToUse.blue() - normalizedDarkness, 0f));
            newColors[i + 3] = 1f;
        }

        return newColors;
    }

    private static float convertRGBtoGL(float color) {
        return color / 255.0f;
    }
}
