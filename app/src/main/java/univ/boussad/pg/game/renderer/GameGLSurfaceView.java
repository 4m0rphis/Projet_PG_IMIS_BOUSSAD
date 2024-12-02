/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package univ.boussad.pg.game.renderer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Pair;
import android.view.MotionEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import univ.boussad.pg.game.PGGame;
import univ.boussad.pg.game.objects.Shape;
import univ.boussad.pg.game.objects.shape.TouchResult;

public class GameGLSurfaceView extends GLSurfaceView {

    private final GameRenderer mRenderer;

    public GameGLSurfaceView(Context context) {
        super(context);

        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setEGLContextClientVersion(3);

        mRenderer = new GameRenderer();
        setRenderer(mRenderer);

        PGGame.RERENDER_ACTION_TRIGGER = this::requestRender;

        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private final Map<Shape, Pair<float[],Boolean>> shapeStates = new HashMap<>(); // PREV X, PREV Y, CONDITION

    private void changeOrPut(Shape shape, float[] pos) {

        boolean condition = Objects.requireNonNull(shapeStates.getOrDefault(shape, new Pair<>(null, false))).second;
        shapeStates.put(shape,new Pair<>(pos,condition));
    }

    private void changeOrPut(Shape shape, boolean condition) {
        float[] pos = Objects.requireNonNull(shapeStates.getOrDefault(shape, new Pair<>(new float[]{0f,0f}, false))).first;
        shapeStates.put(shape,new Pair<>(pos,condition));
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        float x_opengl = 20.0f * x / getWidth() - 10.0f;
        float y_opengl = -20.0f * y / getHeight() + 10.0f;

        synchronized (mRenderer.getShapes()) {
            for (var shape : mRenderer.getShapes()) {

                float[] pos = shape.getPosition();

                boolean test_square = ((x_opengl < pos[0] + 1.0) && (x_opengl > pos[0] - 1.0) && (y_opengl < pos[1] + 1.0) && (y_opengl > pos[1] - 1.0));
                boolean condition = Objects.requireNonNull(shapeStates.getOrDefault(shape, new Pair<>(null, false))).second;
                if (condition || test_square) {

                    switch (e.getAction()) {
                        case MotionEvent.ACTION_DOWN -> {
                            changeOrPut(shape, new float[]{x, y});
                            changeOrPut(shape, true);
                        }
                        case MotionEvent.ACTION_UP -> {
                            if (shape.onShapeTouch(e, x_opengl, y_opengl).equals(TouchResult.RERENDER))
                                requestRender();

                            changeOrPut(shape, false);
                        }
                    }
                }
            }
        }

        return true;
    }

}
