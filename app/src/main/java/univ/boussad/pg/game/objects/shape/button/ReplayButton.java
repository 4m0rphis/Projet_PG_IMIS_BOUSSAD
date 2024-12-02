package univ.boussad.pg.game.objects.shape.button;

import android.view.MotionEvent;
import univ.boussad.pg.game.PGGame;
import univ.boussad.pg.game.objects.Shape;
import univ.boussad.pg.game.objects.color.Coloration;
import univ.boussad.pg.game.objects.shape.TouchResult;

public class ReplayButton extends Shape {
    public ReplayButton(float[] pos, float scale, Coloration color) {
        super(pos, new float[] {
                        -1,1,0,
                        1,0,0,
                        -1,-1,0
                },
                new short[] {
                        0,1,2,
                }, scale, color);
    }

    @Override
    public TouchResult onShapeTouch(MotionEvent event, float glX, float glY) {
        PGGame.instance().startNewGame();
        return TouchResult.RERENDER;
    }
}
