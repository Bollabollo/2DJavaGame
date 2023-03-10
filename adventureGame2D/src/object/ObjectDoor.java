package object;

import adventureGame2D.GamePanel;


public class ObjectDoor extends GameObject {
	
	GamePanel gp;
	
	public ObjectDoor(GamePanel gp, int worldX, int worldY) {
		super(gp);
		this.gp = gp;
		setDefaultAttributes();
	}

	public void setDefaultAttributes() {
		name  = "Door";
		down1 = setupEntity("door", "/objects/environment/", gp.getTileSize(), gp.getTileSize());
		collisionOn = true;
		this.WorldX = WorldX * gp.getTileSize();
		this.WorldY = WorldY * gp.getTileSize();
		
		solidArea.x = 0;
		solidArea.y = 16;
		solidArea.width = 48;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
	}
	
	
} 


