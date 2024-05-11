public class Player extends GameObject{
    public static Player instance;
    private static final double ZOOM_SPEED = 0.03;

    public double zoom = 0.03;
    public double speed = 15;
    public double terminalVelocity = 30;
    public double jumpPower = 10;
    public Vector2 gravity = new Vector2(0, 16);
    public Vector2 size = new Vector2(0.8, 1.8);
    public Vector2 pos = new Vector2(0,-50);
    public Vector2 velocity = new Vector2(0, 0);

    private boolean isGrounded = false;

    private final Collider collider = new Collider() {
        @Override
        public Vector2 getCenter() {
            return pos;
        }

        @Override
        public void setCenter(Vector2 value) {
            pos = value;
        }

        @Override
        public Vector2 getSize() {
            return size;
        }

        @Override
        public void onBottomCollide() {
            isGrounded = true;
            if(velocity.y > 0) velocity = velocity.withY(0);
        }

        @Override
        public void onTopCollide() {
            if(velocity.y < 0) velocity = velocity.withY(0);
        }
    };

    public Player(){
        instance = this;
    }

    @Override
    public void render(Renderer r) {
        //r.setColor(new Color(((float) Math.sin(Main.getTime()) + 1f)/2f,0f,0f));
        r.drawRectWorldSpace(pos.sub(size.scale(0.5)), size);

        r.graphics().drawString("FPS: " + (1/Main.getUncappedDeltaTime()), 10, 10);
    }

    @Override
    public void tick() {
        if(Input.moveLeft.isDown()) {
            pos = pos.add(new Vector2(-speed, 0.0).scale(Main.getDeltaTime()));
        }

        if(Input.moveRight.isDown()) {
            pos = pos.add(new Vector2(speed, 0.0).scale(Main.getDeltaTime()));
        }

        velocity = velocity.add(gravity.scale(Main.getDeltaTime()));
        velocity = velocity.withY(Math.min(velocity.y, terminalVelocity));
        pos = pos.add(velocity.scale(Main.getDeltaTime()));

        isGrounded = false;
        collider.processCollisionWithTerrain();

        if(Input.jump.isPressed() && isGrounded){
            velocity = velocity.withY(-jumpPower);
        }

        Vector2Int mouseTile = Renderer.screenToWorldPos(Input.getMousePosition().toVector()).floorToInt();

        if(Input.use.isDown){
            World.setMainTile(mouseTile, null);
        }

        if(Input.zoomIn.isDown){
            zoom += ZOOM_SPEED * Main.getDeltaTime();
        }

        if(Input.zoomOut.isDown){
            zoom -= ZOOM_SPEED * Main.getDeltaTime();
        }

        zoom = Math.max(zoom, 0.001);

        if(Input.debugReset.isPressed()){
            pos = new Vector2(0, 0);
            velocity = new Vector2(0, 0);
            zoom = 0.03;
        }

        if(Input.debugTeleport.isPressed()){
            pos = Renderer.screenToWorldPos(Input.getMousePosition().toVector());
        }
    }
}
