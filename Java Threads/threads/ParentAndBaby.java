package threads;

class BabyThread implements Runnable {
	public BabyThread() {
		System.out.println(Thread.currentThread().getName() + ": Baby is born");
	}

	public void run() {
		try {
			Thread.sleep(1000);
			System.out.println(Thread.currentThread().getName() + //
					": Baby woke up from its nap");
			Thread.sleep(1000);
			System.out.println(Thread.currentThread().getName() + //
					": Baby woke from another nap");
			Thread.sleep(1000);
			System.out.println(Thread.currentThread().getName() + //
					": Baby is all done");
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + //
					": BABY IS NOT HAPPY!");
		}
	}
}

public class ParentAndBaby {
	public ParentAndBaby() throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + //
				": Parent is born");
	}

	public void manageBaby() throws InterruptedException {
		Thread baby = new Thread(new BabyThread());
		System.out.println(Thread.currentThread().getName() + //
				": Parent created the baby");
		baby.start();
		Thread.sleep(3000);
		if (baby.isAlive()) {
			baby.interrupt();
			baby.join();
		}
		System.out.println("Parent is all done");
	}

	public static void main(String args[]) throws InterruptedException {
		ParentAndBaby parent = new ParentAndBaby();
		parent.manageBaby();
	}
}