package com.s17201120.player;

import com.s17201120.player.view.MainView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class MainPlayer extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage stage) throws Exception {
		MainView main = new MainView(stage);
		main.init();
	}

}
