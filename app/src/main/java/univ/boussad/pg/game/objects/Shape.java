package univ.boussad.pg.game.objects;

import android.opengl.GLES30;
import android.view.MotionEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import univ.boussad.pg.game.objects.color.Coloration;
import univ.boussad.pg.game.objects.shape.TouchResult;

public class Shape {

    public static final int vertexStride = Shape.COORDS_PER_VERTEX * 4;
    public static final int colorStride = Shape.COLORS_PER_VERTEX * 4;


    public static final String vertexShaderCode =
            """
                    #version 300 es
                    uniform mat4 uMVPMatrix;
                    in vec3 vPosition;
                    in vec4 vCouleur;
                    out vec4 Couleur;
                    out vec3 Position;
                    void main() {
                    Position = vPosition;
                    gl_Position = uMVPMatrix * vec4(vPosition,1.0);
                    Couleur = vCouleur;
                    }
                    """;

    // pour définir la taille d'un float
    public static final String fragmentShaderCode =
            """
                    #version 300 es
                    precision mediump float;
                    in vec4 Couleur;
                    in vec3 Position;
                    out vec4 fragColor;
                    void main() {
                    float x = Position.x;
                    float y = Position.y;
                    float test = x*x+y*y;
                    fragColor = Couleur;
                    }
                    """;


    public static final int COORDS_PER_VERTEX = 3;
    public static final int COLORS_PER_VERTEX = 4;

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer indiceBuffer;
    private final FloatBuffer colorBuffer;
    public final Coloration coloration;

    public int programId;
    private final float[] position;
    private final short[] indices;
    protected static int[] linkStatus = {0};

    protected Shape(float[] position, float[] coords, short[] indices, float scale, Coloration coloration) {
        this.indices = indices;
        this.position = position;

        {
            float[] scaledCoordinates = new float[coords.length];
            for (int i = 0; i < coords.length; i++) {
                scaledCoordinates[i] = coords[i] * scale;
            }

            float[] coords1 = coords = scaledCoordinates;

            int points = coords1.length / 3;
            var colors = coloration.applyOn(points);

            this.coloration = coloration;

            ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 4);
            bb.order(ByteOrder.nativeOrder());
            vertexBuffer = bb.asFloatBuffer();
            vertexBuffer.put(coords);
            vertexBuffer.position(0);


            // initialisation du buffer pour les couleurs (4 bytes par float)
            ByteBuffer bc = ByteBuffer.allocateDirect(colors.length * 4);
            bc.order(ByteOrder.nativeOrder());
            colorBuffer = bc.asFloatBuffer();
            colorBuffer.put(colors);
            colorBuffer.position(0);

            // initialisation du buffer des indices
            ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
            dlb.order(ByteOrder.nativeOrder());
            indiceBuffer = dlb.asShortBuffer();
            indiceBuffer.put(indices);
            indiceBuffer.position(0);
        }

        programId = useShaders();
    }

    private int useShaders() {
        int vertexShader = loadShader(
                GLES30.GL_VERTEX_SHADER,
                Shape.vertexShaderCode);
        int fragmentShader = loadShader(
                GLES30.GL_FRAGMENT_SHADER,
                Shape.fragmentShaderCode);

        programId = GLES30.glCreateProgram();
        GLES30.glAttachShader(programId, vertexShader);
        GLES30.glAttachShader(programId, fragmentShader);
        GLES30.glLinkProgram(programId);
        GLES30.glGetProgramiv(programId, GLES30.GL_LINK_STATUS, linkStatus, 0);

        return programId;
    }

    public float[] getPosition() {
        return position;
    }

    public void draw(float[] mvpMatrix) {

        GLES30.glUseProgram(programId);

        int mvpMatrixId = GLES30.glGetUniformLocation(programId, "uMVPMatrix");
        GLES30.glUniformMatrix4fv(mvpMatrixId, 1, false, mvpMatrix, 0);


        int positionId = GLES30.glGetAttribLocation(programId, "vPosition");
        int colorId = GLES30.glGetAttribLocation(programId, "vCouleur");

        GLES30.glEnableVertexAttribArray(positionId);
        GLES30.glEnableVertexAttribArray(colorId);

        GLES30.glVertexAttribPointer(
                positionId, COORDS_PER_VERTEX,
                GLES30.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        GLES30.glVertexAttribPointer(
                colorId, COLORS_PER_VERTEX,
                GLES30.GL_FLOAT, false,
                colorStride, colorBuffer);


        GLES30.glDrawElements(
                GLES30.GL_TRIANGLES, indices.length,
                GLES30.GL_UNSIGNED_SHORT, indiceBuffer);


        GLES30.glDisableVertexAttribArray(positionId);
        GLES30.glDisableVertexAttribArray(colorId);
    }

    public TouchResult onShapeTouch(MotionEvent event, float glX, float glY) {
        return TouchResult.PASS;
    }

    private static int loadShader(int type, String shaderCode) {
        int shader = GLES30.glCreateShader(type);

        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);

        return shader;
    }
}