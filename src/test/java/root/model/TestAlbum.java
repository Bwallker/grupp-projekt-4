package root.model;

import org.junit.Test;
import root.model.Album;
import root.model.SoundClip;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TestAlbum {
    public SoundClip createTestSong() {
        try {
            final var file = File.createTempFile("Who cares", "Whatever");
            file.deleteOnExit();
            return new SoundClip(file);
        } catch (IOException e) {
            throw new NullPointerException("Failed at creating test song because of problems with creating file. Aborting test");
        }
    }

    @Test
    public void testConstructorIsCorrect() {
        final var album = new Album("Whatever");
        assert album.name.equals("Whatever");
        assert album.subAlbums.equals(new ArrayList<>());
        assert album.songs.equals(new ArrayList<>());
    }


    @Test
    public void testAddAlbum() {
        final var album = new Album("Whatever");
        final var subAlbum = new Album("I am child");
        album.addAlbum(subAlbum);
        assert subAlbum.parent.equals(album);
        assert album.containsAlbum(subAlbum);
    }

    @Test
    public void testRemoveAlbumThrows() throws Exception {
        final var album = new Album("Whatever");
        try {
            album.removeAlbum(null);
            throw new Exception("testRemoveAlbumThrows testcase failed because AssertionError was not thrown on bad method call");

        } catch (AssertionError ignored) {}

    }

    @Test
    public void testRemoveAlbum() {
        final var album = new Album("Whatever");
        final var subAlbum = new Album("I am child");
        album.addAlbum(subAlbum);
        assert album.containsAlbum(subAlbum);
        album.removeAlbum(subAlbum);
        assert !album.containsAlbum(subAlbum);
    }

    @Test
    public void testAddSong() {
        final var album = new Album("Whatever");
        final var song = createTestSong();
        album.addSong(song);
        assert album.containsSong(song);
    }


    @Test
    public void testRemoveSongThrows() throws Exception {
        final var album = new Album("Whatever");
        try {
            album.removeSong(null);
            throw new Exception("testRemoveSongThrows testcase failed because AssertionError was not thrown on bad method call");
        } catch (AssertionError ignored) {}
    }

    @Test
    public void testRemoveSong() {
        final var album = new Album("Whatever");
        final SoundClip song = createTestSong();
        album.addSong(song);
        assert album.containsSong(song);
        album.removeSong(song);
        assert !album.containsSong(song);
    }

}
