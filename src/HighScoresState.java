import java.io.*;
import java.util.*;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.state.*;

public class HighScoresState extends BasicGameState {
	private List<Long> highScores;
	private boolean gotFile;
	private File scoreFile;
	private long newScore;
	
	public HighScoresState() {
		Scanner sc = null;
		try {
			scoreFile = new File("SFhighscores.txt");
			if (!scoreFile.exists()) {
				PrintWriter pw = new PrintWriter("SFhighscores.txt");
				pw.print("0");
				for (int i = 0; i < 9; i++)
					pw.print(" 0");
			}
			sc = new Scanner(scoreFile).useDelimiter("\\s");
			gotFile = true;
		} catch (IOException ioex) {
			ioex.printStackTrace();
			gotFile = false;
		}
		highScores = new ArrayList<Long>(10);
		if (gotFile) {
			highScores.add(sc.nextLong());
			for (int i = 0; i < 9; i++) {
				long l = sc.nextLong();
				highScores.add(l);
			}
			sc.close();
		} else {
			for (int i = 0; i < 10; i++) {
				highScores.add(0L);
			}
		}
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
	}
	
	public void enter(GameContainer gc, StateBasedGame sbg, long score) {
		this.newScore = score;
		super.enter(gc, sbg);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
		
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
	}
	
	@Override
	public int getID() {
		return 2;
	}
}