package netty;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class Test {


	public static void main(String[] args) throws InterruptedException {
		ByteBuffer buffer = ByteBuffer.allocateDirect(Integer.MAX_VALUE);
		ByteBuffer buff2 = ByteBuffer.allocateDirect(Integer.MAX_VALUE);
//		ByteBuffer buffer3 = ByteBuffer.allocateDirect(Integer.MAX_VALUE);

		TimeUnit.SECONDS.sleep(10L);
	}
}
