package ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.Timer;

import controller.EnglishWarehouseController;
import model.words.PartOfSpeech;
import model.words.Word;

public class EnglishWarehouseViewImp extends JFrame implements EnglishWarehouseView, ActionListener {
	private Button addWordBtn;
	private Button removeWordBtn;
	private TextField searchAndAddWordEd;
	private DefaultListModel<Word> wordDefaultListModel;
	private JList<Word> wordList;
	private JScrollPane wordListScrollPane;
	private List<Word> words;
	private EnglishWarehouseController englishWarehouseController;

	public EnglishWarehouseViewImp(EnglishWarehouseController englishWarehouseController) {
		super("英文單字庫");
		this.englishWarehouseController = englishWarehouseController;
		englishWarehouseController.setEnglishWarehouseView(this);
		englishWarehouseController.readAllWord();
		wordListScrollPane = new JScrollPane();
		words = new ArrayList<Word>();
	}
	
	private void setupLayout() {
		getContentPane().setBackground(new Color(55, 55, 55));
		getContentPane().setLayout(new FlowLayout());
		add(searchAndAddWordEd);
		add(addWordBtn);
		add(removeWordBtn);
		add(wordListScrollPane);
	}

	private void addButtonsActionListener(ActionListener actionListener) {
		addWordBtn.addActionListener(actionListener);
		removeWordBtn.addActionListener(actionListener);
	}

	private void setupViews() {
		initializeAllComponents();
		setViewsFont(new Font("微軟正黑體", Font.BOLD, 20));
		setViewsSize(new Dimension(50, 40));
	}

	private void initializeAllComponents() {
		addWordBtn = new Button("加入");
		removeWordBtn = new Button("刪除");
		searchAndAddWordEd = new TextField();
	}

	private void setViewsFont(Font font) {
		addWordBtn.setFont(font);
		removeWordBtn.setFont(font);
		searchAndAddWordEd.setFont(font);
		wordListScrollPane.setFont(font);
	}

	private void setViewsSize(Dimension dimension) {
		addWordBtn.setPreferredSize(dimension);
		removeWordBtn.setPreferredSize(dimension);
		searchAndAddWordEd.setPreferredSize(new Dimension(230, 40));
		wordListScrollPane.setPreferredSize(new Dimension(340, 500));
	}

	public void showWordList() {
		wordDefaultListModel = new DefaultListModel<>();
		for (Word word : words) 
			wordDefaultListModel.addElement(word);
		wordList = new JList<>(wordDefaultListModel);
		wordList.setCellRenderer(new WordCellRenderer());
		wordListScrollPane.setViewportView(wordList);
	}

	public void start() {
		EventQueue.invokeLater(() -> {
			setBounds(500, 200, 400, 650);
			setupViews();
			setupLayout();
			addButtonsActionListener(this);
			englishWarehouseController.readAllWord();
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Button button = (Button) e.getSource();
		if (button == addWordBtn) {
			String text = searchAndAddWordEd.getText();
			englishWarehouseController.addWord(text);
			wordDefaultListModel.addElement(new Word(text));
		} else if (button == removeWordBtn) {
			Word word = wordList.getSelectedValue();
			int index = wordList.getSelectedIndex();
			englishWarehouseController.removeWord(word);
			wordDefaultListModel.removeElementAt(index);
		}
	}

	@Override
	public void onWordCreateSuccessfully(Word word) {
	}

	@Override
	public void onWordRemoveSuccessfully(Word word) {
	}
	
	public void onWordReadSuccessfully(Word word) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWordCreateFailed(String word, Exception exception) {
		JOptionPane.showMessageDialog(null, word + " create failed");
	}

	@Override
	public void onWordReadFailed(String word, Exception exception) {
		
	}
	
	@Override
	public void onWordReadSuccessfully(List<Word> words) {
		this.words = words;
		showWordList();
	}
	
	@Override
	public void onWordReadFailed(Exception exception) {
		
	}

	@Override
	public void onWordNotExistCreateFailed(String word) {
		// TODO Auto-generated method stub
		
	}
}

class WordCellRenderer extends JPanel implements ListCellRenderer {
	private static final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);
	private static Map<JPanel, JProgressBar> barMap = new HashMap<>();
	private JLabel label;
	public WordCellRenderer() {
		setOpaque(true);
		setLayout(new BorderLayout());
		label = new JLabel();
		add(label, BorderLayout.WEST);
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		Word word = (Word) value;
//		JProgressBar progressBar;
//		if (barMap.containsKey(this))
//			progressBar = barMap.get(this);
//		else
//		{
//			progressBar = createProgressBar();
//			add(progressBar, BorderLayout.EAST);
//			barMap.put(this, progressBar);
//		}
		label.setText(word.getWord());
		if (isSelected) {
			setBackground(HIGHLIGHT_COLOR);
			label.setForeground(Color.white);
		} else {
			setBackground(Color.white);
			label.setForeground(Color.black);
		}
		return this;
	}
	
	private JProgressBar createProgressBar() {
		JProgressBar progressBar = new JProgressBar();
		progressBar.setUI(new ProgressCircleBar());
		progressBar.setBackground(Color.WHITE);
		progressBar.setForeground(Color.ORANGE);
		progressBar.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		progressBar.setSize(50, 50);
		(new Timer(50, e -> {
		      int iv = Math.min(100, (progressBar.getValue() + 1) % 100);
		      progressBar.setValue(iv);
		    })).start();
		return progressBar;
	}
	
}
