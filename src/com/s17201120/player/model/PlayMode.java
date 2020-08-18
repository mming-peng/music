package com.s17201120.player.model;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public enum PlayMode {
	/**
	 * 循环播放
	 */
	CYCLING("随机播放"),
	/**
	 * 随机播放
	 */
	RANDOM("循环播放"),
	/**
	 * 单曲循环
	 */
	ONE_CYCLING("单曲循环"),
	/**
	 * 顺序播放
	 */
	ORDER("顺序播放");

	private String name;

	private PlayMode(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static PlayMode modeName(String name) {
		switch (name) {
		case "随机播放":
			return RANDOM;
		case "循环播放":
			return CYCLING;
		case "单曲循环":
			return ONE_CYCLING;
		case "顺序播放":
			return ORDER;
		default:
			break;
		}
		return null;
	}
}
