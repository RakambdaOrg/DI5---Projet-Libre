package fr.mrcraftcod.scheduler.jfx;

import fr.mrcraftcod.scheduler.jfx.table.MatchTableView;
import fr.mrcraftcod.scheduler.model.GroupStage;
import fr.mrcraftcod.scheduler.utils.StringUtils;
import javafx.collections.FXCollections;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Tab containing infos about a group stage.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-17.
 *
 * @author Thomas Couchoud
 * @since 2019-01-17
 */
public class GroupStageTab extends Tab{
	private final GroupStage groupStage;
	
	/**
	 * Constructor.
	 *
	 * @param controller The controller.
	 * @param groupStage The group stage of this tab.
	 */
	public GroupStageTab(final Stage parentStage, final MainController controller, final GroupStage groupStage){
		super(groupStage.getName());
		this.groupStage = groupStage;
		
		final var matchPool = FXCollections.observableArrayList(groupStage.getMatches());
		
		final var root = new VBox();
		final var matchesTableView = new MatchTableView(parentStage, controller);
		matchesTableView.loadGroupStage(groupStage, matchPool);
		
		final var infos = new HBox();
		final var remainingMatchesLabel = new Text(StringUtils.getString("remaining_matches_to_set"));
		final var remainingMatches = new Text();
		remainingMatches.setText("" + matchPool.stream().filter(m -> !m.isAssigned()).count());
		matchPool.forEach(m -> m.assignedProperty().addListener(observable -> {
			remainingMatches.setText("" + matchPool.stream().filter(m2 -> !m2.isAssigned()).count());
		}));
		infos.getChildren().addAll(remainingMatchesLabel, remainingMatches);
		
		root.getChildren().addAll(matchesTableView, infos);
		VBox.setVgrow(matchesTableView, Priority.ALWAYS);
		
		this.setContent(root);
		this.setClosable(false);
	}
	
	/**
	 * Get the group stage this tab represents.
	 *
	 * @return The groupstage.
	 */
	public GroupStage getGroupStage(){
		return this.groupStage;
	}
}
