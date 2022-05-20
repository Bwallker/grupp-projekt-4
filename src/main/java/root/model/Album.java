package root.model;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Album {
    protected String name;
    @Getter @Setter
    protected Album parent;
    @Getter
    protected List<Album> subAlbums;
    @Getter
    protected List<SoundClip> songs;
    @Getter @Setter
    protected boolean isFrozen;

    public Album(@NotNull String name) {
        this(name, null);
    }

    public Album(@NotNull String name, Album parent) {
        assert name != null;
        this.parent = parent;
        this.name = name;
        this.subAlbums = new ArrayList<>();
        this.songs = new ArrayList<>();
        this.isFrozen = false;
    }
    /**
     * Adds an album to this albums sub albums
     * @param album The album to add
     * @throws AssertionError Throws an AssertionsError if the passed in album is null */

    public void addAlbum(@NotNull Album album) {
        assert album != null;
        album.parent = this;
        this.subAlbums.add(album);
    }

    /**
     * Removes an album from this albums sub albums
     * @param album The album to remove
     * @throws AssertionError Throws an AssertionsError if the passed in album is null*/

    public void removeAlbum(@NotNull Album album) {
        assert album != null;
        album.parent = null;
        this.subAlbums.remove(album);
    }
    /**
     * @param album The album that this album might contain
     * @return Returns true if the album is part of this albums sub albums, otherwise false
     * @throws AssertionError Throws an AssertionsError if the passed in album is null*/
    public boolean containsAlbum(@NotNull Album album) {

        assert album != null;
        return this.subAlbums.contains(album);
    }

    /**
     * Adds a song to this albums songs
     * Also adds this song to its parents if it does not have it.
     * @param song The song to add
     * @throws AssertionError Throws an AssertionsError if the passed in song is null*/

    public void addSong(@NotNull SoundClip song) {
        assert song != null;
        if (this.parent != null && !parent.containsSong(song)) {
            this.parent.addSong(song);
        }
        this.songs.add(song);
    }
    /**
     * Removes a song from this albums songs
     * Also recursively removes the song from all subalbums
     * @param song The song to remove
     * @throws AssertionError Throws an AssertionsError if the passed in song is*/

    public void removeSong(@NotNull SoundClip song) {
        assert song != null;
        this.songs.remove(song);
        this.subAlbums.forEach((Album a) -> a.removeSong(song));
    }
    /**
     * @param song The song that this album might contain
     * @return Returns true if the song is part of this albums songs, otherwise false
     * @throws AssertionError Throws an AssertionsError if the passed in song is null*/
    public boolean containsSong(@NotNull SoundClip song) {
        assert song != null;
        return this.songs.contains(song);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Album album = (Album) o;
        return name.equals(album.name) && Objects.equals(parent, album.parent) && subAlbums.equals(album.subAlbums) && songs.equals(album.songs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parent, subAlbums, songs);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
