package fr.mrcraftcod.scheduler.model;

import fr.mrcraftcod.scheduler.utils.GymnasiumColor;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a gymnasium.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-21.
 *
 * @author Thomas Couchoud
 * @since 2019-01-21
 */
public class Gymnasium implements Comparable<Gymnasium>{
	private final String name;
	private final String city;
	private final SimpleIntegerProperty capacity;
	private final ObservableList<LocalDate> bannedDates;
	private final GymnasiumColor color;
	
	/**
	 * Constructor.
	 *
	 * @param name     The name.
	 * @param city     The city.
	 * @param capacity The capacity.
	 * @param color    The color of the gymnasium.
	 *
	 * @throws IllegalArgumentException If the name is empty, or the city is empty or the capacity isn't positive.
	 */
	public Gymnasium(final String name, final String city, final int capacity, final GymnasiumColor color) throws IllegalArgumentException{
		if(name == null || name.isBlank()){
			throw new IllegalArgumentException("Gymnasium name is empty");
		}
		if(city == null || city.isBlank()){
			throw new IllegalArgumentException("Gymnasium city is empty");
		}
		if(capacity <= 0){
			throw new IllegalArgumentException("Gymnasium capacity is invalid");
		}
		this.name = name;
		this.city = city;
		this.capacity = new SimpleIntegerProperty(capacity);
		this.color = color;
		this.bannedDates = FXCollections.observableArrayList();
	}
	
	@Override
	public boolean equals(final Object obj){
		return obj instanceof Gymnasium && Objects.equals(((Gymnasium) obj).getCity(), this.getCity()) && Objects.equals(((Gymnasium) obj).getName(), this.getName());
	}
	
	/**
	 * Add a banned date.
	 *
	 * @param date The date to ban.
	 */
	public void addBannedDate(final LocalDate date){
		this.bannedDates.add(date);
	}
	
	/**
	 * Tell if a date is banned.
	 *
	 * @param date The date to test.
	 *
	 * @return True if banned, false otherwise.
	 */
	public boolean isDateBanned(final LocalDate date){
		return this.bannedDates.contains(date);
	}
	
	@Override
	public int compareTo(@NotNull final Gymnasium o){
		return getName().compareTo(o.getName());
	}
	
	/**
	 * Set the capacity of the gymnasium.
	 *
	 * @param capacity The capacity to set.
	 */
	public void setCapacity(final int capacity){
		this.capacityProperty().set(capacity);
	}
	
	/**
	 * Get the color representing this gymnasium.
	 *
	 * @return The color of the gymnasium.
	 */
	public GymnasiumColor getColor(){
		return this.color;
	}
	
	/**
	 * Get the banned dates.
	 *
	 * @return The banned dates.
	 */
	public ObservableList<LocalDate> getBannedDates(){
		return bannedDates;
	}
	
	/**
	 * Get the city.
	 *
	 * @return The city.
	 */
	public String getCity(){
		return this.city;
	}
	
	/**
	 * Get the name.
	 *
	 * @return The name.
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Get the capacity.
	 *
	 * @return The capacity.
	 */
	public int getCapacity(){
		return this.capacityProperty().get();
	}
	
	/**
	 * Get the capacity property.
	 *
	 * @return The capacity property.
	 */
	public SimpleIntegerProperty capacityProperty(){
		return this.capacity;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
