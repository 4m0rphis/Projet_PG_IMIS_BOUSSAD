package univ.boussad.pg.game.objects.shape;

import univ.boussad.pg.game.objects.Shape;
import univ.boussad.pg.game.objects.color.Coloration;

public class Triangle extends Shape {

    public Triangle(float[] pos, float scale, Coloration coloration) {
        super(
                pos,
                new float[] {
                        0f, 1.0f, 0.0f,
                        -1.0f, -1.0f, 0.0f,
                        1.0f, -1.0f, 0.0f,
                },
                new short[] {
                        0,1,2
                }, scale, coloration);
    }

}
