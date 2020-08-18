package com.s17201120.player.model;

import com.flyfox.util.FileUtils;
import com.flyfox.util.NumberUtils;
import com.s17201120.player.util.PathUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class LyricLoader {


	 public Lyric load(Player player) {
		return loadLyric(player);
	 }

	/**
	 * 通过音频文件信息，获取歌词
	 */
	public Lyric loadLyric(Player player) {
		if (!containLocolLyric(player)) {
			return null;
		}

		Lyric lyric = new Lyric();
		String lyricPath = getLyricPath(player);
		try {
			byte[] data = FileUtils.read(lyricPath);
			String str = new String(data, "UTF-8");
			String[] lyricArray = str.split("\n");
			int lines = 0;

			List<LyricItem> listItem = new ArrayList<LyricItem>();
			for (int i = 0; i < lyricArray.length; i++) {
				String lyricStr = lyricArray[i].replace("\r", "");

				if (lyricStr.startsWith("[ti")) { // 歌曲
					lyricStr = lyricStr.replace("[ti:", "");
					lyricStr = lyricStr.replace("]", "");
					lyric.setName(lyricStr);
				} else if (lyricStr.startsWith("[ar")) { // 歌手
					lyricStr = lyricStr.replace("[ar:", "");
					lyricStr = lyricStr.replace("]", "");
					lyric.setSinger(lyricStr);
				} else if (lyricStr.startsWith("[al")) { // 所属专辑名
				} else if (lyricStr.startsWith("[by")) { // lrc歌词制作者
				} else if (lyricStr.startsWith("[offset")) { // 补偿时间
				} else {
					LyricItem item = new LyricItem();
					lyricStr = lyricStr.replace("[", "");
					String[] infoArray = lyricStr.split("]");
					// 不符合要求，不记录
					if (infoArray.length < 2) {
						continue;
					} else {
						// 处理多时间戳情况，正常infoArray.length=2 ，只循环一次
						for (int j = 0; j < infoArray.length - 1; j++) {
							// 时间处理
							String[] timeStr = infoArray[j].split(":");
								// 时间错误，丢弃
								if (timeStr.length < 2) {
									continue;
							}

							long time = (long) ((NumberUtils.parseDbl(timeStr[0]) * 60 + NumberUtils
									.parseDbl(timeStr[1])) * 1000);
							String content = infoArray[infoArray.length - 1];
							item.setTime(time);
							item.setContent(content);
							listItem.add(item);
							lines++;
						}
					}

				}
			}

			// 进行排序
			Collections.sort(listItem, (o1, o2) -> {
				if (o1.getTime() > o2.getTime()) {
					return 1;
				}
				if (o1.getTime() < o2.getTime()) {
					return -1;
				}
				return 0;
			});

			// 歌词行数
			lyric.setLyricItems(listItem);
			lyric.setLines(lines);
			// 成功
			lyric.setInfo(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lyric;
	}

	/**
	 * 本地是否存在歌词
	 */
	public boolean containLocolLyric(Player player) {
		String lyricPath = getLyricPath(player);
		File file = new File(lyricPath);
		if (!file.exists()) {
			return false;
		}

		return new File(lyricPath).exists();
	}

	public String getLyricPath(Player player) {
		String lyricPath = PathUtils.LYRIC_PATH + File.separator
				+ player.getArtist() + " - " + player.getTitle() + ".lrc";
		//System.out.println(lyricPath);
		return lyricPath;
	}

}
