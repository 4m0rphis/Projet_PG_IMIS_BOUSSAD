package univ.boussad.pg.game.objects.shape;

import univ.boussad.pg.game.objects.color.Coloration;
import univ.boussad.pg.game.objects.Shape;

public class Hourglass extends Shape {

    public Hourglass(float[] pos, float scale,  Coloration coloration) {
        super(
                pos,
                new float[] {
                        0,0,0,
                        -1f,1f,0,
                        1f,1f,0,
                        -1f,-1f,0,
                        1f,-1f,0,
                },

                new short[] {
                        0,1,2,
                        0,3,4

                }, scale, coloration);
    }

}
