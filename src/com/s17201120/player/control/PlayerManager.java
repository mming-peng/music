package com.s17201120.player.control;

import com.s17201120.player.model.CacheUtils;
import com.s17201120.player.model.Player;
import com.s17201120.player.util.PathUtils;
import com.s17201120.player.util.PlayerUtils;
import com.s17201120.player.view.FuncBottom;
import com.s17201120.player.view.LyricView;
import com.s17201120.player.view.MusicList;
import com.s17201120.player.view.TopTitle;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.security.SecureRandom;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class PlayerManager {

	/**
	 * 播放列表
	 */
	private static SecureRandom random = new SecureRandom();

	/**
	 * 播放测试列表
	 */
	Player model;

	// 展示信息
	FuncBottom funcBottom = new FuncBottom();
	MusicList musicList = new MusicList();
	LyricView lyricView = new LyricView();
	TopTitle topTitle = new TopTitle();
	Stage stage;

	MusicEvent musicEvent;

	public PlayerManager(Stage stage) {
		this.stage = stage;

		init();
	}

	/**
	 * 初始化
	 */
	public void init() {
		// 底部操作栏事件绑定
		new FuncBottomEvent(this).bind();
		// 列表事件绑定
		new MusicListEvent(this).bind();
		// 音频文件绑定事件
		musicEvent = new MusicEvent(this);

		if (musicList.isEmpty()) {
			String tmpPath = PathUtils.RESOURCES_PATH + File.separatorChar + "Jam - 七月上.mp3";
			Player model = PlayerUtils.getPlayer(tmpPath);
			// 加入列表
			musicList.add(model);
		}

		// 获取播放记录
		if (!musicList.isEmpty()) {
			model = musicList.get(CacheUtils.getIndex());
			funcBottom.getPlayModelChoiceBox().setValue(CacheUtils.getPlayMode().getName());
			bindMusic();
			play();
		}

	}

	/**
	 * 歌曲事件重新绑定
	 */
	public void bindMusic() {
		musicEvent.bind();
	}

	/**
	 * 下一首
	 */
	public void playNext() {
		stop();
		// 获取下一首，播放
		int index = CacheUtils.getIndex();
		index += 1;
		if (index >= musicList.size()) {
			index = 0;
		}
		CacheUtils.setIndex(index);
		model = musicList.get(index);
		musicEvent.bind();

		play();
	}

	/**
	 * 上一首
	 */
	public void playPre() {
		stop();
		// 获取下一首，播放
		int index = CacheUtils.getIndex();
		index -= 1;
		if (index < 0) {
			index = musicList.size() - 1;
		}
		CacheUtils.setIndex(index);
		model = musicList.get(index);
		musicEvent.bind();

		play();
	}

	/**
	 * 停止
	 */
	public void stop() {
		if (model == null) {
			return;
		}
		getMediaPlayer().stop();
	}

	/**
	 * 暂停
	 */
	public void pause() {
		getMediaPlayer().pause();
	}

	/**
	 * 播放
	 */
	public void play() {
		getMediaPlayer().play();
	}

	/**
	 * 跳到播放位置
	 */
	public void seek(Duration seconds) {
		getMediaPlayer().seek(seconds);
	}

	/**
	 * 播放状态
	 */
	public Status getStatus() {
		return getMediaPlayer().getStatus();
	}

	/**
	 * 播放结束
	 */
	public void playEnd() {
		switch (CacheUtils.getPlayMode()) {
		case CYCLING:
			// 循环，如果是最后一首，就放第一首
			playNext();
			break;
		case ONE_CYCLING:
			// 单曲循环
			seek(Duration.ZERO);
			break;
		case ORDER:
			playOrder();
			break;
		case RANDOM:
			playRandom();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 随机播放
	 */
	private void playRandom() {
		stop();
		CacheUtils.setIndex(random.nextInt(musicList.size()));
		model = musicList.get(CacheUtils.getIndex());
		musicEvent.bind();
		play();
	}

	/**
	 * 顺序播放
	 */
	private void playOrder() {
		// 如果是最后一首，就结束
		int tmpIndex = CacheUtils.getIndex() + 1;
		if (tmpIndex >= musicList.size()) {
			seek(Duration.ZERO);
			stop();
		} else {
			playNext();
		}
	}

	// ////////////////////////////////////////////////////////////

	public MediaPlayer getMediaPlayer() {
		return model.getMediaPlayer();
	}

	public FuncBottom getFuncBottom() {
		return funcBottom;
	}

	public MusicList getMusicList() {
		return musicList;
	}

	public LyricView getLyricView() {
		return lyricView;
	}

	public TopTitle getTopTitle() {
		return topTitle;
	}

	public Player getModel() {
		return model;
	}

	public void setModel(Player model) {
		this.model = model;
	}

	public Stage getStage() {
		return stage;
	}

}
