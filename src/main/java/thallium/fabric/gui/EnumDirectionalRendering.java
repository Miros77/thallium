package thallium.fabric.gui;

public enum EnumDirectionalRendering {

    OFF(-1),
    NORMAL(4),
    FAST(2);

    public int level;
    private EnumDirectionalRendering(int level) {
        this.level = level;
    }

}