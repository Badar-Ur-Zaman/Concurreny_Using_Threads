import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

class NumberPrinter extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Number: " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class SquarePrinter implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Square: " + (i * i));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Counter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

class CounterThread extends Thread {
    private final Counter counter;

    public CounterThread(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            counter.increment();
        }
    }
}

class ConcurrentListTask implements Runnable {
    private final CopyOnWriteArrayList<String> list;

    public ConcurrentListTask(CopyOnWriteArrayList<String> list) {
        this.list = list;
    }

    @Override
    public void run() {
        list.add(Thread.currentThread().getName());
        System.out.println("List: " + list);
    }
}


class BankAccount {
    private final AtomicInteger balance = new AtomicInteger(0);

    public void deposit(int amount) {
        balance.addAndGet(amount);
    }

    public void withdraw(int amount) {
        balance.addAndGet(-amount);
    }

    public int getBalance() {
        return balance.get();
    }
}

class BankClient extends Thread {
    private final BankAccount account;

    public BankClient(BankAccount account) {
        this.account = account;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            int amount = (int) (Math.random() * 100);
            if (Math.random() > 0.5) {
                account.deposit(amount);
                System.out.println(Thread.currentThread().getName() + " deposited: " + amount);
            } else {
                account.withdraw(amount);
                System.out.println(Thread.currentThread().getName() + " withdrew: " + amount);
            }
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        //task 1
        Thread numberPrinter = new NumberPrinter();
        Thread squarePrinter = new Thread(new SquarePrinter());
        numberPrinter.start();
        squarePrinter.start();
        numberPrinter.join();
        squarePrinter.join();

        //task 2
        Counter counter = new Counter();
        Thread t1 = new CounterThread(counter);
        Thread t2 = new CounterThread(counter);
        Thread t3 = new CounterThread(counter);
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        System.out.println("Final Counter Value: " + counter.getCount());

        //task 3
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        Thread thread1 = new Thread(new ConcurrentListTask(list));
        Thread thread2 = new Thread(new ConcurrentListTask(list));
        Thread thread3 = new Thread(new ConcurrentListTask(list));
        thread1.start();
        thread2.start();
        thread3.start();
        thread1.join();
        thread2.join();
        thread3.join();

        //task 4
        BankAccount account = new BankAccount();
        Thread client1 = new BankClient(account);
        Thread client2 = new BankClient(account);
        Thread client3 = new BankClient(account);
        client1.start();
        client2.start();
        client3.start();
        client1.join();
        client2.join();
        client3.join();
        System.out.println("Final Account Balance: " + account.getBalance());
    }
}
