package fr.mrcraftcod.shcheduler.jfx;

import fr.mrcraftcod.shcheduler.jfx.table.MatchMenuButton;
import fr.mrcraftcod.shcheduler.model.GroupStage;
import fr.mrcraftcod.shcheduler.model.Gymnasium;
import fr.mrcraftcod.shcheduler.model.Match;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.Objects;

public class GymnasiumMatchTableCell extends TableCell<Gymnasium, ObservableList<Match>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(GymnasiumMatchTableCell.class);
	private final ObservableList<Match> matchPool;
	private final LocalDate date;
	private final MainController controller;
	private ObservableList<Match> matches;
	private MatchMenuButton matchMenuButton;
	private GroupStage groupStage;
	
	public GymnasiumMatchTableCell(final GroupStage gs, final MainController controller, final LocalDate date, final ObservableList<Match> matchPool){
		super();
		this.controller = controller;
		this.groupStage = gs;
		this.matches = null;
		this.matchPool = matchPool;
		this.date = date;
		
		this.setAlignment(Pos.CENTER);
		setPrefHeight(Control.USE_COMPUTED_SIZE);
	}
	
	@Override
	public void updateItem(final ObservableList<Match> item, final boolean empty){
		super.updateItem(item, empty);
		if(!empty){
			if(Objects.nonNull(item)){
				matches = item;
				matches.forEach(match -> controller.assignMatch(match, getGymnasium(), getDate()));
			}
			else if(Objects.nonNull(matches)){
				matches.forEach(match -> controller.assignMatch(match, null, null));
				this.setStyle("");
				matches = null;
			}
			setGraphic(getCellContent(item));
			setText(null);
		}
	}
	
	public Gymnasium getGymnasium(){
		if(this.getTableView().getItems().size() > this.getTableRow().getIndex() && this.getTableRow().getIndex() >= 0){
			return this.getTableView().getItems().get(this.getTableRow().getIndex());
		}
		return null;
	}
	
	private Node getCellContent(final ObservableList<Match> matches){
		final var vBox = new VBox(5);
		if(Objects.nonNull(matches)){
			for(final var match : matches){
				if(Objects.nonNull(match)){
					final var group = new FlowPane();
					final var text = new Text();
					text.wrappingWidthProperty().bind(widthProperty());
					text.setText(match.getTeam1().getName() + "\nVS\n" + match.getTeam2().getName());
					text.setTextAlignment(TextAlignment.CENTER);
					group.setStyle(String.format("-fx-background-color: linear-gradient(to bottom, %s 35%%, %s 65%% 10%%);", match.getTeam1().getGymnasium().getColor(), match.getTeam2().getGymnasium().getColor()));
					group.getChildren().add(text);
					vBox.getChildren().add(group);
				}
			}
		}
		
		final var prop = controller.remainingPlaceProperty(getGymnasium(), getDate());
		final var group = new FlowPane();
		final var t = new Text();
		t.wrappingWidthProperty().bind(widthProperty().add(-30));
		t.textProperty().bind(prop.asString("%1$+d"));
		t.setTextAlignment(TextAlignment.CENTER);
		
		group.setMaxHeight(Double.MAX_VALUE);
		group.managedProperty().bind(prop.greaterThan(0));
		group.visibleProperty().bind(prop.greaterThan(0));
		group.setAlignment(Pos.CENTER);
		group.getStyleClass().add("places-counter");
		group.getChildren().add(t);
		vBox.getChildren().add(group);
		
		VBox.setVgrow(group, Priority.ALWAYS);
		
		return vBox;
	}
	
	@Override
	public void startEdit(){
		if(!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()){
			return;
		}
		
		if(matchMenuButton == null){
			matchMenuButton = new MatchMenuButton(this, matchPool.filtered(m -> Objects.equals(getGymnasium(), m.getTeam1().getGymnasium()) || Objects.equals(getGymnasium(), m.getTeam2().getGymnasium())), controller);
		}
		final var valid = new Button("OK");
		valid.setOnAction(evt -> GymnasiumMatchTableCell.this.commitEdit(matchMenuButton.getCheckedItems()));
		valid.setMaxWidth(Double.MAX_VALUE);
		
		final var vBox = new VBox(matchMenuButton, valid);
		vBox.setOnKeyReleased(t -> {
			if(t.getCode() == KeyCode.ENTER || t.getCode() == KeyCode.ESCAPE){
				commitEdit(matchMenuButton.getCheckedItems());
			}
		});
		
		super.startEdit();
		setText(null);
		setGraphic(vBox);
	}
	
	public LocalDate getDate(){
		return date;
	}
	
	@Override
	public void cancelEdit(){
		this.commitEdit(matchMenuButton.getCheckedItems());
	}
	
	public GroupStage getGroupStage(){
		return this.groupStage;
	}
}

