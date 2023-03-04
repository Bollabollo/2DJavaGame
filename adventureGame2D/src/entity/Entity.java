package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import adventureGame2D.GamePanel;
import adventureGame2D.UtilityTool;

public class Entity {
	
	GamePanel gp;
	public BufferedImage image, image2, image3;
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public String name, direction = "down";

	
	
	
	
	//Entity position
	public int WorldX, WorldY, speed;
	protected int spriteCounter = 0, actionLock = 0, invincibilityCounter = 0;
	protected boolean spriteNum = true; 
	

	
	
		
	//specifies the solid area of the character entity for collision
	//Store data about this rectangle as x, y, width and height
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collisionOn = false, invincibility = false;
	//0 - player, 1 - npc, 2 - monster
	protected int entityType;
	
	//Monster HP Bar
	private boolean hpBarEnabled = false;
	private int hpBarCounter = 0;

	
	//attack area
	protected Rectangle attackArea = new Rectangle (0, 0, 0, 0);
	
	
	//ENTITY STATUS
	//Character HP - both players and monster
	protected int maxLife, life;
	//maxLife and Life get and set
	public int getMaxLife() {return maxLife;}
	public int getLife() {return life;}
	public void setMaxLife(int life) {maxLife = life;}
	public void setLife (int life) {this.life = life;}
	protected int deathCount = 0;
	
	
	//alive and in dying animation or not
	protected boolean alive = true, dying = false;
	public boolean getAlive() {return alive;}
	public boolean getDying() { return dying;}
//	Constructor
	public Entity (GamePanel gp) {
		this.gp = gp;
	}
	
	
// 	Class methods
	protected void setAction() {}
	
	protected void speak() {}
	
	public void update() {
		setAction();
		collisionOn = false;
		gp.cChecker.CheckTile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkEntity(this, gp.NPCs);
		gp.cChecker.checkEntity(this, gp.monsters);
		
		
		if (gp.cChecker.checkPlayer(this) && this.entityType == 2) {
			if (!gp.player.invincibility) {
				gp.player.setLife(gp.player.getLife() - 1);
				gp.player.invincibility = true;
			}
		}
		
		if (!collisionOn) {
			switch (direction) {
			case "up": WorldY -= speed; break;
			case "down": WorldY += speed; break;
			case "left": WorldX -= speed; break;
			case "right": WorldX += speed; break;
			
			}
		}

		spriteCounter++;
		//Player image changes every 12 frames
		if (!collisionOn) {
		if (spriteCounter > 12) {
			if (spriteNum) {
				spriteNum = false;
			}
			
			else {
				spriteNum = true;
			}
			spriteCounter = 0;
		}
		}
		this.checkInvincibilityTime();
	}
	
	
	
	public void draw (Graphics2D g2, GamePanel gp) {
		this.gp = gp;
		BufferedImage image = null;
		int entityScreenX = this.WorldX - gp.player.WorldX + gp.player.screenX;
		int entityScreenY = this.WorldY - gp.player.WorldY + gp.player.screenY;
		
		//Render the monsters on screen - pretty fuzzy about this since just copy
		if (this.WorldX + gp.tileSize > gp.player.WorldX - gp.player.screenX && 
			this.WorldX - gp.tileSize < gp.player.WorldX + gp.player.screenX &&
			this.WorldY + gp.tileSize > gp.player.WorldY - gp.player.screenY &&
			this.WorldY - gp.tileSize < gp.player.WorldY + gp.player.screenY) {
			
			switch (direction) {
			case "up": if (spriteNum) {image = up1;} else {image = up2;} break;
			case "down": if (spriteNum) {image = down1;} else {image = down2;} break;
			case "left": if (spriteNum) {image = left1;} else {image = left2;} break;
			case "right": if (spriteNum) {image = right1;} else {image = right2;} break;
			}
			
			//Monster HP bar
			if (entityType == 2 && hpBarEnabled) {
				double oneBarValue = (double) gp.tileSize/this.maxLife,
						hpBarValue = oneBarValue * this.life;
				
				
				//Gray outline
				//x, y, width, height
				g2.setColor(new Color(35, 35, 35));
				g2.fillRect(entityScreenX - 1, entityScreenY - 1, gp.tileSize + 2, 12);
				
			
				
				//Red hp bar
				g2.setColor(new Color(255,0,30));
				g2.fillRect(entityScreenX, entityScreenY, (int) hpBarValue, 10);
				
				
				++hpBarCounter;
				
				if (hpBarCounter > 360) {
					hpBarCounter = 0;
					hpBarEnabled = false;
				}
			}
			
			if (invincibility) {
				hpBarEnabled = true;
				hpBarCounter = 0;
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));	
			}
			
			if (dying && !alive) {
				this.deathAnimation(g2);
			}
			
			
			//16 pixels
			g2.drawImage(image, entityScreenX, entityScreenY, gp.tileSize, gp.tileSize, null);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}
	}
	
	//Render and scale the entity
	protected BufferedImage setupCharacter(String imageName, String pathName, int width, int height) {
		
		UtilityTool uTool = new UtilityTool();
		BufferedImage scaledImage = null;
		
		try {
			scaledImage = ImageIO.read(getClass().getResourceAsStream(pathName + imageName+".png"));
			scaledImage = uTool.scaleImage(scaledImage, width, height);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return scaledImage;
	}
	
	//Set direction when talking to player
	protected void talkingDirection (Entity player, Entity NPC) {
		String direction = "";
		switch(player.direction) {
		case "up": direction ="down"; break;
		case "down": direction = "up"; break;
		case "right": direction = "left"; break;
		case "left": direction = "right"; break; 
		}
		NPC.direction = direction;
		
	}
	//******************************************************************************************************************		
	//------------------------------MONSTER OVERRIDE METHODS----------------------------------------------------------------------//
	//*****************************************************************************************************************
	
	protected void checkInvincibilityTime() {
		if (invincibility) {
			++invincibilityCounter;
			if (invincibilityCounter > 120) {
				invincibility = false;
				invincibilityCounter = 0;
				
				}
			}
	}
	
	protected void damageContact(Entity entity) {};
	
	public void monsterDamageReaction(Player player) {}
	
	//Draws dying animation for monsters
	protected void deathAnimation (Graphics2D g2) {
		++deathCount;
		
		if (deathCount < 41) {
			
			if (deathCount%5 == 0 && deathCount%10 != 0) {
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));	
				
			} else if (deathCount % 10 == 0){
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));	
			}
		} else {
			dying = false;
			
		}
		
	}
	
	
	public int returnDeathSound() {return 9999;};
	
	
	
}
