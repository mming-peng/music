package com.s17201120.player.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class Lyric implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 歌曲名称
	 */
	private String name;

	/**
	 * 歌手名称
	 */
	private String singer;

	/**
	 * 歌词项目信息
	 */
	private List<LyricItem> lyricItems = new ArrayList<LyricItem>();

	/**
	 * 歌词总行数, 不包括无效歌词行.
	 */
	private int lines;

	/**
	 * 时长
	 */
	private long total;
	/**
	 * 扩展信息,记录状态和位置
	 */
	private int info;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public List<LyricItem> getLyricItems() {
		return lyricItems;
	}

	public void setLyricItems(List<LyricItem> lyricItems) {
		this.lyricItems = lyricItems;
	}

	public int getLines() {
		return lines;
	}

	public void setLines(int lines) {
		this.lines = lines;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getInfo() {
		return info;
	}

	public void setInfo(int info) {
		this.info = info;
	}

	@Override
	public String toString() {
		StringBuffer items = new StringBuffer();
		lyricItems.forEach(n -> items.append("\r\n").append(n.getTime()).append(":").append(n.getContent()));
		return "[name=" + name + "：singer=" + singer //
				+ "：lines=" + lines + "：total=" + total //
				+ "：info=" + info + "]" + items.toString();
	}
}
