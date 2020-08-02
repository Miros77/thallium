package thallium.fabric;

public class MathUtils {

    public static boolean useFastMath = true;

    private static final float[] SIN_TABLE_FAST = new float[4096];

    public static float fastSin(float value) {
        return SIN_TABLE_FAST[(int)(value * 651.8986f) & 4095];
    }

    public static float fastCos(float value) {
        return SIN_TABLE_FAST[(int)(value * 651.8986f + 1024.0F) & 4095];
    }

    static {
        for (int j = 0; j < SIN_TABLE_FAST.length; ++j)
            SIN_TABLE_FAST[j] = (float)(Math.sin((double)j * Math.PI * 2.0D / 4096.0D));
    }

}