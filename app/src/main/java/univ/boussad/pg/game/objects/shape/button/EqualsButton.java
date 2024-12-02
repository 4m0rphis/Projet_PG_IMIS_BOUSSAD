package univ.boussad.pg.game.objects.shape.button;

import android.view.MotionEvent;

import univ.boussad.pg.game.Bet;
import univ.boussad.pg.game.PGGame;
import univ.boussad.pg.game.objects.Shape;
import univ.boussad.pg.game.objects.color.Coloration;
import univ.boussad.pg.game.objects.shape.TouchResult;

public class EqualsButton extends Shape {
    public EqualsButton(float[] pos, float scale, Coloration coloration) {
        super(
                pos,
                new float[] {
                        -1,.5f,0,
                        1,.5f,0,
                        1,.25f,0,
                        -1,.25f,0,

                        -1,-.5f,0,
                        1,-.5f,0,
                        1,-.25f,0,
                        -1,-.25f,0,
                },
                new short[] {
                        0,1,2,
                        2,3,0,

                        4,5,6,
                        6,7,4,
                }, scale, coloration);
    }

    @Override
    public TouchResult onShapeTouch(MotionEvent event, float glX, float glY) {
        PGGame.instance().playerPlacedBet(Bet.EQUALS);
        return TouchResult.RERENDER;
    }
}