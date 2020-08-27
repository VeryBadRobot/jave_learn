package fastdfs;

import net.sf.ehcache.util.TimeUtil;
import org.joda.time.DateTime;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class FastdfsServer {


	public static void main(String[] args) throws IOException {
		System.out.println(getMinute(new DateTime()));
	}


	public static long getMinute(DateTime dateTime) {
		long year = dateTime.getYear();
		long month = dateTime.getMonthOfYear();
		long day = dateTime.getDayOfMonth();
		long hour = dateTime.getHourOfDay();
		long minute = dateTime.getMinuteOfHour();

		long result = (year * 100000000L) + (month * 1000000L) + (day * 10000L) + (hour * 100L) + minute;
		return result;
	}


	public static class Reactor implements Runnable {
		final Selector selector;
		final ServerSocketChannel serverSocket;

		Reactor(int port) throws IOException {
			//Reactor初始化
			selector = Selector.open();
			serverSocket = ServerSocketChannel.open();
			serverSocket.socket().bind(new InetSocketAddress(port));

			//设置非阻塞
			serverSocket.configureBlocking(false);

			//分步处理，第一步，接收accept事件
			SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);

			//attach callable object, Acceptor
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

			}
		}

		void dispatch(SelectionKey k) {
			Runnable r = (Runnable) k.attachment();

			//调用之前注册的callback对象
			if (r != null) {
				r.run();
			}
		}


		class Acceptor implements Runnable {

			public void run() {
				try {
					SocketChannel channel = serverSocket.accept();
					if (channel != null) {
						new Handler(selector, channel);
					}
				} catch (IOException e) {
				}
			}

		}
	}


	static class Handler implements Runnable {
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
