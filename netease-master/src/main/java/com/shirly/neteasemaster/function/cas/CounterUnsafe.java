package com.shirly.neteasemaster.function.cas;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author shirly
 * @Date 2020/2/7 15:57
 * @Description 利用Unsafe实现原子性操作
 *     AtomicReference实际用的就是Unsafe中的API
 */
public class CounterUnsafe {

    AtomicInteger ai = new AtomicInteger(0);

    volatile int i = 0;
    private static Unsafe unsafe; // 提供CAS操作
    private static long valueOffset; //i字段的偏移量，从逻辑上代表了i字段

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null); // 通过反射获取

            Field iField = CounterUnsafe.class.getDeclaredField("i");
            valueOffset = unsafe.objectFieldOffset(iField); // 获取i字段的偏移量
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add() {
        for(;;) {// CAS操作可能会失败，所以自旋
            int current = unsafe.getIntVolatile(this, valueOffset); // 获取当前值
            // compareAndSwapInt就是原子性操作
            if (unsafe.compareAndSwapInt(this, valueOffset, current, current + 1)) {
                break;
            }
        }
    }

    public void AtomicIntegerAdd() {
        ai.getAndIncrement();//和上面自写的都是用的Unsafe中的API
    }

    public static void main(String[] args) throws InterruptedException {
        CounterUnsafe cu = new CounterUnsafe();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    cu.add();
                    cu.AtomicIntegerAdd();
                }
            }).start();
        }
        Thread.sleep(6000L);
        System.out.println("cu.add():" + cu.i);
        System.out.println("cu.AtomicIntegerAdd():" + cu.ai.get());
    }
}

