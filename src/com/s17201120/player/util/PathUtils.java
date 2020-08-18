package com.s17201120.player.util;

import java.io.File;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class PathUtils {
	public final static String USER_PATH = System.getProperty("user.dir");
	public static String RESOURCES_PATH = USER_PATH + File.separator + "resources";
	public final static String LYRIC_PATH = USER_PATH + File.separator + "resources" + File.separator + "lyric";

}
