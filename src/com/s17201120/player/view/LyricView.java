package com.s17201120.player.view;

import com.flyfox.util.NumberUtils;
import com.s17201120.player.model.LyricManager;
import com.s17201120.player.model.Lyric;
import com.s17201120.player.model.LyricItem;
import com.s17201120.player.model.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class LyricView {

	private BorderPane pane;
	private VBox lyricPane;
	private Text lyric;

	public LyricView() {
		init();
	}

	private void init() {
		pane = new BorderPane();
		pane.getStyleClass().add("player-lyric");

		lyricPane = new VBox();
		lyricPane.setAlignment(Pos.CENTER);
		pane.setCenter(lyricPane);
	}

	public void loadLyric(Player player, double time) {
		lyricPane.getChildren().clear();

		long nowTime = (long) (time * 1000);
		Lyric lyric = LyricManager.load(player);
		int index = 0;

		if (lyric == null) {
			//System.out.println("歌词加载失败：");
			return;
		}

		if (NumberUtils.parseInt(lyric.getInfo()) < 0) {
			System.err.println("歌词加载失败：" + lyric.getInfo());
			return;
		}

		List<LyricItem> list = lyric.getLyricItems();
		int size = list.size();

		// 获取当前位置
		for (int i = size - 1; i >= 0; i--) {
			LyricItem item = lyric.getLyricItems().get(i);
			long cha = nowTime - item.getTime();
			if (100 < cha) {
				index = i;
				break;
			}
		}

		// 展示歌词
		for (int i = 0; i < 13; i++) {
			// 太靠前
			if (index - 6 + i < 0) {
				continue;
			}
			// 超过最后的了
			if (index - 6 + i >= size) {
				break;
			}

			LyricItem item = list.get(index - 6 + i);

			if (i < 6) { // 前几句
				getOtherText(item, i);
			} else if (i == 6) { // 当前歌词
				getCurrentText(item);
			} else { // 后几句
				getOtherText(item, 12 - i);
			}

		}

	}

	/**
	 * 当前歌词
	 */
	private void getCurrentText(LyricItem item) {
		HBox box = new HBox();
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(8, 0, 8, 0));

		Text text = new Text(item.getContent());
		text.setFont(Font.font("Arial", 30));
		text.setFill(Color.ORANGE);
		text.setStrokeWidth(0.2);
		text.setStroke(Color.WHITE);
		box.getChildren().add(text);
		lyricPane.getChildren().add(box);
	}

	/**
	 * 其他歌词
	 */
	private void getOtherText(LyricItem item, int i) {
		HBox box = new HBox();
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(i * 0.8, 0, i * 0.8, 0));

		Text text = new Text(item.getContent());
		text.setFont(Font.font("Arial", 20 + i));
		text.setOpacity(0.25 * (i + 1));
		text.setFill(Color.WHITE);
		text.setStrokeWidth(0.2);
		text.setStroke(Color.WHITE);
		box.getChildren().add(text);
		lyricPane.getChildren().add(box);
	}

	public Pane getNode() {
		return pane;
	}
}
