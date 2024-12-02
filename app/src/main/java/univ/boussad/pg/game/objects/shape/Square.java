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
package univ.boussad.pg.game.objects.shape;
import univ.boussad.pg.game.objects.Shape;
import univ.boussad.pg.game.objects.color.Coloration;

public class Square extends Shape {

    public Square(float[] pos, float scale, Coloration coloration) {
        super(pos,
                new float[]{
                -1.0f, 1.0f, 0.0f,
                -1.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f,
                1.f, 1.f, 0.0f},

                new short[]
                        {0, 1, 2,
                        0, 2, 3}, scale, coloration);
    }
}
