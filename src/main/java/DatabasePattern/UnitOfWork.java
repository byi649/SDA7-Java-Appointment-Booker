package DatabasePattern;

import java.sql.Connection;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

public class UnitOfWork {
    private static ThreadLocal current = new ThreadLocal();
    private List<Object> newObjects = new ArrayList<>();
    private List<Object> dirtyObjects = new ArrayList<>();
    private List<Object> deletedObjects = new ArrayList<>();

    public static void newCurrent() {
        setCurrent(new UnitOfWork());
    }

    public static void setCurrent(UnitOfWork uow) {
        current.set(uow);
    }

    public static UnitOfWork getCurrent() {
        return (UnitOfWork) current.get();
    }

    public void registerNew(Object object) {
        newObjects.add(object);
    }

    public void registerDirty(Object object) {
        if (!dirtyObjects.contains(object) && !newObjects.contains(object)) {
            dirtyObjects.add(object);
        }
    }

    public void registerDeleted(Object object) {
        if (newObjects.remove(object)) return;
        dirtyObjects.remove(object);
        if (!deletedObjects.contains(object)) {
            deletedObjects.add(object);
        }
    }

    public boolean commit() {
        boolean success = true;
        System.out.println("Starting UOW");

        try {
            for (Object object : newObjects) {
                if (!IDataMapper.getMapper(object).insert(object)) {
                    success = false;
                    System.out.println("Failed new: " + object);
                }
                System.out.println("New object: " + object);
            }
            for (Object object : dirtyObjects) {
                if (!IDataMapper.getMapper(object).update(object)) {
                    success = false;
                    System.out.println("Failed dirty: " + object);
                }
                System.out.println("Dirty object: " + object);
            }
            for (Object object : deletedObjects) {
                if (!IDataMapper.getMapper(object).delete(object)) {
                    success = false;
                    System.out.println("Failed deleted: " + object);
                }
                System.out.println("Deleted object: " + object);
            }
        } catch (Exception e) {
            System.out.println("Unit of work commit error");
            System.out.println(e);
        }
        return success;
    }

}
