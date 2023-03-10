package npc;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import adventureGame2D.GamePanel;
import enums.Direction;
import enums.EntityType;

public class OldDude extends NPC implements FriendlyInterface{
	//Dialogues
	GamePanel gp;
	private ArrayList<String> oldDudeDialogues = new ArrayList <String>();
	private int dialogueIndex = 0;

	public OldDude (GamePanel gp, int worldX, int worldY) {
		super(gp);
		this.gp = gp;
		//x, y, width, height
		solidArea = new Rectangle(8, 8, 36, 36);
		this.WorldX = gp.getTileSize() * worldX;
		this.WorldY = gp.getTileSize() * worldY;
		
		this.setDefaultValues();
		this.getImage();
		this.setDialogue();
	}
	

	//-------------------------------NPC RENDER METHODS------------------
	public void getImage() {
		
		up1 = setupEntity("oldman_up_1", "/npc/", gp.getTileSize(), gp.getTileSize());
		up2 = setupEntity("oldman_up_2", "/npc/", gp.getTileSize(), gp.getTileSize());
		down1 = setupEntity("oldman_down_1", "/npc/", gp.getTileSize(), gp.getTileSize());
		down2 = setupEntity("oldman_down_2", "/npc/", gp.getTileSize(), gp.getTileSize());
		left1 = setupEntity("oldman_left_1", "/npc/", gp.getTileSize(), gp.getTileSize());
		left2 = setupEntity("oldman_left_2", "/npc/", gp.getTileSize(), gp.getTileSize());
		right1 = setupEntity("oldman_right_1", "/npc/", gp.getTileSize(), gp.getTileSize());
		right2 = setupEntity("oldman_right_2", "/npc/", gp.getTileSize(), gp.getTileSize());
		

		
	}
	@Override
	public void setDialogue() {
		
		//Meeting the player for the first time
		oldDudeDialogues.add(" '...' ");
		oldDudeDialogues.add(" 'OH what the-' ");
		oldDudeDialogues.add(" 'Who the heck are you, whippersnapper?\n Creeping on me like that?' ");
		
		//depends on what the player chooses
		oldDudeDialogues.add(" 'You deaf, son?' "); 
		oldDudeDialogues.add(" 'I sure as hell haven't seen you around.\n What's your name?' "); 
		oldDudeDialogues.add(" 'You what? You woke up in my HOUSE?' ");
	}
	
	@Override
	public void setBehaviour() {
		
		++actionLock;
		
		//After every a certain pseudo random amount of time
	if (actionLock == 240) {
		Random random = new Random();
		int i = random.nextInt(100) + 1;//1 to 100
		
		
		if (i < 25) {
			this.direction = Direction.UP;
		} else if (i < 50) {
			this.direction = Direction.DOWN;
		} else if (i < 75) {
			this.direction = Direction.LEFT;
		} else {
			direction = Direction.RIGHT;
		}
		
		actionLock = 0;
		}
	
	}
	
	public void speak() {
		//Opening speak
		try {
		if (oldDudeDialogues.get(dialogueIndex) != null) {
			gp.getGameUI().setCurrentDialogue(oldDudeDialogues.get(dialogueIndex));
			++dialogueIndex;
			}
		} catch (IndexOutOfBoundsException e){}
		
		talkingDirection(gp.getPlayer(), this);
		
	}

	@Override
	public final void setDefaultValues() {
		name = "Old_Dude";
		direction = Direction.DOWN;
		speed = 2;
		maxLife = 6;
		life = maxLife;
		entityType = EntityType.FRIENDLY;
		
	}


	
}
