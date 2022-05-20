package root.actions;

import root.model.Album;
import root.model.SoundClip;
import java.util.List;
public class RemoveSoundClipAction extends Action {
    protected Album albumRemovedFrom;
    protected List<SoundClip> removedSoundClips;
    protected root.view.MusicOrganizerWindow view;
    public RemoveSoundClipAction (root.view.MusicOrganizerWindow view, Album albumRemovedFrom, List<SoundClip> removedSoundClips) {
        this.albumRemovedFrom = albumRemovedFrom;
        this.removedSoundClips = removedSoundClips;
        this.view = view;
    }

    @Override
    public void redo() {
        removedSoundClips.forEach(albumRemovedFrom::removeSong);
        view.onClipsUpdated();
    }

    @Override
    public void undo() {
        removedSoundClips.forEach(albumRemovedFrom::addSong);
        view.onClipsUpdated();
    }

    @Override
    public String toString() {
        return "RemoveSoundClipAction{" +
                "albumRemovedFrom=" + albumRemovedFrom +
                ", removedSoundClips=" + removedSoundClips +
                ", view=" + view +
                '}';
    }
}
