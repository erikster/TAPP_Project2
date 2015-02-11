import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.state.*;

public class ScoreManager {
	public static final int TIME_SCORE_INT_MS = 100;  // 0.1 seconds
	public static final int TIME_SCORE_INC    = 1;    // 1 pt earned per time unit
	
	private long currScore;
	private long counter;
	
	public ScoreManager() {
		this.currScore = 0;
		this.counter   = 0;
	}
	
	public void update(GameContainer gc, int delta_ms) {
		counter += delta_ms;
		if (counter > TIME_SCORE_INT_MS) {
			currScore += TIME_SCORE_INC;
			counter = 0;
		}
	}
	
	public long getScore() { return currScore; }
}