package Exercici2.Tools;

public class Semaphore {

    private final Object mutex = new Object();
    private int count = 0;

    public void doWait(){

        synchronized (this.mutex) {
            try {

                //Wait the current thread until notify signal
                //Using while to protect against spurious wake ups.
                while(count == 0) {
                    this.mutex.wait();
                }

            }catch (Exception ignored){}

            //Clear the signal and continue execution
            this.count--;

        }

    }

    public void doSignal() {
        synchronized (this.mutex){
            this.count++;
            this.mutex.notify();
        }

    }

}
