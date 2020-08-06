package thallium.fabric.math;

import thallium.fabric.gui.ThalliumOptions;

public class MathUtils {

    public static float fastSin(float value) {
        switch (ThalliumOptions.fastMathType) {
            case DEVMASTER:
                return SIN.Devmaster.sin(value);
            case ICECORE:
                return SIN.Icecore.sin(value);
            case RIVEN:
                return SIN.Riven.sin(value);
            default:
                break;
        }
        return -1;
    }

    public static float fastCos(float value) {
        switch (ThalliumOptions.fastMathType) {
            case DEVMASTER:
                return SIN.Devmaster.cos(value);
            case ICECORE:
                return SIN.Icecore.cos(value);
            case RIVEN:
                return SIN.Riven.cos(value);
            default:
                break;
        }
        return -1;
    }

}