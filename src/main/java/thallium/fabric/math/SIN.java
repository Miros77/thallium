//
//
// Free Code provided by mooman219 on Java-Gaming.org
//
//

package thallium.fabric.math;

/**
 * Free class provided by mooman219 on JavaGaming
 * 
 * @author mooman219
 * @see https://jvm-gaming.org/t/extremely-fast-sine-cosine/55153/6
 */
public class SIN {

    ///////////////////////////////////////
    // Devmaster's sine/cosine ( http://forum.devmaster.net/t/fast-and-accurate-sine-cosine/9648 )
    ///////////////////////////////////////
    public static final class Devmaster {

        public static final float PI = 3.1415927f;
        public static final float PI_2 = PI / 2f;
        public static final float DOUBLE_PI = PI * 2f;
        public static final float B = 4 / PI;
        public static final float C = -4 / (PI * PI);
        public static final float P = 0.225f;

        public static final float sin(float x) {
            float x1 = x % PI;
            float x2 = x % DOUBLE_PI;

            if (x > 0) {
                float y = x1 * (B + C * x1);
                y = (y > 0) ? (y = P * (y * y - y) + y)
                        : (y = P * (-y * y - y) + y);
                float xp = x2 - DOUBLE_PI;
                if (!(xp < 0 && xp < -PI)) {
                    y = -y;
                }
                return y;
            } else {
                float y = x1 * (B - C * x1);
                y = (y > 0) ? (y = P * (y * y - y) + y)
                        : (y = P * (-y * y - y) + y);
                float xp = x2 + DOUBLE_PI;
                if (xp > 0 && xp < PI) {
                    y = -y;
                }
                return y;
            }
        }

        public static final float cos(float x) {
            float x0 = x + PI_2;
            float x1 = x0 % PI;
            float x2 = x0 % DOUBLE_PI;

            if (x0 > 0) {
                float y = x1 * (B + C * x1);
                y = (y > 0) ? (y = P * (y * y - y) + y)
                        : (y = P * (-y * y - y) + y);
                float xp = x2 - DOUBLE_PI;
                if (!(xp < 0 && xp < -PI)) {
                    y = -y;
                }
                return y;
            } else {
                float y = x1 * (B - C * x1);
                y = (y > 0) ? (y = P * (y * y - y) + y) : (y = P * (-y * y - y) + y);
                float xp = x2 + DOUBLE_PI;
                if (xp > 0 && xp < PI)
                    y = -y;
                return y;
            }
        }
    }

    ///////////////////////////////////////
    // Riven's sine/cosine ( http://www.java-gaming.org/topics/fast-math-sin-cos-lookup-tables/24191/view.html )
    ///////////////////////////////////////
    public static final class Riven {

        private static final int SIN_BITS, SIN_MASK, SIN_COUNT;
        private static final float radFull, radToIndex;
        private static final float degFull, degToIndex;
        private static final float[] sin, cos;

        static {
            SIN_BITS = 12;
            SIN_MASK = ~(-1 << SIN_BITS);
            SIN_COUNT = SIN_MASK + 1;

            radFull = (float) (Math.PI * 2.0);
            degFull = (float) (360.0);
            radToIndex = SIN_COUNT / radFull;
            degToIndex = SIN_COUNT / degFull;

            sin = new float[SIN_COUNT];
            cos = new float[SIN_COUNT];

            for (int i = 0; i < SIN_COUNT; i++) {
                sin[i] = (float) Math.sin((i + 0.5f) / SIN_COUNT * radFull);
                cos[i] = (float) Math.cos((i + 0.5f) / SIN_COUNT * radFull);
            }

            // Four cardinal directions (credits: Nate)
            for (int i = 0; i < 360; i += 90) {
                sin[(int) (i * degToIndex) & SIN_MASK] = (float) Math.sin(i * Math.PI / 180.0);
                cos[(int) (i * degToIndex) & SIN_MASK] = (float) Math.cos(i * Math.PI / 180.0);
            }
        }

        public static final float sin(float rad) {
            return sin[(int) (rad * radToIndex) & SIN_MASK];
        }

        public static final float cos(float rad) {
            return cos[(int) (rad * radToIndex) & SIN_MASK];
        }
    }


    ///////////////////////////////////////
    // Icecore's sine/cosine ( http://www.java-gaming.org/topics/extremely-fast-sine-cosine/36469/msg/346190/view.html#msg346190 )
    ///////////////////////////////////////
    public static final class Icecore {

        private static final int Size_SC_Ac = 5000;
        private static final int Size_SC_Ar = Size_SC_Ac + 1;
        private static final float Sin[] = new float[Size_SC_Ar];
        private static final float Cos[] = new float[Size_SC_Ar];
        private static final float Pi = (float) Math.PI;
        private static final float Pi_D = Pi * 2;
        private static final float Pi_SC_D = Pi_D / Size_SC_Ac;

        static {
            for (int i = 0; i < Size_SC_Ar; i++) {
                double d = i * Pi_SC_D;
                Sin[i] = (float) Math.sin(d);
                Cos[i] = (float) Math.cos(d);
            }
        }

        public static final float sin(float r) {
            float rp = r % Pi_D;
            if (rp < 0) {
                rp += Pi_D;
            }
            return Sin[(int) (rp / Pi_SC_D)];
        }

        public static final float cos(float r) {
            float rp = r % Pi_D;
            if (rp < 0) {
                rp += Pi_D;
            }
            return Cos[(int) (rp / Pi_SC_D)];
        }
    }

}