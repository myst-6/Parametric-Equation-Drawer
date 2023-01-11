package main;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;

public class Util {
    public static void logHeap() {
        for (MemoryPoolMXBean mpBean : ManagementFactory.getMemoryPoolMXBeans()) {
            if (mpBean.getType() == MemoryType.HEAP) {
                System.out.printf("Name: %s: %s\n", mpBean.getName(), mpBean.getUsage());
            }
        }
    }
}
