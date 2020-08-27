package reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TestServer implements Runnable {


	final Selector selector;

	final ServerSocketChannel serverSocketChannel;


	public TestServer() throws IOException {
		this.selector = Selector.open();
		this.serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress(10231));
		serverSocketChannel.configureBlocking(false);
		SelectionKey sk = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		sk.attach(new Acceptor());

	}


	public void run() {

		try {
			while (!Thread.interrupted()) {
				selector.select();

				Set selected = selector.selectedKeys();
				Iterator it = selected.iterator();

				while (it.hasNext()) {
					//Reactor负责dispatch收到的事件
					dispatch((SelectionKey) (it.next()));
				}
				selected.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	void dispatch(SelectionKey sk) {
		Runnable r = (Runnable) sk.attachment();
		if (r != null) {
			r.run();
		}
	}


	class Acceptor implements Runnable {
		public void run() {
			try {
				SocketChannel connection = serverSocketChannel.accept(); //主selector负责accept
				if (connection != null) {
					new Handler(selector, connection); //选个subReactor去负责接收到的connection
				}
			} catch (IOException e) {
				e.printStackTrace();
			}


		}
	}

	class Handler implements Runnable {
		final SocketChannel channel;
		final SelectionKey sk;
		ByteBuffer input = ByteBuffer.allocate(1024);
		ByteBuffer output = ByteBuffer.allocate(1024);


		static final int READING = 0, SENDING = 1;
		int state = READING;
		private ByteBuffer out;

		Handler(Selector selector, SocketChannel c) throws IOException {
			channel = c;
			c.configureBlocking(false);

			//Optionally try first read now
			sk = channel.register(selector, 0);

			//将Handler作为callback对象
			sk.attach(this);

			//第二步，注册Read就绪事件
			sk.interestOps(SelectionKey.OP_READ);
			selector.wakeup();
		}

		boolean inputIsComplete() {
			/**
			 *
			 */
			return true;
		}

		boolean outputIsComplete() {
			/**
			 *
			 */
			return true;
		}

		void process() {
			/**
			 *
			 */
			return;
		}


		public void run() {
			try {
				if (state == READING) {
					read();
				} else if (state == SENDING) {
					send();
				}
			} catch (IOException ex) {
				/**
				 *
				 */
			}
		}

		void read() throws IOException {
			//14、读取数据
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

			int length = 0;

			while ((length = channel.read(byteBuffer)) > 0) {
				byteBuffer.flip();
				String message = new String(byteBuffer.array(), 0, length);
				System.out.println(message);
				byteBuffer.clear();
			}


			if (inputIsComplete()) {
				process();

				state = SENDING;

				//Normally also do first write now
				//第三步,接收write就绪事件
				sk.interestOps(SelectionKey.OP_WRITE);
			}
		}

		void send() throws IOException {
			String message = "server received!";
			output.put(message.getBytes());
			output.flip();
			channel.write(output);
			if (outputIsComplete()) {
				sk.cancel();
			}
		}
	}


	public static void main(String[] args) throws IOException {
		Thread thread = new Thread(new TestServer());
		thread.start();
	}

}
