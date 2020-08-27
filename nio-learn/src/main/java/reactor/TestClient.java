package reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TestClient {


	public static void main(String[] args) throws IOException {
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress(10231));
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		String message = "from client!";

		buffer.put(message.getBytes());
		buffer.flip();

		socketChannel.write(buffer);

		buffer.clear();

		socketChannel.read(buffer);

		buffer.flip();
		byte[] bytes= new byte[1024];

		System.out.println(buffer.get(bytes).toString());
	}

}
