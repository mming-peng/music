package com.s17201120.player.view;

import com.s17201120.player.model.PlayMode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.s17201120.player.util.ImageUtils;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class FuncBottom {

	private HBox bottom;

	private Label play; // 播放，暂停
	private Label pre; // 上一首
	private Label next; // 下一首

	private Label title; // 名称展示
	private final Slider timeSlider = new Slider(); // 时间滑块
	private Label timeLabel; // 时间显示

	ChoiceBox<String> playModelChoiceBox;

	Label volumeLabel; // 音量
	final Slider volumeSlider = new Slider(); // 音量滑块

	public FuncBottom() {
		init();
	}

	public void init() {
		initPlayButton();
		initTime();
		initPlayMode();
		initVolume();

		initLayout();
	}

	/**
	 * 初始化布局
	 */
	private void initLayout() {
		// 布局
		bottom = new HBox();
		bottom.getStyleClass().add("player-bottom");

		bottom.getChildren().addAll(pre, play, next);

		HBox infoBox = new HBox();
		HBox timeBox = new HBox();
		VBox centerBox = new VBox();

		infoBox.getChildren().add(title);
		timeBox.getChildren().addAll(timeSlider, timeLabel);
		centerBox.getChildren().addAll(infoBox, timeBox);
		bottom.getChildren().add(centerBox);

		VBox playModelBox = new VBox();
		playModelBox.getStyleClass().add("player-playModel-vbox");
		playModelBox.getChildren().add(playModelChoiceBox);
		bottom.getChildren().addAll(playModelBox, volumeLabel, volumeSlider);
	}

	public HBox getNode() {
		return bottom;
	}

	private void initPlayButton() {
		// 播放 暂停
		Tooltip pauseTooltip = new Tooltip("暂停");
		Tooltip preTooltip = new Tooltip("上一首");
		Tooltip nextTooltip = new Tooltip("下一首");

		ImageView playIamgeView = ImageUtils.getImageView("pause.png", 48, 48);
		play = new Label("", playIamgeView);
		play.getStyleClass().addAll("player-play", "player-button");
		play.setTooltip(pauseTooltip);

		// 上一首
		ImageView preIamgeView = ImageUtils.getImageView("pre.png", 40, 40);
		pre = new Label("", preIamgeView);
		pre.getStyleClass().addAll("player-pre", "player-button");
		pre.setTooltip(preTooltip);

		// 下一首
		ImageView nextIamgeView = ImageUtils.getImageView("next.png", 40, 40);
		next = new Label("", nextIamgeView);
		next.getStyleClass().addAll("player-next", "player-button");
		next.setTooltip(nextTooltip);

	}

	/**
	 * 时间
	 */
	private void initTime() {
		title = new Label();
		title.getStyleClass().add("player-title-label");

		timeSlider.getStyleClass().add("player-time-slider");
		timeSlider.setMin(0.0);
		timeSlider.setMax(1.0);
		timeSlider.setValue(0.0);

		timeLabel = new Label("00:00/00:00");
		timeLabel.getStyleClass().add("player-time-label");

	}

	/**
	 * 播放模式
	 */
	private void initPlayMode() {
		ObservableList<String> list = FXCollections.observableArrayList(PlayMode.CYCLING.getName(),
				PlayMode.RANDOM.getName(), PlayMode.ORDER.getName(), PlayMode.ONE_CYCLING.getName());
		playModelChoiceBox = new ChoiceBox<String>();
		playModelChoiceBox.setItems(list);
		// 默认单曲循环
		playModelChoiceBox.setValue(PlayMode.ONE_CYCLING.getName());
		playModelChoiceBox.getStyleClass().add("player-playModel-choiceBox");
	}

	/**
	 * 声音
	 */
	private void initVolume() {
		volumeLabel = new Label("音量");
		volumeLabel.getStyleClass().add("player-volume-label");

		volumeSlider.setMin(0.0);
		volumeSlider.setMax(1.0);
		volumeSlider.setValue(1.0);
		volumeSlider.getStyleClass().add("player-volume-slider");

	}

	public Label getPlay() {
		return play;
	}

	public Label getPre() {
		return pre;
	}

	public Label getNext() {
		return next;
	}

	public Label getName() {
		return title;
	}

	public Slider getTimeSlider() {
		return timeSlider;
	}

	public Label getTimeLabel() {
		return timeLabel;
	}

	public Label getVolumeLabel() {
		return volumeLabel;
	}

	public Slider getVolumeSlider() {
		return volumeSlider;
	}

	public ChoiceBox<String> getPlayModelChoiceBox() {
		return playModelChoiceBox;
	}

}
