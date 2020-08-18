package com.s17201120.player.model;

import java.io.Serializable;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class LyricItem implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 时间戳
	 */
	private long time;

	/**
	 * 当前行歌词内容
	 */
	private String content;

	public LyricItem() {
	}

	public LyricItem(long time, String content) {
		this.time = time;
		this.content = content;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
