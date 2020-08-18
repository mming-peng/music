package com.s17201120.player.view;

import com.s17201120.player.model.CacheUtils;
import com.s17201120.player.model.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import com.s17201120.player.util.PlayerUtils;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class MusicList {

	private TableView<Player> playTable = new TableView<Player>();
	// 数据列表
	private final ObservableList<Player> tableData = FXCollections.observableArrayList();

	public MusicList() {
		init();
	}

	@SuppressWarnings("unchecked")
	private void init() {
		playTable.getStyleClass().addAll("player-musicList");
		
		// 名称
		TableColumn<Player, String> title = new TableColumn<Player, String>("歌曲");
		title.setCellValueFactory(new PropertyValueFactory<Player, String>("title"));
		title.setPrefWidth(150);
		// 艺术家
		TableColumn<Player, String> artist = new TableColumn<Player, String>("歌手");
		artist.setCellValueFactory(new PropertyValueFactory<Player, String>("artist"));
		artist.setPrefWidth(90);
		// 专辑
		TableColumn<Player, String> album = new TableColumn<Player, String>("专辑");
		album.setCellValueFactory(new PropertyValueFactory<Player, String>("album"));
		album.setPrefWidth(130);

		playTable.getColumns().addAll(title, artist, album);

		// 初始化列表
		if (CacheUtils.getTableData().size() > 0) {
			CacheUtils.getTableData().forEach(path -> {
				Player player = PlayerUtils.getPlayer(path);
				tableData.add(player);
			});
		}

		// 数据绑定
		playTable.setItems(tableData);

	}

	public TableView<Player> getTable() {
		return playTable;
	}

	public TableView<Player> getNode() {
		return playTable;
	}

	/**
	 * 添加音乐
	 */
	public void add(Player music) {
		// 不添加相同数据
		if (!CacheUtils.contains(music.getPath())) {
			tableData.add(music);
			// 缓存同步
			CacheUtils.add(music.getPath());
		}
	}

	/**
	 * 删除音乐
	 */
	public void remove(int index) {
		tableData.remove(index);
		// 缓存同步
		CacheUtils.remove(index);
	}

	/**
	 * 删除音乐
	 */
	public void remove(Player music) {
		tableData.remove(music);
		// 缓存同步
		CacheUtils.remove(music.getPath());
	}

	public void clear() {
		tableData.clear();
		// 缓存同步
		CacheUtils.clear();
	}

	public Player getFirst() {
		return get(0);
	}

	public Player getLast() {
		return get(tableData.size() - 1);
	}

	public Player get(int index) {
		return tableData.get(index);
	}

	/**
	 * 是否可以播放
	 */
	public boolean isEmpty() {
		return tableData.size() <= 0;
	}

	public int size() {
		return tableData.size();
	}

	public ObservableList<Player> getTableData() {
		return tableData;
	}

}
