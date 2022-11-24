package DatabasePattern;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class LockManager {

    private static LockManager instance;

    //Key: lockable
    //Value: owner
    private ConcurrentMap<String, LocalDateTime> lockMap;

    public static synchronized LockManager getInstance() {
        if(instance == null) {
            instance = new LockManager();
        }
        return instance;
    }

    private LockManager() {
        lockMap = new ConcurrentHashMap<String, LocalDateTime>();
    }

    public boolean acquireLock(String lockable) {
        releaseIdleLocks();
        if(!lockMap.containsKey(lockable)) {
            //no lock on lockable, grant lock
            lockMap.put(lockable, LocalDateTime.now());
        } else {
            return false;
        }
        return true;
    }

    public void releaseIdleLocks(){
        for (String key : lockMap.keySet()){
            if (Duration.between(lockMap.get(key), LocalDateTime.now()).getSeconds() > 60) {
                releaseLock(key);
            }
        }
    }

    public void releaseLock(String lockable) {
        lockMap.remove(lockable);
//        notify();
    }
}