//package club.qqtim.lock;
//
//import org.redisson.RedissonLockEntry;
//
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.TimeUnit;
//
//public class DistributeLock {
//
//
//
//    private void lock(long leaseTime, TimeUnit unit, boolean interruptibly) throws InterruptedException {
//        long threadId = Thread.currentThread().getId();
//        Long ttl = tryAcquire(-1, leaseTime, unit, threadId);
//        // lock acquired
//        if (ttl == null) {
//            return;
//        }
//
//        CompletableFuture<RedissonLockEntry> future = subscribe(threadId);
//        pubSub.timeout(future);
//        RedissonLockEntry entry;
//        if (interruptibly) {
//            entry = commandExecutor.getInterrupted(future);
//        } else {
//            entry = commandExecutor.get(future);
//        }
//
//        try {
//            while (true) {
//                ttl = tryAcquire(-1, leaseTime, unit, threadId);
//                // lock acquired
//                if (ttl == null) {
//                    break;
//                }
//
//                // waiting for message
//                if (ttl >= 0) {
//                    try {
//                        entry.getLatch().tryAcquire(ttl, TimeUnit.MILLISECONDS);
//                    } catch (InterruptedException e) {
//                        if (interruptibly) {
//                            throw e;
//                        }
//                        entry.getLatch().tryAcquire(ttl, TimeUnit.MILLISECONDS);
//                    }
//                } else {
//                    if (interruptibly) {
//                        entry.getLatch().acquire();
//                    } else {
//                        entry.getLatch().acquireUninterruptibly();
//                    }
//                }
//            }
//        } finally {
//            unsubscribe(entry, threadId);
//        }
////        get(lockAsync(leaseTime, unit));
//    }
//
//
//}
