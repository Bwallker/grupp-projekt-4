package root.model;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import lombok.Getter;
import lombok.Setter;
import root.view.SoundClipListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * SoundClip is a class representing a digital
 * sound clip file on disk.
 */
public class SoundClip {
	@Getter @Setter
 	private SoundClipListView listView;
	private final File file;
	@Getter
	protected boolean flag;
	@Getter
	protected byte grade;
	public void setGrade (byte grade) {
		this.grade = grade;
		this.listView.update(this);
	}
	/**
	 * Make a SoundClip from a file.
	 * Requires file != null.
	 */
	public SoundClip(File file) {
		assert file != null;
		this.file = file;
		this.grade = 1;
		this.listView = null;
	}

	/**
	 * @return the file containing this sound clip.
	 */
	public File getFile() {
		return file;
	}
	
	public String toString(){
		return file.getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		return 
			obj instanceof SoundClip
			&& ((SoundClip)obj).file.equals(file);
	}
	
	@Override
	public int hashCode() {
		return file.hashCode();
	}

	public void flipFlag() {
		this.flag = !this.flag;
		this.listView.update(this);
	}

}
