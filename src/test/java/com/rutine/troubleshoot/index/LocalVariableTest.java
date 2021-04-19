package com.rutine.troubleshoot.index;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * @author rutine
 * @date 2019/11/13 15:00
 */
@SpringJUnitConfig
public class LocalVariableTest {


    @DisplayName("本地变量1")
    @RepeatedTest(5)
    public void testLocalVariable() {
        Boolean bol = null;
        Byte bt = null;
        Short sh = null;
        Integer in = null;
        Float fl = null;
        Double db = null;
        Long lg = null;
        String str = null;
        for (int i = 0; i < 10000; i++) {
            bol = (i % 2 == 0);
            bt = (byte) i;
            sh = (short) i;
            in = i;
            fl = (float) i;
            db = (double) i;
            lg = (long) i;
            str = String.valueOf(i);
        }
    }

    @DisplayName("本地变量2")
    @RepeatedTest(5)
    public void testLocalVariable2() {
        for (int i = 0; i < 10000; i++) {
            Boolean bol = (i % 2 == 0);
            Byte bt = (byte) i;
            Short sh = (short) i;
            Integer in = i;
            Float fl = (float) i;
            Double db = (double) i;
            Long lg = (long) i;
            String str = String.valueOf(i);
        }
    }
}
