package com.s17201120.player.view;

import com.s17201120.player.util.ImageUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * @author  彭明明
 * @date  2020/6
 * @version 1.0
 */
public class TopTitle {

	private BorderPane pane;
	private Label logo;
	private Label minimize;
	private Label close;
	private TextField queryTextField;
	private Button button;

	public TopTitle() {
		init();
	}

	public void init() {
		pane = new BorderPane();
		pane.getStyleClass().add("player-topTitle");
		logo = new Label("17201120-彭明明");
		logo.getStyleClass().add("player-logo");

		ImageView minimizeIamgeView = ImageUtils.getImageView("minimize.png", 18, 18);
		minimize = new Label("", minimizeIamgeView);
		minimize.getStyleClass().addAll("player-top-min", "player-button");

		ImageView closeIamgeView = ImageUtils.getImageView("close.png", 18, 18);
		close = new Label("", closeIamgeView);
		close.getStyleClass().addAll("player-top-close", "player-button");

		HBox hBox = new HBox();
		hBox.getChildren().addAll(minimize, close);

		HBox hBox1 = new HBox();
		hBox1.getStyleClass().addAll("player-search");
		hBox1.setSpacing(5);
		queryTextField = new TextField();
		queryTextField.setMaxSize(140,50);
		queryTextField.setPromptText("搜索");
		button = new Button("搜索");
		hBox1.getChildren().addAll(queryTextField, button);
		pane.setLeft(logo);
		pane.setCenter(hBox1);
		pane.setRight(hBox);
	}

	public Label getMinimize() {
		return minimize;
	}

	public Label getClose() {
		return close;
	}

	public Pane getNode() {
		return pane;
	}

	public TextField getQueryTextField() {
		return queryTextField;
	}

	public void setQueryTextField(TextField queryTextField) {
		this.queryTextField = queryTextField;
	}

	public Button getButton() {
		return button;
	}

	public void setButton(Button button) {
		this.button = button;
	}
}
