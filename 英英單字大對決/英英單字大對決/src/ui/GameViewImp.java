package ui;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JPanel;

import controller.EEFighter;
import model.sprite.BasicMapDirector;
import model.sprite.GameMap;
import model.sprite.Sprite;
import model.sprite.Sprite.Direction;
import model.sprite.Sprite.Status;
import model.sprite.XY;

/*
 * The game view where showing the playing game.
 */
public class GameViewImp extends JPanel implements GameView, KeyListener {

	private GameMap gameMap;
	private EEFighter eeFighter;
	private Sprite spriteP1;
	private Sprite spriteP2;

	public GameViewImp(EEFighter eeFighter) {
		this.eeFighter = eeFighter;
	}
	
	@Override
	public void start() {
		eeFighter.setGameView(this);

		setBounds(0, 0, 1110, 700);
		setupViews();
		setFocusable(true);
		requestFocusInWindow();
		addKeyListener(this);
		setupLayout();

		eeFighter.startGame();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (gameMap != null)
			for (Sprite sprite : gameMap)
				g.drawImage(sprite.getImage(), sprite.getX(), sprite.getY(), null);
	}

	private void setupLayout() {
		setLayout(new FlowLayout());
	}

	private void setupViews() {
		
	}

	@Override
	public void onGameClose() {

	}

	@Override
	public void onDraw(GameMap gameMap, List<Sprite> letters, Sprite player1, Sprite player2) {
		this.gameMap = gameMap;
		this.spriteP1 = player1;
		this.spriteP2 = player2;
		repaint();
	}

	/**
	 * Use 8-bit to store the key pressing status. Each bit corresponding action key
	 * from left to right is: None, None, None, pop the eaten letter, move up, move
	 * down, move left, move right. If the bit showing 1 means the corresponding
	 * action key should be pressing, and the bit should be set as 0 once the key is
	 * released. Due to this strategy, we can perform composite moving keys such as
	 * 'move top-left' by simply storing 0b00001010.
	 */
	private int keyInputP1 = 0b00000000;
	private int keyInputP2 = 0b00000000;

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			keyInputP1 |= 0b00001000;
			break;
		case KeyEvent.VK_DOWN:
			keyInputP1 |= 0b00000100;
			break;
		case KeyEvent.VK_LEFT:
			keyInputP1 |= 0b00000010;
			break;
		case KeyEvent.VK_RIGHT:
			keyInputP1 |= 0b00000001;
			break;
		case KeyEvent.VK_T:
			keyInputP2 |= 0b00001000;
			break;
		case KeyEvent.VK_G:
			keyInputP2 |= 0b00000100;
			break;
		case KeyEvent.VK_F:
			keyInputP2 |= 0b00000010;
			break;
		case KeyEvent.VK_H:
			keyInputP2 |= 0b00000001;
			break;
		default:
			break;
		}

		if ((keyInputP1 & 0b001000) != 0)
			moveRoleSprite(spriteP1, new XY(0, -4));
		if ((keyInputP1 & 0b000100) != 0)
			moveRoleSprite(spriteP1, new XY(0, 4));
		if ((keyInputP1 & 0b000010) != 0)
			moveRoleSprite(spriteP1, new XY(-4, 0));
		if ((keyInputP1 & 0b000001) != 0)
			moveRoleSprite(spriteP1, new XY(4, 0));
		if ((keyInputP2 & 0b001000) != 0)
			moveRoleSprite(spriteP2, new XY(0, -4));
		if ((keyInputP2 & 0b000100) != 0)
			moveRoleSprite(spriteP2, new XY(0, 4));
		if ((keyInputP2 & 0b000010) != 0)
			moveRoleSprite(spriteP2, new XY(-4, 0));
		if ((keyInputP2 & 0b000001) != 0)
			moveRoleSprite(spriteP2, new XY(4, 0));
		repaint();
	}

	private void moveRoleSprite(Sprite sprite, XY xy) {
		// TODO send a moving request to the controller
		sprite.move(xy); // this is testing, please remove.
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// set the corresponding action bit as 0.
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			keyInputP1 &= 0b11110111;
			break;
		case KeyEvent.VK_DOWN:
			keyInputP1 &= 0b11111011;
			break;
		case KeyEvent.VK_LEFT:
			keyInputP1 &= 0b11111101;
			break;
		case KeyEvent.VK_RIGHT:
			keyInputP1 &= 0b11111110;
			break;
		case KeyEvent.VK_T:
			keyInputP2 &= 0b11110111;
			break;
		case KeyEvent.VK_G:
			keyInputP2 &= 0b11111011;
			break;
		case KeyEvent.VK_F:
			keyInputP2 &= 0b11111101;
			break;
		case KeyEvent.VK_H:
			keyInputP2 &= 0b11111110;
			break;
		}
	}

	@Override
	public void onGameStarted() {
		
	}

	@Override
	public void onGameOver() {
		
	}

	@Override
	public void onMovedSuccessfuly(Sprite sprite, Direction direction, Status status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHitWall(Sprite sprite) {
		// TODO Auto-generated method stub
		
	}

}
