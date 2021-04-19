package com.rutine.troubleshoot.index;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author rutine
 * @date 2019/11/13 15:52
 */
@SpringJUnitConfig
public class ExecutorServiceTest {


    @Test
    @Disabled
    public void testThread() throws InterruptedException {
        ExecutorService es = new ThreadPoolExecutor(5,20,10L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(10000),
                new CustomizableThreadFactory("troubleshoot-thread")
                , new RejectedExecutionHandler(){
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println(r.toString() + "is disCard");
                    }
                }
                );


        Thread deamon = new Thread(() -> {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                final int a = i;
                es.submit(() -> {
                    if (a % 1000 == 0) {
                        System.out.println(Thread.currentThread().getName() + " --> "+ a);
                    }
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
            System.out.println(Thread.currentThread().getName() + " --> time="+ (System.currentTimeMillis() - start));
        });
        deamon.setDaemon(true);
        deamon.start();

        System.out.println(Thread.currentThread().getName());
    }

    static List<People> list = Lists.newArrayList();

    @BeforeAll
    public static void before() {
        for (int i = 0; i < 1000000; i++) {
            list.add(new People(i, "test-" + i));
        }
    }

    @DisplayName("测试1")
    @RepeatedTest(10)
    public void testPeople() {
        List<People> peopleList = Lists.newArrayList(list);
        peopleList.parallelStream()
                .filter(people -> people.id % 3 == 0)
                .forEach(people -> {
                    people.name = people.name + "test1";
                });

//        System.out.println("test1, name:" + peopleList.get(0).name + ", size:" + peopleList.size());
    }

    @DisplayName("测试2")
    @RepeatedTest(10)
    public void testPeople2() {
        List<People> peopleList = Lists.newArrayList(list);
        for (People people : peopleList) {
            if (people.id % 3 == 0) {
                people.name = people.name + "test2";
            }
        }

//        System.out.println("test2, name:" + peopleList.get(0).name + ", size:" + peopleList.size());
    }

    public static class People {
        int id;
        String name;
        People(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
