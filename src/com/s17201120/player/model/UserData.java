package com.s17201120.player.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class UserData implements Serializable {

	private static final long serialVersionUID = -7307085103810503931L;

	/**
	 * 播放列表
	 */
	private List<String> tableData = new LinkedList<String>();
	/**
	 * 播放位置
	 */
	private int index = 0;

	/**
	 * 播放模式
	 */
	private PlayMode playMode = PlayMode.ORDER;

	/**
	 * 音量
	 */
	private double volume = -1;

	public List<String> getTableData() {
		return tableData;
	}

	public void setTableData(List<String> tableData) {
		this.tableData = tableData;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public PlayMode getPlayMode() {
		return playMode;
	}

	public void setPlayMode(PlayMode playMode) {
		this.playMode = playMode;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}
}
