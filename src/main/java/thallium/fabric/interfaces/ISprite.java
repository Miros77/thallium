package thallium.fabric.interfaces;

public interface ISprite {

    public void markNeedsAnimationUpdate();

    public void unmarkNeedsAnimationUpdate();

    public boolean needsAnimationUpdate();

}