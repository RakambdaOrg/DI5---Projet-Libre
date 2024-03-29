package fr.mrcraftcod.scheduler.jfx;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import fr.mrcraftcod.scheduler.CLIParameters;
import fr.mrcraftcod.scheduler.Exporter;
import fr.mrcraftcod.scheduler.Parser;
import fr.mrcraftcod.scheduler.exceptions.ParserException;
import fr.mrcraftcod.scheduler.jfx.utils.JFXUtils;
import fr.mrcraftcod.scheduler.model.GroupStage;
import fr.mrcraftcod.scheduler.model.Team;
import fr.mrcraftcod.scheduler.utils.StringUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.SwingUtilities;
import java.awt.Taskbar;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Main application window.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-17.
 *
 * @author Thomas Couchoud
 * @since 2019-01-17
 */
public class MainApplication extends Application{
	private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);
	private Stage stage;
	private MainController controller;
	private TabPane tabPane;
	
	@Override
	public void start(final Stage stage){
		this.stage = stage;
		this.controller = new MainController();
		final var scene = buildScene();
		stage.setTitle(StringUtils.getString("frame_title"));
		stage.setScene(scene);
		stage.sizeToScene();
		setIcon();
		stage.show();
		onStageDisplayed();
	}
	
	/**
	 * Main call.
	 *
	 * @param args See {@link CLIParameters}.
	 */
	public static void main(final String[] args){
		launch(args);
	}
	
	/**
	 * Build the scene.
	 *
	 * @return The scene.
	 */
	private Scene buildScene(){
		return new Scene(createContent(), 640, 640);
	}
	
	/**
	 * Create the scene content.
	 *
	 * @return The root content.
	 */
	private Parent createContent(){
		tabPane = new TabPane();
		
		final var menuBar = new MenuBar();
		final var os = System.getProperty("os.name");
		if(os != null && os.startsWith("Mac")){
			menuBar.useSystemMenuBarProperty().set(true);
		}
		
		final var menuConstraints = new Menu(StringUtils.getString("menu_edit"));
		final var menuConstraintsBannedGymnasiumDates = new MenuItem(StringUtils.getString("menu_edit_gymnasiums"));
		menuConstraintsBannedGymnasiumDates.setOnAction(evt -> {
			final var selectedTab = tabPane.getSelectionModel().selectedItemProperty().get();
			if(selectedTab instanceof GroupStageTab){
				new EditGymnasiumListStage(getStage(), MainApplication.this.controller.getChampionship().getGroupStages().stream().flatMap(gs -> gs.getTeams().stream()).map(Team::getGymnasium).distinct().collect(Collectors.toList()));
			}
		});
		
		final var menuExport = new Menu(StringUtils.getString("menu_export"));
		final var menuExportSchedule = new MenuItem(StringUtils.getString("menu_export_schedule"));
		final var menuExportScheduleGroupStage = new MenuItem(StringUtils.getString("menu_export_schedule_group_stage"));
		menuExportSchedule.setOnAction(evt -> {
			final var directoryChooser = new DirectoryChooser();
			final var selectedDirectory = directoryChooser.showDialog(stage);
			
			if(Objects.nonNull(selectedDirectory)){
				Exporter.exportChampionship(MainApplication.this.controller.getChampionship(), Paths.get(selectedDirectory.toURI()));
				final var alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle(StringUtils.getString("alert_success"));
				alert.setHeaderText("");
				alert.setContentText(StringUtils.getString("alert_export_success"));
				alert.setGraphic(new ImageView(this.getClass().getResource("/jfx/checkmark.png").toString()));
				alert.showAndWait();
			}
			else{
				final var alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle(StringUtils.getString("alert_error"));
				alert.setHeaderText("");
				alert.setContentText(StringUtils.getString("alert_error_export_no_folder"));
				alert.showAndWait();
			}
		});
		menuExportScheduleGroupStage.setOnAction(evt -> {
			final var selectedTab = tabPane.getSelectionModel().selectedItemProperty().get();
			if(selectedTab instanceof GroupStageTab){
				final var directoryChooser = new DirectoryChooser();
				final var selectedDirectory = directoryChooser.showDialog(stage);
				
				if(Objects.nonNull(selectedDirectory)){
					Exporter.exportGroupStage(((GroupStageTab) selectedTab).getGroupStage(), Paths.get(selectedDirectory.toURI()));
					final var alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle(StringUtils.getString("alert_success"));
					alert.setHeaderText("");
					alert.setContentText(StringUtils.getString("alert_export_success"));
					alert.setGraphic(new ImageView(this.getClass().getResource("/jfx/checkmark.png").toString()));
					alert.showAndWait();
				}
				else{
					final var alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle(StringUtils.getString("alert_error"));
					alert.setHeaderText("");
					alert.setContentText(StringUtils.getString("alert_error_export_no_folder"));
					alert.showAndWait();
				}
			}
			else{
				final var alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle(StringUtils.getString("alert_error"));
				alert.setContentText(StringUtils.getString("alert_error_export_no_tab"));
				alert.showAndWait();
			}
		});
		
		menuConstraints.getItems().addAll(menuConstraintsBannedGymnasiumDates);
		menuExport.getItems().addAll(menuExportSchedule, menuExportScheduleGroupStage);
		menuBar.getMenus().addAll(menuConstraints, menuExport);
		
		final var root = new BorderPane();
		root.setCenter(tabPane);
		root.setTop(menuBar);
		return root;
	}
	
	/**
	 * Set the icon of the application.
	 */
	private void setIcon(){
		final var icon = new Image("/jfx/icon.png");
		this.stage.getIcons().clear();
		this.stage.getIcons().add(icon);
		if(Taskbar.isTaskbarSupported() && Taskbar.getTaskbar().isSupported(Taskbar.Feature.ICON_IMAGE)){
			Taskbar.getTaskbar().setIconImage(SwingFXUtils.fromFXImage(icon, null));
		}
	}
	
	/**
	 * Called when the stage is displayed.
	 */
	private void onStageDisplayed(){
		final var parameters = new CLIParameters();
		try{
			JCommander.newBuilder().addObject(parameters).build().parse(this.getParameters().getRaw().toArray(new String[0]));
		}
		catch(final ParameterException e){
			// LOGGER.error("Failed to parse arguments", e);
			e.usage();
			System.exit(1);
		}
		try{
			final var championship = new Parser(';', parameters.getChampionshipWeeks()).parse(parameters.getCsvGymnasiumConfigFile(), parameters.getCsvTeamConfigFile());
			controller.setChampionship(championship);
			championship.getGroupStages().stream().sorted(Comparator.comparing(GroupStage::getName)).forEach(gs -> tabPane.getTabs().add(new GroupStageTab(getStage(), controller, gs)));
		}
		catch(final ParserException | IOException e){
			LOGGER.error("Error parsing config", e);
			SwingUtilities.invokeLater(() -> {
				new JFXPanel(); // this will prepare JavaFX toolkit and environment
				Platform.runLater(() -> {
					JFXUtils.displayExceptionAlert(e, "Scheduler error", "Error while starting", "Failed to parse configuration");
					System.exit(1);
				});
			});
		}
	}
	
	/**
	 * Get the stage.
	 *
	 * @return The stage.
	 */
	@SuppressWarnings("unused")
	public Stage getStage(){
		return stage;
	}
}
