package fr.mrcraftcod.shcheduler.jfx;

import fr.mrcraftcod.shcheduler.jfx.utils.NumberTextField;
import fr.mrcraftcod.shcheduler.model.Gymnasium;
import fr.mrcraftcod.shcheduler.utils.StringUtils;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Creathed by mrcraftcod (MrCraftCod - zerderr@gmail.com) on 2019-02-15.
 *
 * @author Thomas Couchoud
 * @since 2019-02-15
 */
public class EditGymnasiumStage{
	public EditGymnasiumStage(final Stage parentStage, final Gymnasium gymnasium){
		final var dialog = new Stage();
		
		final var scene = buildScene(dialog, gymnasium);
		dialog.setTitle(StringUtils.getString("frame_title_gymnasium_edit", gymnasium.getName()));
		dialog.setScene(scene);
		dialog.sizeToScene();
		
		dialog.initOwner(parentStage);
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.showAndWait();
	}
	
	private Scene buildScene(final Stage stage, final Gymnasium gymnasium){
		return new Scene(buildContent(stage, gymnasium));
	}
	
	private Parent buildContent(final Stage stage, final Gymnasium gymnasium){
		final var root =  new VBox(3);
		
		final var capacityBox = new HBox();
		
		final var capacityInput = new NumberTextField();
		capacityInput.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(capacityInput, Priority.ALWAYS);
		
		capacityInput.numberProperty().bindBidirectional(gymnasium.capacityProperty());
		
		capacityBox.getChildren().addAll(new Text(StringUtils.getString("edit_gymnasium_capacity")), capacityInput);
		root.getChildren().add(capacityBox);
		
		return root;
	}
}