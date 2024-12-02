package univ.boussad.pg.game.objects.shape;

import univ.boussad.pg.game.objects.Shape;
import univ.boussad.pg.game.objects.color.Coloration;

public class Trapezoid extends Shape {

    public Trapezoid(float[] pos, float scale, Coloration coloration) {
        super(pos,
                new float[] {
                        -1.0f,  -1.0f, 0.0f,
                        -0.65f,  0f, 0.0f,
                        .65f,  0f, 0.0f,
                        1.0f,  -1.0f, 0.0f
                },

                new short[] {
                        0,1,2,
                        0,2,3
                }
                ,scale,coloration);
    }
}
