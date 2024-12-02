package univ.boussad.pg.game.objects.shape.button;

import android.view.MotionEvent;

import univ.boussad.pg.game.Bet;
import univ.boussad.pg.game.PGGame;
import univ.boussad.pg.game.objects.Shape;
import univ.boussad.pg.game.objects.color.Coloration;
import univ.boussad.pg.game.objects.shape.TouchResult;

public class LessThanButton extends Shape {

    public LessThanButton(float[] pos, float scale, Coloration coloration) {
        super(
                pos,
                new float[] {
                        1,1,0,
                        -1,0,0,
                        1,-1,0
                },
                new short[] {
                        0,1,2,
                }, scale, coloration);
    }

    @Override
    public TouchResult onShapeTouch(MotionEvent event, float glX, float glY) {
        PGGame.instance().playerPlacedBet(Bet.LESS_THAN);
        return TouchResult.RERENDER;
    }
}
