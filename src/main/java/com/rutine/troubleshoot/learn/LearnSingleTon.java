
package com.rutine.troubleshoot.learn;

/**
 * 懒汉单例实现方法
 *
 * @date 2017年3月18日
 * @author rutine
 */
public class LearnSingleTon {
    
    private LearnSingleTon() { }
    
    public static LearnSingleTon getInstance() {
        return SingleTonContainer.singleTon;
    }
    
    private static class SingleTonContainer {
        private static LearnSingleTon singleTon  = new LearnSingleTon();
    }
}
