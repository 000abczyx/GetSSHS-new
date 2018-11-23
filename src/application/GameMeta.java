package application;

public class GameMeta {
	int score;
	int bestScore;
	boolean over;
	boolean won;
	boolean terminated;

	GameMeta(int score, int bestScore, boolean over, boolean won, boolean terminated) {
		this.score = score;
		this.bestScore = bestScore;
		this.over = over;
		this.won = won;
		this.terminated = terminated;
	}
}
