package test;


import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * 文件源描述。
 */
public interface FileSource {

	/**
	 * 返回文件名称
	 * @return
	 */
	String getName();

	/**
	 * 返回文件后辍
	 * @return
	 */
	String getExt();

	/**
	 * 返回文件大小
	 * @return
	 */
	long getSize();

	/**
	 * 返回文件流
	 * @return
	 * @throws IOException
	 */
	InputStream getInputStream() throws IOException;

	/**
	 * 转存为文件
	 * @param dest
	 * @throws IOException
	 */
	void transferTo(File dest) throws IOException;

	/**
	 * 读取文件ID。
	 * 说明：该方法的代价是完整读取一次文件，使用需谨慎。
	 * @return

	 */
	default String readFileId() {
		String id = null;
		try (InputStream input = this.getInputStream()) {
			MessageDigest digest = DigestUtils.getMd5Digest();
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = input.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			String md5 = Hex.encodeHexString(digest.digest());
			String size = Long.toHexString(this.getSize());
			String ext = this.getExt();
			id = md5 + ext + size;
		} catch (IOException e) {
			System.out.println(e);
		}
		return id;
	}
}
