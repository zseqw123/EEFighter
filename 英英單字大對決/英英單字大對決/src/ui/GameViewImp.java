package ui;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JPanel;

import controller.EEFighter;
import model.Question;
import model.sprite.GameMap;
import model.sprite.IGameStartView;
import model.sprite.PlayerSprite;
import model.sprite.Sprite;
import model.sprite.Sprite.Direction;
import model.sprite.Sprite.Status;

/**
 * @author Lin The game view where showing the playing game.
 */
public class GameViewImp extends JPanel implements GameView, KeyListener {
	private GameMap gameMap;
	private EEFighter eeFighter;
	private PlayerSprite spriteP1;
	private PlayerSprite spriteP2;
	private List<Sprite> letters;
	private IGameStartView gameStartView;

	public GameViewImp(EEFighter eeFighter, IGameStartView gameStartView) {
		this.eeFighter = eeFighter;
		this.gameStartView = gameStartView;
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
		eeFighter.nextQuestion();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawGameMap(g);
		drawLetters(g);
		drawBothPlayers(g);
	}

	private void drawGameMap(Graphics g) {
		if (gameMap != null)
			for (Sprite sprite : gameMap)
				drawSprite(g, sprite);
	}

	private void drawLetters(Graphics g) {
		if (letters != null)
			for (int i = 0; i < letters.size(); i++)
				drawSprite(g, letters.get(i));
	}

	private void drawBothPlayers(Graphics g) {
		if (spriteP1 != null)
			drawSprite(g, spriteP1);
		if (spriteP2 != null)
			drawSprite(g, spriteP2);
	}

	private void drawSprite(Graphics g, Sprite sprite) {
		g.drawImage(sprite.getImage(sprite.getDirection()), sprite.getX(), sprite.getY(), null);
	}

	private void setupLayout() {
		setLayout(new FlowLayout());
	}

	private void setupViews() {

	}

	@Override
	public void onGameClose() {

	}

	public void nextQuestion() {
		try {
			Thread.sleep(1000);
			eeFighter.nextQuestion();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDraw(GameMap gameMap, List<Sprite> letters, PlayerSprite player1, PlayerSprite player2) {
		this.gameMap = gameMap;
		this.letters = letters;
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

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			eeFighter.move(spriteP1, Direction.NORTH, Status.MOVE);
			break;
		case KeyEvent.VK_DOWN:
			eeFighter.move(spriteP1, Direction.SOUTH, Status.MOVE);
			break;
		case KeyEvent.VK_LEFT:
			eeFighter.move(spriteP1, Direction.WEST, Status.MOVE);
			break;
		case KeyEvent.VK_RIGHT:
			eeFighter.move(spriteP1, Direction.EAST, Status.MOVE);
			break;
		case KeyEvent.VK_K:
			eeFighter.popLetter(spriteP1);
			break;
		case KeyEvent.VK_L:
			eeFighter.checkAnswer(spriteP1);
			break;
		case KeyEvent.VK_T:
			eeFighter.move(spriteP2, Direction.NORTH, Status.MOVE);
			break;
		case KeyEvent.VK_G:
			eeFighter.move(spriteP2, Direction.SOUTH, Status.MOVE);
			break;
		case KeyEvent.VK_F:
			eeFighter.move(spriteP2, Direction.WEST, Status.MOVE);
			break;
		case KeyEvent.VK_H:
			eeFighter.move(spriteP2, Direction.EAST, Status.MOVE);
			break;
		case KeyEvent.VK_Z:
			eeFighter.popLetter(spriteP2);
			break;
		case KeyEvent.VK_X:
			eeFighter.checkAnswer(spriteP2);
			break;
		default:
			break;
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// set the corresponding action bit as 0.
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_RIGHT:
			eeFighter.move(spriteP1, spriteP1.getDirection(), Status.STOP);
			break;
		case KeyEvent.VK_T:
		case KeyEvent.VK_G:
		case KeyEvent.VK_F:
		case KeyEvent.VK_H:
			eeFighter.move(spriteP2, spriteP2.getDirection(), Status.STOP);
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

	}

	@Override
	public void onHitWall(Sprite sprite) {

	}

	@Override
	public void onNextQuestion(Question question) {
		gameStartView.onNextQuestion(question);
	}

	@Override
	public void onLetterPoppedSuccessfuly(PlayerSprite player, List<Sprite> letter) {
		if (spriteP1 == player)
			gameStartView.onPlayerPopedLetter("player1", letter);
		else if (spriteP2 == player)
			gameStartView.onPlayerPopedLetter("player2", letter);
	}

	@Override
	public void onLetterPoppedFailed(PlayerSprite player) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLetterGotten(PlayerSprite player, List<Sprite> letter) {
		if (spriteP1 == player)
			gameStartView.onPlayerEatLetter("player1", letter);
		else 
			gameStartView.onPlayerEatLetter("player2", letter);
	}

	@Override
	public void onAnswerCorrect(PlayerSprite player) {
		System.out.println("correct");
	}

	@Override
	public void onAnswerWrong(PlayerSprite player) {
		
	}

	@Override
	public void onNoMoreQuestion() {
		// TODO Auto-generated method stub
		
	}

}
