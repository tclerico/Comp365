package threads;

class Baby implements Runnable {
	int iterations;

	public Baby(int iterations) {
		this.iterations = iterations;
	}

	public void run() {
		try {
			while (iterations > 0) {
				System.out.println(Thread.currentThread().getName() + ": " + iterations);
				Thread.sleep(1000);
				iterations--;
			}
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + ": ABORTED");
		}
	}
}

public class ParentAndBabies {

	public final static int durations[] = { 10, 12, 14 };

	public static void main(String args[]) throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + ": Starting new threads...");
		for ( int i = 0; i < durations.length; i++ ) {
			Thread t = new Thread( new Baby( durations[ i ] ));
			t.start();
		}
		System.out.println(Thread.currentThread().getName() + ": Started all threads.");
	}
}