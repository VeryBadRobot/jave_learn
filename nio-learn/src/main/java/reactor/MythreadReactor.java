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

public class MythreadReactor implements Runnable {
	//subReactor集合，一个selector代表一个subReactor
	Selector[] selectors = new Selector[2];
	int next = 0;
	final ServerSocketChannel serverSocket;

	MythreadReactor(int port)throws IOException
	{
		//Reactor初始化
		selectors[0] = Selector.open();
		selectors[1] = Selector.open();
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(port));

		//非阻塞
		serverSocket.configureBlocking(false);

		//分布处理，第一步，接收accept事件
		SelectionKey sk = serverSocket.register(selectors[0],SelectionKey.OP_ACCEPT);
		//attach callback object, Acceptor
		sk.attach(new Acceptor());
	}

	public void run()
	{
		try{
			while (!Thread.interrupted())
			{
				for(int i = 0; i< 2; i++)
				{
					selectors[i].select();
					Set selected = selectors[i].selectedKeys();
					Iterator it = selected.iterator();

					while (it.hasNext())
					{
						//Reactor负责dispatch收到的事件
						dispatch((SelectionKey)(it.next()));
					}
					selected.clear();
				}
			}
		}catch (IOException ex)
		{
			/**
			 *
			 */
		}
	}

	void dispatch(SelectionKey key)
	{
		Runnable r = (Runnable)(key.attachment());
		//调用之前注册的callback对象
		if (r!=null)
		{
			r.run();
		}
	}

	class Acceptor{
		public synchronized void run() throws IOException{
			SocketChannel connection = serverSocket.accept(); //主selector负责accept
			if (connection != null)
			{
				new Handler(selectors[next], connection); //选个subReactor去负责接收到的connection
			}
			if (++next == selectors.length) next = 0;

		}
	}

	class Handler implements Runnable {
		final SocketChannel channel;
		final SelectionKey sk;
		ByteBuffer input = ByteBuffer.allocate(1024);
		ByteBuffer output = ByteBuffer.allocate(1024);

		static final int READING = 0, SENDING = 1;
		int state = READING;

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
			return false;
		}

		boolean outputIsComplete() {
			/**
			 *
			 */
			return false;
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
			channel.read(input);

			if (inputIsComplete()) {
				process();

				state = SENDING;
				//Normally also do first write now

				//第三步,接收write就绪事件
				sk.interestOps(SelectionKey.OP_WRITE);
			}
		}

		void send() throws IOException {
			channel.write(output);
			if (outputIsComplete()) {
				sk.cancel();
			}
		}
	}


}
