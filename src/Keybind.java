public class Keybind {
    protected boolean pressed;
    protected boolean isDown;
    protected boolean isDownPrev;

    public Keybind(){
        Main.onUpdate.subscribe(this::update);
        Main.onPreUpdate.subscribe(this::preUpdate);
    }

    public boolean isDown() {
        return isDown;
    }

    public boolean isPressed() {
        return isDown && !isDownPrev;
    }

    public boolean isReleased() {
        return !isDown && isDownPrev;
    }

    private void update(){
        isDownPrev = isDown;
    }
    private void preUpdate(){isDown = pressed;}
}
