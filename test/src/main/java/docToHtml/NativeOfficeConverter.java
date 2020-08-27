package docToHtml;

import org.apache.commons.io.IOUtils;
import org.aspectj.apache.bcel.classfile.ClassFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author shaowenxing@cnstrong.cn
 * @since 14:59
 * 借助DLL将文档转换为需要的格式
 */
@Configuration
public class NativeOfficeConverter {
    static {
        String s = NativeOfficeConverter.class.getClassLoader().getResource("DocTOHtml.dll").getPath();
        System.load(s);
    }

    private Logger logger = LoggerFactory.getLogger(NativeOfficeConverter.class);
    /**
     * 上传存储目录文件的目录
     */
    @Value("${libreOffice.workingDir}")
    private String workingDir;

    @PostConstruct
    public void initFiles() {
        try {
            int i = init();
            if (i != 0) {
                throw new ClassFormatException("init error exit");
            }
            logger.info("office init success");
            // 生成文件目录
            Path p = Paths.get(workingDir);
            if (!p.toFile().exists())
                Files.createDirectories(Paths.get(workingDir));
        } catch (IOException e) {
            logger.error("create directory error:", e);
            System.exit(0);
        }
    }

    /**
     * 递归删除文件（夹）
     *
     * @param file 待删除的文件（夹）
     * @return
     */

    public boolean remove(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return delete(file);
        }
        if (file.listFiles() != null) {
            Arrays.asList(file.listFiles()).forEach(this::remove);
        }
        return delete(file);
    }

    public boolean delete(File f) {
        try {
            Files.delete(f.toPath());
        } catch (IOException e) {
            logger.error("delete file error", e);
            return false;
        }
        return true;
    }

    /**
     * 将
     *
     * @param path 需要转换的文件路径
     * @return 0 成功。 其他失败
     * -1 找不到office服务
     * -2 文件找不到或者文件异常
     */
    public native int docToHtmlConvert(String path);

    /**
     * 初始化解析进程文件
     *
     * @return
     */
    public native int init();

    /**
     * 将文件写入到磁盘上
     *
     * @param inputStream 输入
     * @param path        写入的路径
     * @return
     */
    public String inputSteamToFile(InputStream inputStream, String path) {
        String fileName = UUID.randomUUID().toString();
        Path path1 = Paths.get(path, fileName, fileName + ".doc");
        try {
            Files.createDirectories(Paths.get(path, fileName));
        } catch (IOException e) {
            logger.error("file error", e);
            return null;
        }
        try (FileOutputStream fos = new FileOutputStream(path1.toFile())) {
            if (inputStream.available() > 1024 * 1024 * 4) {
                // 文件大的换一种方式写入
                writeNIO(fos, inputStream);
            } else {
                IOUtils.copy(inputStream, fos);
            }
        } catch (IOException e) {
            logger.error("file error", e);
        }
        IOUtils.closeQuietly(inputStream);
        return fileName;
    }

    /**
     * NIO进行写入
     *
     * @throws IOException
     */
    private void writeNIO(FileOutputStream fos, InputStream inputStream) throws IOException {

        FileChannel channel = fos.getChannel();
        // 声明长度
        byte[] chars = new byte[1024 * 4];
        ByteBuffer buf = ByteBuffer.allocate(1024 * 4);
        while (-1 != inputStream.read(chars)) {
            // 写入文件
            buf.put(chars);
            buf.flip();
            channel.write(buf);
            buf.clear();
        }
        // 关闭流
        IOUtils.closeQuietly(channel);
        IOUtils.closeQuietly(fos);
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }


}
