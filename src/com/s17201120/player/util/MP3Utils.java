package com.s17201120.player.util;

import com.flyfox.util.StrUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class MP3Utils {
	public static MP3Info readMP3(String path) throws IOException {

		byte[] header = new byte[3];// 头信息
		RandomAccessFile raf = null;
		try {
			// 随机读写方式打开MP3文件
			raf = new RandomAccessFile(path, "r");
			raf.seek(0);
			raf.read(header, 0, 3);// 读取标签信息
			// ID3V2
			if ("ID3".equalsIgnoreCase(new String(header))) {
				return readV2(raf);
			} else {
				raf.seek(raf.length() - 128);
				raf.read(header, 0, 3);// 读取标签信息
				// ID3V1
				if ("TAG".equalsIgnoreCase(new String(header))) {
					return readV1(raf);
				}
			}
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("获取文件失败：" + path + "\terror:" + e.getMessage());
		} catch (IOException e) {
			throw new IOException("读取文件失败：" + path + "\terror:" + e.getMessage());
		} finally {
			try {
				// 关闭文件
				if (raf != null)
					raf.close();
			} catch (IOException e) {
				throw new IOException("关闭文件失败：" + path + "\terror:" + e.getMessage());
			}
		}

		return null;
	}

	private static MP3Info readV2(RandomAccessFile raf) throws IOException, UnsupportedEncodingException {
		MP3Info mp3Info = new MP3Info();
		// header 3 version 1 reVersion 1 flag 1 size 4
		raf.seek(10);

		byte[] frameHead = new byte[10];
		raf.read(frameHead);
		//System.out.println(raf.get);
		String frameId = new String(frameHead, 0, 4);

		while (decodeFrame(frameId)) {
			/**
			 * 标签内容大小 减去 '\0'之后的标签内容大小
			 */
			int qw = frameHead[4];
			int bw = frameHead[5];
			int sw = frameHead[6];
			int gw = frameHead[7];
			if (qw < 0) {
				qw = Math.abs(qw) + 128;
			}
			if (bw < 0) {
				bw = Math.abs(bw) + 128;
			}
			if (sw < 0) {
				sw = Math.abs(sw) + 128;
			}
			if (gw < 0) {
				gw = Math.abs(gw) + 128;
			}
			int contentSize = qw * 0x1000000 + bw * 0x10000 + sw * 0x100 + gw - 1;
			/**
			 * 根据标签内容大小获取标签内容
			 */
			byte[] content = new byte[contentSize];

			/**
			 * 跳过一个字节 '\0'
			 */
			raf.skipBytes(1);
			/**
			 * 读取帧内容
			 */
			raf.read(content);
			String value = new String(content, "UTF-16");
			if ("TPE1".equals(frameId)) {
				mp3Info.setArtist(value);
			} else if ("TALB".equals(frameId)) {
				mp3Info.setAlbum(value);
			} else if ("TIT2".equals(frameId)) {
				mp3Info.setTitle(value);
			}

			// System.out.println(frameId + ":" + value);

			/**
			 * 将帧内容加入到歌曲信息
			 */
			raf.read(frameHead);
			frameId = new String(frameHead, 0, 4);
		}
		return mp3Info;
	}

	private static boolean decodeFrame(String frameId) {
		/**
		 * 将读取到的开头四个字节匹配字符串[A-Z]{3}[A-Z0-9]{1} 匹配不成功就返回空标签
		 */
		Pattern pattern = Pattern.compile("[A-Z]{3}[A-Z0-9]{1}");
		Matcher matcher = pattern.matcher(frameId);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	private static MP3Info readV1(RandomAccessFile raf) throws IOException {
		MP3Info mp3Info = new MP3Info();

		byte[] buf = new byte[125];// 初始化标签信息的byte数组,去除头信息3个字节
		raf.read(buf);// 读取标签信息
		// 歌曲名称
		String songName = new String(buf, 0, 30, "GBK").trim();
		if (StrUtils.isNotEmpty(songName) && !PlayerUtils.isMessyCode(songName)) {
			mp3Info.setTitle(songName);
		}
		String artist = new String(buf, 30, 30, "GBK").trim();// 歌手名字
		if (StrUtils.isNotEmpty(artist) && !PlayerUtils.isMessyCode(artist)) {
			mp3Info.setArtist(artist);
		}
		// 专辑
		String album = new String(buf, 60, 30, "GBK").trim();// 专辑名称
		if (StrUtils.isNotEmpty(album) && !PlayerUtils.isMessyCode(album)) {
			mp3Info.setAlbum(album);
			System.out.println(album);
		}
		return mp3Info;
	}
}
