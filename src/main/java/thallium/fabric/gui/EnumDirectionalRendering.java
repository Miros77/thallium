package thallium.fabric.gui;

public enum EnumDirectionalRendering {

    OFF(-1),
    NORMAL(4),
    FAST(2);

    public int level;
    private EnumDirectionalRendering(int level) {
        this.level = level;
    }

    public EnumDirectionalRendering getNext() {
        switch (this) {
            case FAST:
                return OFF;
            case NORMAL:
                return FAST;
            case OFF:
                return NORMAL;
            default:
                return NORMAL;
        }
    }

}