import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Launch {
	public static void main(String[] args) {
		try {
			AppGameContainer agc = new AppGameContainer(new FugitiveGame("Stellar Fugitive"));
			agc.setDisplayMode(640, 480, false);
			agc.start();
		} catch (SlickException ex) {
			ex.printStackTrace();
		}
	}
}