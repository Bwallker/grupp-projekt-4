package root.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.Getter;
import lombok.Setter;
import root.model.Album;
import root.model.SoundClip;

public class SoundClipListView extends TableView<SoundClip> {
	@Getter @Setter
	private MusicOrganizerWindow view;
	public SoundClipListView() {
		super();
		var clipsAreFlagged = new TableColumn<SoundClip, Boolean>("Is flagged");
		var clipNames = new TableColumn<SoundClip, String>("Names");
		var clipGrades = new TableColumn<SoundClip, Number>("Grades");
		clipsAreFlagged.setCellValueFactory((clip) -> new ReadOnlyBooleanWrapper(clip.getValue().isFlag()));
		clipNames.setCellValueFactory((clip) -> new ReadOnlyStringWrapper(clip.getValue().toString()));
		clipGrades.setCellValueFactory((clip) -> new ReadOnlyIntegerWrapper(clip.getValue().getGrade()));
		this.getColumns().add(clipNames);
		this.getColumns().add(clipsAreFlagged);
		this.getColumns().add(clipGrades);
	}
	
	/**
	 * Displays the contents of the specified album
	 * @param album - the album which contents are to be displayed
	 */
	public void display(Album album){
		this.getItems().forEach((clip) -> clip.setListView(null));
		this.getItems().clear();

		
		// TODO: 
		// Add all SoundClips contained in the parameter album to 
		// the list of SoundClips 'clips' (the instance variable)
		var items = this.getItems();

		items.addAll(album.getSongs());

		this.setItems(items);
		this.getItems().forEach((clip) -> {
			clip.setListView(this);
			update(clip);
		});

	}
	/** Observer pattern: This method is called everytime on of our sound clips change */
	public void update(SoundClip clip) {
		if (clip.isFlag() && !view.flaggedSoundClips.containsSong(clip)) {
			view.flaggedSoundClips.addSong(clip);
		} else if (!clip.isFlag() && view.flaggedSoundClips.containsSong(clip)) {
			view.flaggedSoundClips.removeSong(clip);
		}

		if (clip.getGrade() > 3 && !view.greatSoundClips.containsSong(clip)) {
			view.greatSoundClips.addSong(clip);
		} else if (clip.getGrade() <= 3 && view.greatSoundClips.containsSong(clip)) {
			view.greatSoundClips.removeSong(clip);
		}
	}

	public List<SoundClip> getSelectedClips(){
		ObservableList<SoundClip> items = this.getSelectionModel().getSelectedItems();
		return new ArrayList<>(items);
	}
}
