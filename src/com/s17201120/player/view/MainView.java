package com.s17201120.player.view;

import com.s17201120.player.control.PlayerManager;
import com.s17201120.player.model.Player;
import com.s17201120.player.util.ImageUtils;
import com.s17201120.player.util.PlayerUtils;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.effect.Light.Point;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class MainView {
	private static final double HEIGHT = 800;
	private static final double WIGHT = 1050;
	private static Point point = new Point();
	PlayerManager Player;
	Stage stage;
	BorderPane root;
	private ParallelTransition transition = null;
	Pane bottom;
	TableView<Player> musicList;
	Pane info;
	Pane topTitle;

	public MainView(Stage stage) {
		this.stage = stage;
	}

	public void init() {
		root = new BorderPane();
		Scene scene = new Scene(root, WIGHT, HEIGHT);
		scene.getStylesheets().add("resources/player.css");
		// 初始化
		Player = new PlayerManager(stage);
		bottom = Player.getFuncBottom().getNode();
		musicList = Player.getMusicList().getNode();
		info = Player.getLyricView().getNode();
		topTitle = Player.getTopTitle().getNode();

		initLoyout();
		initEvent();

		stage.setFullScreen(false);
		stage.setResizable(false);
		stage.setTitle("Player");
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		// stage.show();

	}

	private void initLoyout() {
		final HBox hbox = new HBox();
		hbox.setSpacing(5);

		hbox.getChildren().addAll(musicList, info);
		root.setTop(topTitle);
		root.setCenter(hbox);
		root.setBottom(bottom);

		// 加入背景
		BackgroundImage bgImage = new BackgroundImage(ImageUtils.getImage("bg.jpg"), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background bg = new Background(bgImage);
		root.setBackground(bg);

	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		// 底部功能区展示
		FadeTransition bottomShow = fade(bottom, 1000, 1);
		FadeTransition bottomHide = fade(bottom, 1000, 0);

		// 播放列表展示
		FadeTransition musicListShow = fade(musicList, 1000, 0.8);
		FadeTransition musicListHide = fade(musicList, 1000, 0);

		// 拖拽实现
		root.setOnMousePressed(event -> {
			point.setX(stage.getX() - event.getScreenX());
			point.setY(stage.getY() - event.getScreenY());
		});

		root.setOnMouseReleased(event -> {
			if (stage.getY() < 0) {
				stage.setY(0);
			}
		});

		root.setOnMouseDragged(event -> {
			if (stage.isFullScreen()) {
				return;
			}

			double x = (event.getScreenX() + point.getX());
			double y = (event.getScreenY() + point.getY());

			Platform.runLater(() -> {
				stage.setX(x);
				stage.setY(y);
			});

		});

		// 双击全屏
		root.setOnMouseClicked(event -> {
			if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
				stage.setFullScreen(!stage.isFullScreen());

				if (transition != null)
					transition.stop();
				if (stage.isFullScreen()) {
					transition = new ParallelTransition(bottomHide, musicListHide);
				} else {
					transition = new ParallelTransition(bottomShow, musicListShow);
				}
				transition.play();
			}
		});

		// 去除全屏提示
		stage.setFullScreenExitHint("");

		// 最小化
		Player.getTopTitle().getMinimize().setOnMouseClicked(event -> {
			stage.setIconified(true);
		});

		// 关闭按钮
		Player.getTopTitle().getClose().setOnMouseClicked(event -> {
			Event.fireEvent(stage, new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
		});

		// 关闭
		stage.setOnCloseRequest(event -> {
			exit();
		});

		// 划入显示菜单
		root.setOnMouseEntered(event -> {
			if (transition != null)
				transition.stop();

			transition = new ParallelTransition(bottomShow, musicListShow);
			transition.play();
		});

		// 定时任务
		int duration = 5;
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		KeyFrame keyFrame = new KeyFrame(Duration.millis(duration), event -> {
			if (stage.isFullScreen()) {
				transition = new ParallelTransition(bottomHide, musicListHide);
				transition.play();
			}
			bottom.setDisable(stage.isFullScreen());
			musicList.setDisable(stage.isFullScreen());
		});
		timeline.getKeyFrames().add(keyFrame);
		timeline.play();
	}

	public static void exit() {
		while (!PlayerUtils.CLOSE.get()) {
			try {
				Thread.sleep(100L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Platform.exit();
		System.exit(0);
	}

	private FadeTransition fade(Node node, double time, double value) {
		FadeTransition musicListShow = new FadeTransition();
		musicListShow.setNode(node);
		musicListShow.setDuration(Duration.millis(500));
		musicListShow.setToValue(value);
		musicListShow.setInterpolator(Interpolator.EASE_OUT);
		return musicListShow;
	}
}
