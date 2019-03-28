package test;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 本地磁盘文件描述。
 */
public class FileSystemSource extends AbstractFileSource {

	private File file;

	public FileSystemSource(String path) {
		this(new File(path));
	}

	public FileSystemSource(File file) {
		super(file.getName());
		this.file = file;
	}

	public FileSystemSource(File file, String fullName) {
		super(fullName);
		this.file = file;
	}

	public FileSystemSource(File file, String name, String ext) {
		super(name, ext);
		this.file = file;
	}

	@Override
	public long getSize() {
		return FileUtils.sizeOf(file);
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(file);
	}

	@Override
	public void transferTo(File dest) throws IOException {
		FileUtils.copyFile(this.file, dest);
	}
}
