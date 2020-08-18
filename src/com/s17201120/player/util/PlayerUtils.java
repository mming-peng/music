package com.s17201120.player.util;

import com.s17201120.player.model.Player;
import com.s17201120.player.model.MusicType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class PlayerUtils {

	public final static AtomicBoolean CLOSE = new AtomicBoolean(true);

	private static AtomicLong genID = new AtomicLong();

	/**
	 * 获取Player信息，进行初始化
	 */
	public static Player getPlayer(String path) {
		String music = path.substring(path.lastIndexOf(File.separatorChar) + 1);
		MediaPlayer mediaPlayer = PlayerUtils.getMediaPlayer(path);
		Player model = new Player();
		// 全路径
		model.setPath(path);
		// 唯一ID
		model.setId(genID.getAndIncrement() + "");
		model.setMediaPlayer(mediaPlayer);
		String name = music.substring(0, music.lastIndexOf("."));
		model.setFileName(name);
		String type = music.substring(music.lastIndexOf(".") + 1);
		MusicType musicType = MusicType.valueOf(type.toUpperCase());
		if (musicType == null) {
			model.setType(MusicType.MP3);
		} else {
			model.setType(musicType);
		}

		// 读取MP3信息
		try {
			MP3Info mp3Info = MP3Utils.readMP3(model.getPath());
			if (mp3Info != null) {
				if (!PlayerUtils.isMessyCode(mp3Info.getTitle())) {
					model.setTitle(mp3Info.getTitle());
				}
				if (!PlayerUtils.isMessyCode(mp3Info.getAlbum())) {
					model.setAlbum(mp3Info.getAlbum());
				}
				if (!PlayerUtils.isMessyCode(mp3Info.getArtist())) {
					model.setArtist(mp3Info.getArtist());
				}
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		// 如果文件没有获取到MP3题目，取文件名
		if (model.getTitle() == null) {
			if (name.indexOf(" - ") > 0) {
				model.setTitle(name.substring(0, name.indexOf(" - ")));
			} else {
				model.setTitle(name);
			}
		}

		return model;
	}

	public static MediaPlayer getMediaPlayer(String filePath) {
		File file = new File(filePath);
		Media media = new Media(file.toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		return mediaPlayer;
	}

	/**
	 * 判断字符是否是中文
	 *
	 * @param c
	 *            字符
	 * @return 是否是中文
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否是乱码
	 */
	public static boolean isMessyCode(String strName) {
		if (strName == null) {
			return false;
		}

		if (isMessyCode2(strName)) {
			return true;
		}

		Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
		Matcher m = p.matcher(strName);
		String after = m.replaceAll("");
		String temp = after.replaceAll("\\p{P}", "");
		char[] ch = temp.trim().toCharArray();
		float chLength = ch.length;
		float count = 0;
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!Character.isLetterOrDigit(c)) {
				if (!isChinese(c)) {
					count = count + 1;
				}
			}
		}
		float result = count / chLength;
		if (result > 0.4) {
			return true;
		} else {
			return false;
		}

	}

	private static boolean isMessyCode2(String str) {
		if (str == null)
			return false;

		try {
			byte[] b = str.getBytes("ISO8859_1");

			for (int i = 0; i < b.length; i++) {
				byte b1 = b[i];
				if (b1 == 63)
					break; // 1
				else if (b1 > 0)
					continue;// 2
				else if (b1 < 0) { // 不可能为0，0为字符串结束符
					return true;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String formatTime(Duration duration) {
		int totalSec = (int) Math.floor(duration.toSeconds());
		int hours = totalSec / (60 * 60);
		if (hours > 0) {
			totalSec -= hours * 60 * 60;
		}
		int min = totalSec / 60;
		int sec = totalSec - hours * 60 * 60 - min * 60;


		if (hours > 0) {
			return String.format("%d:%02d:%02d", hours, min, sec);
		} else {
			return String.format("%02d:%02d", min, sec);
		}
	}
}
