package univ.boussad.pg.game.objects.shape;

import univ.boussad.pg.game.objects.color.Coloration;
import univ.boussad.pg.game.objects.Shape;

public class Pentagon extends Shape {

    public Pentagon(float[] pos, float scale, Coloration coloration) {
        super(pos, new float[]{
                0.0f,       1.0f,     0.0f,
                -1f,  0.3f,  0.0f,
                -0.5f, -1f,  0.0f,
                0.5f,  -1f,  0.0f,
                1f,   0.3f,  0.0f
        },
                new short[]{
                        0, 1, 2,
                        0,2,3,
                        0,3,4
                },scale,coloration);
    }
}
