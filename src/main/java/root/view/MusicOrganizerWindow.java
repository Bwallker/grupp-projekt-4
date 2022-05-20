package root.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.Getter;
import root.controller.MusicOrganizerController;
import root.model.Album;
import root.model.SoundClip;
import root.util.Util;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;



public class MusicOrganizerWindow extends Application {
	
	private BorderPane bord;
	private static MusicOrganizerController controller;
	@Getter
	private TreeItem<Album> rootNode;
	@Getter
	private TreeView<Album> tree;
	private ButtonPaneHBox buttons;
	@Getter
	private SoundClipListView soundClipTable;
	private TextArea messages;
	public Album greatSoundClips;
	public Album flaggedSoundClips;
	
	public static void main(String[] args) {
		controller = new MusicOrganizerController();
		if (args.length == 0) {
			controller.loadSoundClips("sample-sound");
		} else if (args.length == 1) {
			controller.loadSoundClips(args[0]);
		} else {
			System.err.println("too many command-line arguments");
			System.exit(0);
		}
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
				
		try {
			controller.registerView(this);
			primaryStage.setTitle("Music Organizer");
			
			bord = new BorderPane();
			
			// Create buttons in the top of the GUI
			buttons = new ButtonPaneHBox(controller, this);
			bord.setTop(buttons);

			// Create the tree in the left of the GUI
			tree = createTreeView();
			bord.setLeft(tree);

			setSelectedTreeItem(tree.getRoot());
			var greatSoundClips = new Album("Great Sound Clips");
			var flaggedSoundClips = new Album("Flagged Sound Clips");
			this.greatSoundClips = greatSoundClips;
			this.flaggedSoundClips = flaggedSoundClips;

			onAlbumAdded(getTree().getRoot(), greatSoundClips);
			onAlbumAdded(getTree().getRoot(), flaggedSoundClips);
			this.greatSoundClips.setFrozen(true);
			this.flaggedSoundClips.setFrozen(true);
			// Create the list in the right of the GUI
			soundClipTable = createSoundClipListView();
			bord.setCenter(soundClipTable);
						
			// Create the text area in the bottom of the GUI
			bord.setBottom(createBottomTextArea());
			
			Scene scene = new Scene(bord);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.sizeToScene();
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent arg0) {
					Platform.exit();
					System.exit(0);
					
				}
				
			});



			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private TreeView<Album> createTreeView(){
		rootNode = new TreeItem<>(controller.getRootAlbum());
		TreeView<Album> v = new TreeView<>(rootNode);

		v.setOnMouseClicked(e -> {
			if (e.getClickCount() == 2) {
				// This code gets invoked whenever the user double clicks in the TreeView
				// TODO: ADD YOUR CODE HERE
				soundClipTable.display(getSelectedAlbum());

			}

		});

		
		return v;
	}
	
	private SoundClipListView createSoundClipListView() {
		SoundClipListView v = new SoundClipListView();
		v.setView(this);
		v.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		v.display(controller.getRootAlbum());
		
		v.setOnMouseClicked(e -> {
			if (e.getClickCount() == 2) {
				// This code gets invoked whenever the user double clicks in the sound clip table
				// TODO: ADD YOUR CODE HERE
				controller.playSoundClips();
			}

		});
		
		return v;
	}
	
	private ScrollPane createBottomTextArea() {
		messages = new TextArea();
		messages.setPrefRowCount(3);
		messages.setWrapText(true);
		messages.prefWidthProperty().bind(bord.widthProperty());
		messages.setEditable(false); // don't allow user to edit this area
		
		// Wrap the TextArea in a ScrollPane, so that the user can scroll the 
		// text area up and down
		ScrollPane sp = new ScrollPane(messages);
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		return sp;
	}
	
	/**
	 * Displays the message in the text area at the bottom of the GUI
	 * @param message the message to display
	 */
	public void displayMessage(String message) {
		messages.appendText(message + "\n");
	}
	
	public Album getSelectedAlbum() {
		TreeItem<Album> selectedItem = getSelectedTreeItem();
		return selectedItem == null ? null : selectedItem.getValue();
	}
	
	public TreeItem<Album> getSelectedTreeItem(){
		return tree.getSelectionModel().getSelectedItem();
	}

	public void setSelectedTreeItem(Album albumToBeMadeSelected) {
		var node = Util.findNode(this.tree, albumToBeMadeSelected);
		if (node.isEmpty()) {
			throw new NullPointerException("Album could not be found in tree in setSelectedTreeItem");
		}
		var row = tree.getRow(node.get());
		tree.getSelectionModel().select(row);

	}

	public void setSelectedTreeItem(TreeItem<Album> albumToBeMadeSelected) {
		setSelectedTreeItem(albumToBeMadeSelected.getValue());

	}
	
	
	
	/**
	 * Pop up a dialog box prompting the user for a name for a new album.
	 * Returns the name, or null if the user pressed Cancel
	 */
	public String promptForAlbumName() {
		TextInputDialog dialog = new TextInputDialog();
		
		dialog.setTitle("Enter album name");
		dialog.setHeaderText(null);
		dialog.setContentText("Please enter the name for the album");
		Optional<String> result = dialog.showAndWait();
		return result.orElse(null);
	}

	public byte promptForSoundClipGrade(SoundClip clip) {
		TextInputDialog dialog = new TextInputDialog();

		dialog.setTitle("What grade would you like to give to sound clip: \"" + clip.toString() + "\"?");
		dialog.setHeaderText(null);
		dialog.setContentText("Please enter the grade: It must be an integer between 0 and 5");
		Optional<String> result = dialog.showAndWait();
		if (result.isEmpty()) {
			return promptForSoundClipGrade(clip);
		}
		var unwrapped = result.get();
		byte val;
		try {
			val = Byte.parseByte(unwrapped.trim());
		} catch (NumberFormatException e) {
			return promptForSoundClipGrade(clip);
		}
		if (val < 0 || val > 5) {
			var popup = new TextArea("Your grade was less than 0 or more than 5");
			popup.setVisible(true);
			return promptForSoundClipGrade(clip);
		}

		return val;
	}
	
	/**
	 * Return all the sound clips currently selected in the clip table.
	 */
	public List<SoundClip> getSelectedSoundClips(){
		return soundClipTable.getSelectedClips();
	}
	
	
	
	/**
	 * *****************************************************************
	 * Methods to be called in response to events in the Music Organizer
	 * *****************************************************************
	 */	
	
	
	
	/**
	 * Updates the album hierarchy with a new album
	 * @param newAlbum
	 */
	public void onAlbumAdded(TreeItem<Album> parentItem, Album newAlbum) {
		TreeItem<Album> newItem = new TreeItem<>(newAlbum);
		parentItem.getChildren().add(newItem);
		parentItem.setExpanded(true); // automatically expand the parent node in the tree
	}
	public void onAlbumAdded(Album newAlbum){
		onAlbumAdded(getSelectedTreeItem(), newAlbum);
	}
	
	/**
	 * Updates the album hierarchy by removing an album from it
	 */
	public void onAlbumRemoved(){
		onAlbumRemoved(getSelectedTreeItem());
		
	}

	public void onAlbumRemoved(TreeItem<Album> toRemove) {
		TreeItem<Album> parent = toRemove.getParent();
		parent.getChildren().remove(toRemove);
	}
	
	/**
	 * Refreshes the clipTable in response to the event that clips have
	 * been modified on an album
	 */
	public void onClipsUpdated(){
		Album a = getSelectedAlbum();
		soundClipTable.display(a);
	}
	
}
