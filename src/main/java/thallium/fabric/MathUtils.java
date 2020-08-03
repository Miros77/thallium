package thallium.fabric;

public class MathUtils {

    private static final float[] SINE_TABLE_FAST = new float[4096];

    public static float fastSin(float value) {
        return SINE_TABLE_FAST[(int)(value * 651.8986f) & 4095];
    }

    public static float fastCos(float value) {
        return SINE_TABLE_FAST[(int)(value * 651.8986f + 1024.0F) & 4095];
    }

    static {
        for (int j = 0; j < SINE_TABLE_FAST.length; ++j)
            SINE_TABLE_FAST[j] = (float)(Math.sin((double)j * Math.PI * 2.0D / 4096.0D));
    }

}