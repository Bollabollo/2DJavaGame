package monster;

import java.util.Random;

import adventureGame2D.GamePanel;
import entity.Player;
import enums.Direction;
import enums.EntityType;

public class MonsterGreenSlime extends Monster{
	
	GamePanel gp;
	
	public MonsterGreenSlime (GamePanel gp, int worldX, int worldY) {
		super(gp);
		this.gp = gp;
		this.WorldX = worldX * gp.getTileSize();
		this.WorldY = worldY * gp.getTileSize();
		this.setDefaultValues();
		this.getImage(gp.getTileSize());
		
	}
	/**
	 * Override from MonsterInterface
	 */
	public final void setDefaultValues() {
		name = "Green Slime";
		attack = 2;
		defense = 0;
		speed = 1;
		maxLife = 6;
		experience = 2;
		life = maxLife;
		entityType = EntityType.HOSTILE;
		
		solidArea.x = 8;
		solidArea.y = 8;
		solidArea.width = 40;
		solidArea.height = 40;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
	}
	@Override
	/**
	 * Override from MonsterInterface
	 */
	public final void getImage(int size) {
		up1 = setupEntity("greenslime_down_1","/monster/", size, size);
		up2 = setupEntity("greenslime_down_2","/monster/", size, size);

		down1 = setupEntity("greenslime_down_1","/monster/", size, size);
		down2 = setupEntity("greenslime_down_2","/monster/", size, size);
		
		right1 = setupEntity("greenslime_down_1","/monster/", size, size);
		right2 = setupEntity("greenslime_down_2","/monster/", size, size);
		
		left1 = setupEntity("greenslime_down_1","/monster/", size, size);
		left2 = setupEntity("greenslime_down_2","/monster/", size, size);
	}
	
	@Override
	/**
	 * Override from Entity
	 */
	public final void setBehaviour() {
	++actionLock;
		
		//After every a certain pseudo random amount of time
	if (actionLock == 240) {
		Random random = new Random();
		int i = random.nextInt(100) + 1;//1 to 100
		
		if (i < 25) {
			direction = Direction.UP;
		} else if (i < 50) {
			direction = Direction.DOWN;
		} else if (i < 75) {
			direction = Direction.LEFT;
		} else {
			direction = Direction.RIGHT;
		}
		
		actionLock = 0;
		}
	}
	
	/**
	 * Override from MonsterInterface
	 */
	
	@Override
	public void monsterDamageReaction(Player player) {
		this.actionLock = 0;
		this.direction = player.getDirection();
	}
	
	/**
	 * Override from MonsterInterface
	 */
	
	@Override
	public void damagePlayer (Player player) {
		int damage = player.getLife() - this.attack + gp.getPlayer().getTotalDefense();
		player.setLife(damage);
		player.setInvincibility(true);
		gp.getGameUI().addSubtitleMsg(gp.getPlayer().getName() + " hurt by " + this.name);
	}
	
	@Override
	public final int returnDeathSound() {
		return 7;
	};
	
	
	/**
	 * Inherits the Monster class checkInvincibilityTime method
	 */
	
	
	
}
