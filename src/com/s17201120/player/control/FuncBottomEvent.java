package com.s17201120.player.control;

import com.s17201120.player.view.FuncBottom;
import com.s17201120.player.view.MusicList;
import com.s17201120.player.model.CacheUtils;
import com.s17201120.player.model.PlayMode;
import com.s17201120.player.util.ImageUtils;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class FuncBottomEvent {
	
	PlayerManager manager;

	public FuncBottomEvent(PlayerManager manager) {
		this.manager = manager;
	}
	
	/**
	 * 底部操作事件初始化
	 */
	public void bind() {
		FuncBottom funcBottom = manager.getFuncBottom();
		MusicList musicList = manager.getMusicList();
		
		Label play = funcBottom.getPlay();
		Tooltip playTooltip = new Tooltip("播放");
		Tooltip pauseTooltip = new Tooltip("暂停");
		ImageView playIamgeView = (ImageView) play.getGraphic();

		play.setOnMouseClicked(event -> {
			if (musicList.isEmpty()) {
				return;
			}

			// 新加入数据,获取当前音乐，绑定
			if (manager.getModel() == null) {
				manager.setModel(musicList.get(CacheUtils.getIndex()));
				manager.bindMusic();
			}

			if (manager.getStatus() == MediaPlayer.Status.PLAYING) {
				// 暫停播放
				playIamgeView.setImage(ImageUtils.getImage("play.png"));
				play.setTooltip(playTooltip);
				manager.pause();
			} else {
				// 播放音訊
				manager.play();
				play.setTooltip(pauseTooltip);
				playIamgeView.setImage(ImageUtils.getImage("pause.png"));
			}
		});

		// 上一首
		funcBottom.getPre().setOnMouseClicked(event -> {
			if (musicList.isEmpty()) {
				return;
			}

			manager.playPre();
		});

		// 下一首
		funcBottom.getNext().setOnMouseClicked(event -> {
			if (musicList.isEmpty()) {
				return;
			}

			manager.playNext();
		});

		Slider timeSlider = funcBottom.getTimeSlider();
		// 鼠标按下，不再更新
		timeSlider.setOnMousePressed(event -> {
			timeSlider.setValueChanging(true);
		});

		// 鼠标释放，修改时间后恢复
		timeSlider.setOnMouseReleased(event -> {
			Platform.runLater(new Runnable() {
				public void run() {
					manager.seek(Duration.seconds(timeSlider.getValue()));

					manager.getLyricView().loadLyric(manager.getModel(), timeSlider.getValue());

					timeSlider.setValueChanging(false);
				}

			});
		});

		// 播放模式缓存
		funcBottom.getPlayModelChoiceBox().valueProperty().addListener((observable, oldValue, newValue) -> {
			CacheUtils.setPlayMode(PlayMode.modeName(newValue));
		});

		// 音量进行缓存
		funcBottom.getVolumeSlider().valueProperty().addListener((ob, oldValue, newValue) -> {
			CacheUtils.setVolume(newValue.doubleValue());
		});
	}
}
