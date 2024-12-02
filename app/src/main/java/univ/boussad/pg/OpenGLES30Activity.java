package univ.boussad.pg;



import android.os.Bundle;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

import univ.boussad.pg.game.renderer.GameGLSurfaceView;

/* Ce tutorial est issu d'un tutorial http://developer.android.com/training/graphics/opengl/index.html :
openGLES.zip HelloOpenGLES20
 */


public class OpenGLES30Activity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new GameGLSurfaceView(this));
    }
}
