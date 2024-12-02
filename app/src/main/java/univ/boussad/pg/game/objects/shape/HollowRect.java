package univ.boussad.pg.game.objects.shape;

import univ.boussad.pg.game.objects.color.Coloration;
import univ.boussad.pg.game.objects.Shape;

public class HollowRect extends Shape {

    public HollowRect(float[] pos, float scale, float thickness, Coloration coloration) {
        this(pos,scale,thickness,coloration,true);
    }

    @SuppressWarnings("DuplicateExpressions")
    public HollowRect(float[] pos, float scale, float thickness, Coloration coloration, boolean b) {
        super(pos,
                new float[]{
                        -1,1,0,
                        1,1,0,
                        1,(b ? (1f/thickness)*.97f : thickness),0,
                        -1,(b ? (1f/thickness)*.97f : thickness),0,
                        -1,-1,0,
                        1,-1,0,
                        1,-(b ? (1f/thickness)*.97f : thickness),0,
                        -1,-(b ? (1f/thickness)*.97f : thickness),0,


                        -(b ? (1f/thickness)*.90f : thickness), -(b ? (1f/thickness)*.97f : thickness),0,
                        (b ? (1f/thickness)*.90f : thickness), -(b ? (1f/thickness)*.97f : thickness),0,

                        -(b ? (1f/thickness)*.90f : thickness), (b ? (1f/thickness)*.97f : thickness),0,

                        (b ? (1f/thickness)*.90f : thickness), (b ? (1f/thickness)*.97f : thickness),0, // 8 - 11 (no sub)
                },

                new short[]
                        {0, 1, 2,
                                0, 2, 3, 4,5,6,
                                4,6,7,
                                0,7,8,
                                0,10,8,

                                1,6,9,
                                1,11,9}, scale,coloration);
    }
}
