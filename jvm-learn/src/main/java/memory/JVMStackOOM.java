package memory;

public class JVMStackOOM {
	private void dontStop(){
		while (true){}
	}

	public void stackLeakByThread() {
		while (true){
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					dontStop();
				}
			});
			thread.start();
		}
	}

	public static void main(String[] args) {
		JVMStackOOM oom = new JVMStackOOM();
		oom.stackLeakByThread();
	}

}
