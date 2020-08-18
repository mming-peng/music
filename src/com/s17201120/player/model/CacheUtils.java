package com.s17201120.player.model;

import com.flyfox.util.FileUtils;
import com.flyfox.util.serializable.Serializer;
import com.s17201120.player.util.PlayerUtils;
import com.s17201120.player.util.FSTSerializer;
import com.s17201120.player.util.PathUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class CacheUtils {

	/**
	 * 用户信息对象
	 */
	private static UserData userData = new UserData();

	/**
	 * fst序列化
	 */
	private static final Serializer SERIALIZER = new FSTSerializer();
	/**
	 * 列表数据
	 */
	private static final String path = PathUtils.RESOURCES_PATH + File.separator + "music.data";

	static {
		// 初始化数据
		try {
			byte[] data = FileUtils.read(path);
			if (data != null && data.length > 0) {
				userData = SERIALIZER.deserialize(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将列表序列化
	 */
	public static void update() {
		PlayerUtils.CLOSE.set(false);
		try {
			byte[] data = SERIALIZER.serialize(userData);
			FileUtils.write(path, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PlayerUtils.CLOSE.set(true);
	}

	public static List<String> getTableData() {
		return userData.getTableData();
	}

	public static int getIndex() {
		return userData.getIndex();
	}

	public static PlayMode getPlayMode() {
		return userData.getPlayMode();
	}

	public static double getVolume() {
		return userData.getVolume();
	}
	
	public static void setTableData(List<String> tableData) {
		userData.setTableData(tableData);
		CacheUtils.update();
	}
	
	public static void setIndex(int index) {
		userData.setIndex(index);
		CacheUtils.update();
	}

	public static void setVolume(double volume) {
		userData.setVolume(volume);
		CacheUtils.update();
	}
	
	public static void setPlayMode(PlayMode playMode) {
		userData.setPlayMode(playMode);
		CacheUtils.update();
	}

	public static boolean contains(String path) {
		return userData.getTableData().contains(path);
	}

	public static void add(String path) {
		userData.getTableData().add(path);
		CacheUtils.update();
	}

	public static void remove(int index) {
		userData.getTableData().remove(index);
		CacheUtils.update();
	}

	public static void remove(String path) {
		userData.getTableData().remove(path);
		CacheUtils.update();
	}

	public static void clear() {
		setIndex(0); // 设置为0
		userData.getTableData().clear();
		CacheUtils.update();
	}

}
