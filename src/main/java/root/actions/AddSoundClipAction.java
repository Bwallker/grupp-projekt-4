package root.actions;

import root.model.Album;
import root.model.SoundClip;
import java.util.List;
public class AddSoundClipAction extends Action {
    protected Album albumAddedTo;
    protected List<SoundClip> addedSoundClips;
    protected root.view.MusicOrganizerWindow view;
    public AddSoundClipAction (root.view.MusicOrganizerWindow view, Album albumAddedTo, List<SoundClip> addedSoundClips) {
        this.albumAddedTo = albumAddedTo;
        this.addedSoundClips = addedSoundClips;
        this.view = view;
    }

    @Override
    public void redo() {
        addedSoundClips.forEach(albumAddedTo::addSong);
        view.onClipsUpdated();
    }

    @Override
    public void undo() {
        addedSoundClips.forEach(albumAddedTo::removeSong);
        view.onClipsUpdated();

    }
}
