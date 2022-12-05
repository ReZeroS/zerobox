package club.qqtim.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class CntDownLatch {

    private static class Sync extends AbstractQueuedSynchronizer {


        public Sync(int cnt) {
            setState(cnt);
        }


        @Override
        protected boolean tryAcquire(int arg) {
            return super.tryAcquire(arg);
        }

        @Override
        protected boolean tryRelease(int arg) {
            return super.tryRelease(arg);
        }

        @Override
        protected boolean isHeldExclusively() {
            return super.isHeldExclusively();
        }

        @Override
        protected int tryAcquireShared(int arg) {
            return getState() == 0? 1: -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {

            for (;;) {
                final int state = getState();
                if (state == 0) {
                    return false;
                }
                int nextState = state - 1;
                if (compareAndSetState(state, nextState)) {
                    return nextState == 0;
                }
                // else do retry
            }
        }
    }

    private final Sync sync;

    public CntDownLatch(int cnt) {
        this.sync = new Sync(cnt);
    }



    public void await() throws InterruptedException {
        // acquireSharedInterruptibly cause state not only zero or one (exclusive mode)
        this.sync.acquireSharedInterruptibly(1);
    }

    public void cntDown(){
        this.sync.releaseShared(1);
    }





}
