package com.s17201120.player.util;

import javafx.util.Duration;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class MP3Info {

	private String album; // 专辑
	private String artist; // 艺术家
	private String title; // 名称
	private Duration totleTime = Duration.UNKNOWN; // 总时间

	public Duration getTotleTime() {
		return totleTime;
	}

	public void setTotleTime(Duration totleTime) {
		this.totleTime = totleTime;
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
}
