package root.controller;

import java.util.List;
import java.util.Set;

import root.model.Album;
import root.model.SoundClip;
import root.model.SoundClipBlockingQueue;
import root.model.SoundClipLoader;
import root.model.SoundClipPlayer;
import root.view.MusicOrganizerWindow;
import root.actions.Action;
import root.actions.AddAlbumAction;
import root.actions.RemoveAlbumAction;
import root.actions.AddSoundClipAction;
import root.actions.RemoveSoundClipAction;

import java.util.ArrayList;
public class MusicOrganizerController {

	private MusicOrganizerWindow view;
	private final SoundClipBlockingQueue queue;
	private final Album root;
	private final ArrayList<Action> performedActions;
	private final ArrayList<Action> undoneActions;
	public MusicOrganizerController() {

		// TODO: Create the root album for all sound clips
		root = new Album("All Sound Clips");

		// Create the blocking queue
		queue = new SoundClipBlockingQueue();
				
		// Create a separate thread for the sound clip player and start it
		this.performedActions = new ArrayList<>();
		this.undoneActions = new ArrayList<>();

		(new Thread(new SoundClipPlayer(queue))).start();
	}
	
	/**
	 * Load the sound clips found in all subfolders of a path on disk. If path is not
	 * an actual folder on disk, has no effect.
	 */
	public void loadSoundClips(String path) {
		Set<SoundClip> clips = SoundClipLoader.loadSoundClips(path);

		// TODO: Add the loaded sound clips to the root album
		clips.forEach(root::addSong);
	}
	
	public void registerView(MusicOrganizerWindow view) {
		this.view = view;
	}
	
	/**
	 * Returns the root album
	 */
	public Album getRootAlbum(){
		return root;
	}
	
	/**
	 * Adds an album to the Music Organizer
	 */
	public void addNewAlbum(){ //TODO Update parameters if needed - e.g. you might want to give the currently selected album as parameter
		// TODO: Add your code here
		if (view.getSelectedAlbum().isFrozen()) return;
		final var newAlbumName = view.promptForAlbumName();
		final var newAlbum = new Album(newAlbumName, view.getSelectedAlbum());
		view.getSelectedAlbum().addAlbum(newAlbum);
		view.onAlbumAdded(newAlbum);
		final var action = new AddAlbumAction(view, view.getSelectedAlbum(), newAlbum);
		this.performedActions.add(action);
		System.out.println(performedActions);

	}
	
	/**
	 * Removes an album from the Music Organizer
	 */
	public void deleteAlbum(){ //TODO Update parameters if needed
		// TODO: Add your code here
		if (view.getSelectedAlbum().isFrozen()) return;

		final var removedAlbum = view.getSelectedAlbum();
		view.getSelectedAlbum().getParent().removeAlbum(view.getSelectedAlbum());
		view.getSelectedAlbum().setParent(null);
		view.onAlbumRemoved();
		final var action = new RemoveAlbumAction(view, view.getSelectedAlbum(), removedAlbum);
		this.performedActions.add(action);
		System.out.println(performedActions);
	}
	
	/**
	 * Adds sound clips to an album
	 */
	public void addSoundClips(){ //TODO Update parameters if needed
		// TODO: Add your code
		if (view.getSelectedAlbum().isFrozen()) return;

		final var action = new AddSoundClipAction(view, view.getSelectedAlbum(), new ArrayList<>(view.getSelectedSoundClips()));
		view.getSelectedSoundClips().forEach(view.getSelectedAlbum()::addSong);
		view.onClipsUpdated();
		this.performedActions.add(action);
		System.out.println(performedActions);

	}
	
	/**
	 * Removes sound clips from an album
	 */
	public void removeSoundClips(){ //TODO Update parameters if needed
		// TODO: Add your code here
		if (view.getSelectedAlbum().isFrozen()) return;

		final var action = new RemoveSoundClipAction(view, view.getSelectedAlbum(), new ArrayList<>(view.getSelectedSoundClips()));
		view.getSelectedSoundClips().forEach(view.getSelectedAlbum()::removeSong);
		view.onClipsUpdated();
		this.performedActions.add(action);
		System.out.println(performedActions);

	}
	
	/**
	 * Puts the selected sound clips on the queue and lets
	 * the sound clip player thread play them. Essentially, when
	 * this method is called, the selected sound clips in the 
	 * SoundClipTable are played.
	 */
	public void playSoundClips(){
		List<SoundClip> l = view.getSelectedSoundClips();
		queue.enqueue(l);
		for (SoundClip soundClip : l) {
			view.displayMessage("Playing " + soundClip);
		}
	}

	public void undo() {
		if (performedActions.isEmpty()) return;
		final var lastAction =  performedActions.remove(performedActions.size() - 1);
		lastAction.undo();
		undoneActions.add(lastAction);
	}

	public void redo() {
		if (undoneActions.isEmpty()) return;
		final var lastUndoneAction =  undoneActions.remove(undoneActions.size() - 1);
		lastUndoneAction.redo();
		performedActions.add(lastUndoneAction);

	}

	public void flag() {
		this.view.getSelectedSoundClips().forEach(SoundClip::flipFlag);
		view.onClipsUpdated();
	}

	public void grade() {
		for (var clip : view.getSelectedSoundClips()) {
			var grade = view.promptForSoundClipGrade(clip);
			clip.setGrade(grade);
		}
		view.onClipsUpdated();
	}
}
