package root.actions;

import root.model.Album;
import root.util.Util;
import root.view.MusicOrganizerWindow;
public class AddAlbumAction extends Action {
    protected Album albumAddedTo;
    protected Album addedAlbum;
    protected MusicOrganizerWindow view;
    public AddAlbumAction (MusicOrganizerWindow view, Album albumAddedTo, Album addedAlbum) {
        this.albumAddedTo = albumAddedTo;
        this.addedAlbum = addedAlbum;
        this.view = view;
    }
    @Override
    public void redo() {
        albumAddedTo.addAlbum(addedAlbum);
        addedAlbum.setParent(albumAddedTo);
        view.onAlbumAdded(Util.findNode(view.getTree(), this.albumAddedTo).orElseThrow(), addedAlbum);

    }

    @Override
    public void undo() {
        final var parent = addedAlbum.getParent();
        albumAddedTo.removeAlbum(addedAlbum);
        final var addedAlbumNode = Util.findNode(view.getTree(), this.addedAlbum);
        view.onAlbumRemoved(addedAlbumNode.orElseThrow());
        addedAlbum.setParent(parent);

    }
}
