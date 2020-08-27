package basic.future;

public class ClientThread extends Thread {

	public ClientThread(String name)
	{
		super(name);
	}

	public void run() {
		System.out.println(getName() + "BEGIN");
		for (int i = 0; i < 10; i++)
		{
			Log.println("i = " + i);
			try {
				Thread.sleep(100);
			}catch (InterruptedException e)
			{}
		}

		Log.close();
		System.out.println(getName() + " END");
	}

	public static void main(String[] args) {
		new ClientThread("Alice").start();
		new ClientThread("Bobby").start();
		new ClientThread("Chris").start();
	}

}
