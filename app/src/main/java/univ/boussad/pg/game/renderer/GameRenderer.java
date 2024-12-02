package univ.boussad.pg.game.renderer;

import android.graphics.Color;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.*;
import java.util.stream.Collectors;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import univ.boussad.pg.game.PGGame;
import univ.boussad.pg.game.objects.Shape;
import univ.boussad.pg.game.objects.color.Coloration;
import univ.boussad.pg.game.objects.shape.HollowRect;
import univ.boussad.pg.game.objects.shape.button.ReplayButton;

public final class GameRenderer implements GLSurfaceView.Renderer {

    private Map<Shape, List<DrawingModifier>> SHAPES_TO_RENDER = new HashMap<>();
    private final float[] mProjectionMatrix = new float[16];

    @SuppressWarnings("unused")
    private static final String TAG = "PGGameRenderer";

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Matrix.orthoM(mProjectionMatrix, 0, -10.0f, 10.0f, -10.0f, 10.0f, -0.5f, 0.5f);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        SHAPES_TO_RENDER = new HashMap<>(); // "Resets" the map to not render objects on top of each other.
        PGGame.instance().drawInterface(this);

        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        for (var shape :
                this.SHAPES_TO_RENDER
                .keySet()
                .stream()
                .sorted(Comparator.comparingInt(obj -> obj instanceof ReplayButton ? 1 : 0))
                .collect(Collectors.toCollection(LinkedHashSet::new))) {

            float[] scratch = new float[16];
            float[] mModelMatrix = new float[16];
            float[] mMVPMatrix = new float[16];
            float[] mViewMatrix = new float[16];

            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.setIdentityM(mViewMatrix, 0);
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

            float[] mPosition = shape.getPosition();
            Matrix.translateM(mModelMatrix, 0, mPosition[0], mPosition[1], 0);
            Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mModelMatrix, 0);
            shape.draw(scratch);

            var modifiers = SHAPES_TO_RENDER.getOrDefault(shape, List.of());

            //noinspection DataFlowIssue
            if (modifiers.contains(DrawingModifier.DRAW_WITH_BOX) || modifiers.contains(DrawingModifier.DRAW_WITH_BOX_SAME_COLOR)) {
                new HollowRect(shape.getPosition(), .9f, 1, modifiers.contains(DrawingModifier.DRAW_WITH_BOX) ? new Coloration(Color.valueOf(255, 140, 0), 0f) : shape.coloration).draw(scratch);
            }
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    public void addShape(Shape shape, List<DrawingModifier> modifiers) {
        SHAPES_TO_RENDER.put(shape, modifiers);
    }

    public Set<Shape> getShapes() {
        return SHAPES_TO_RENDER.keySet();
    }
}