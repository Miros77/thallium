package thallium.fabric.math;

public enum FastMathType {

    VANILLA, DEVMASTER, RIVEN, ICECORE;

    public FastMathType getNext() {
        switch (this) {
            case DEVMASTER:
                return RIVEN;
            case ICECORE:
                return DEVMASTER;
            case RIVEN:
                return ICECORE;
            case VANILLA:
                return RIVEN;
            default:
                break;
            
        }
        return null;
    }

}