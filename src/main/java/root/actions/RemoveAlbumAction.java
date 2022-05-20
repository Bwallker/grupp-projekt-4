package root.actions;

import root.model.Album;
import root.util.Util;

public class RemoveAlbumAction extends Action {
    protected Album albumRemovedFrom;
    protected Album removedAlbum;
    protected root.view.MusicOrganizerWindow view;
    public RemoveAlbumAction (root.view.MusicOrganizerWindow view, Album albumRemovedFrom, Album removedAlbum) {
        this.albumRemovedFrom = albumRemovedFrom;
        this.removedAlbum = removedAlbum;
        this.view = view;
    }
    @Override
    public void redo() {
        final var parent = removedAlbum.getParent();
        albumRemovedFrom.removeAlbum(removedAlbum);
        final var removedAlbumNode = Util.findNode(view.getTree(), this.removedAlbum);
        view.onAlbumRemoved(removedAlbumNode.orElseThrow());
        removedAlbum.setParent(parent);

    }

    @Override
    public void undo() {
        removedAlbum.addAlbum(removedAlbum);
        removedAlbum.setParent(albumRemovedFrom);
        view.onAlbumAdded(Util.findNode(view.getTree(), this.albumRemovedFrom).orElseThrow(), removedAlbum);
    }
}
