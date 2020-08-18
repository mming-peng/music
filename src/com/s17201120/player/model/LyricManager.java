package com.s17201120.player.model;

import com.s17201120.player.client.LyricRequestClient;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class LyricManager {
	private static LyricLoader lyricLoader = new LyricLoader();

	public static Lyric load(Player player) {
		Lyric lyric = lyricLoader.load(player);
		if(lyric==null){
			String reqPath = player.getArtist() + " - " + player.getTitle() + ".lrc";
			LyricRequestClient lyr = new  LyricRequestClient(reqPath);
			lyr.requestLyric(reqPath);
		}
		return lyric;
	}

}
