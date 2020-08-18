package com.s17201120.player.model;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import com.s17201120.player.util.PlayerUtils;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class Player {

	private String id; // 标示
	private String path; // 全路径
	private String fileName; // 名称
	private String album; // 专辑
	private String artist; // 艺术家
	private String title; // 名称
	private Duration totleTime = Duration.UNKNOWN; // 总时间
	private MusicType type = MusicType.MP3; // 类型
	private MediaPlayer mediaPlayer; // media载体

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDuration() {
		return PlayerUtils.formatTime(totleTime);
	}

	public Duration getTotleTime() {
		return totleTime;
	}

	public void setTotleTime(Duration totleTime) {
		this.totleTime = totleTime;
	}

	public MusicType getType() {
		return type;
	}

	public void setType(MusicType type) {
		this.type = type;
	}

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public void setMediaPlayer(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "[id=" + id + ":name=" + title + "：singer=" + artist //
				+ "：path=" + path + "]";
	}
}
