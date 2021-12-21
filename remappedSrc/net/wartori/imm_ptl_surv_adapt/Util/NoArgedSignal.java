package net.wartori.imm_ptl_surv_adapt.Util;

public class NoArgedSignal implements Runnable {
    public Runnable toRun;
    public boolean isEmpty = true;

    @Override
    public void run() {
        if (!isEmpty) {
            toRun.run();
            isEmpty = true;
        }
    }

    public void connect(Runnable toRun) {
        this.toRun = toRun;
        isEmpty = false;
    }
}
