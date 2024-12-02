package univ.boussad.pg.game.objects.shape;


import android.view.MotionEvent;

import univ.boussad.pg.game.objects.color.Coloration;
import univ.boussad.pg.game.objects.Shape;

public class Hexagon extends Shape {

    public Hexagon(float[] pos, float scale, Coloration coloration) {
        super(pos, new float[]{
                        -1.0f, 0.0f, 0.0f,
                        -0.5f, (float) Math.sqrt(3) * 0.5f, 0.0f,
                        0.5f, (float) Math.sqrt(3) * 0.5f, 0.0f,
                        1.0f, 0.0f, 0.0f,
                        0.5f, -(float) Math.sqrt(3) * 0.5f, 0.0f,
                        -0.5f, -(float) Math.sqrt(3) * 0.5f, 0.0f
                },
                new short[]{
                        0, 1, 5,
                        1, 2, 5,
                        2, 3, 5,
                        3, 4, 5,
                        4, 0, 5,
                        0, 1, 4
                },scale,coloration);
    }
}
