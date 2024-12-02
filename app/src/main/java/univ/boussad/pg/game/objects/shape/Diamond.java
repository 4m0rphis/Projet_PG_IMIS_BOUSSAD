package univ.boussad.pg.game.objects.shape;

import univ.boussad.pg.game.objects.color.Coloration;
import univ.boussad.pg.game.objects.Shape;

public class Diamond extends Shape {

    public Diamond(float[] pos, float scale, Coloration coloration) {
        super(
                pos,
                new float[] {
                        0,1,0,
                        -1,0,0,
                        1,0,0,
                        0,-1,0
                        },
                new short[] {
                        0,1,3,
                        0,2,3
                }, scale, coloration);
    }
}
