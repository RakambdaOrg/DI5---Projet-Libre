package fr.mrcraftcod.shcheduler.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a championship.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-21.
 *
 * @author Thomas Couchoud
 * @since 2019-01-21
 */
public class Championship{
	private final Collection<GroupStage> groupStages;
	
	/**
	 * Constructor.
	 */
	public Championship(){
		this.groupStages = new ArrayList<>();
	}
	
	/**
	 * Add a group stage.
	 * If the group stage already exists, it won't be added again.
	 *
	 * @param groupStage The group stage to add.
	 *
	 * @throws IllegalArgumentException If the group stage is null.
	 */
	public void addGroupStage(final GroupStage groupStage) throws IllegalArgumentException{
		this.groupStages.add(groupStage);
	}
	
	/**
	 * Add a collection group stage.
	 * If the group stage already exists, it won't be added again.
	 *
	 * @param groupStages The groups stages to add.
	 *
	 * @return True if all elements were added, false otherwise.
	 */
	public boolean addAllGroupStages(final Collection<GroupStage> groupStages){
		return this.groupStages.addAll(groupStages);
	}
	
	/**
	 * Tells if a gymnasium is full at a given date.
	 *
	 * @param gymnasium The gymnasium to check for.
	 * @param date      The date to check at.
	 *
	 * @return True if full, false otherwise.
	 */
	public boolean isGymnasiumFull(final Gymnasium gymnasium, final LocalDate date){
		int found = 0;
		for(GroupStage group : groupStages){
			for(Match match : group.getMatches()){
				if(Objects.equals(gymnasium, match.getGymnasium()) && Objects.equals(date, match.getDate())){
					found++;
				}
				if(found >= gymnasium.getCapacity()){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Get the group stages.
	 *
	 * @return The group stages.
	 */
	public Collection<GroupStage> getGroupStages(){
		return groupStages;
	}
}
