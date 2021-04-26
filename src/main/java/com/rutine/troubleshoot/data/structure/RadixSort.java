package com.rutine.troubleshoot.data.structure;

/**
 * 基数排序, 顺着链表一个个元素先分派到队列, 然后收集(前一个队列尾元素链接后一个队列头元素)
 * @author rutine
 * @date 2021/4/23 9:58
 */
public class RadixSort {
    private static int basicCode = 48; //字符0的code码=48
    private static int radix = 10; //基数

    public static void main(String[] args) {
       sort(gen(20, 3));
    }

    static void sort(Node[] nodes) {
        int[] h = new int[radix]; //第i个队列头指针
        int[] t = new int[radix]; //第i个队列尾指针

        Node p = nodes[0]; //链表头指针
        for (int i = 2; i >= 0; i--) {
            for (int j = 0; j < radix; h[j++] = 0); //重置头指针

            //分配
            while (p.link != 0) {
                int index = nodes[p.link].data.charAt(i) - basicCode;
                if (h[index] == 0) { //队列为空
                    h[index] = p.link;
                } else { //队列尾部节点指向新入队索引
                    nodes[t[index]].link = p.link;
                }
                t[index] = p.link;
                p = nodes[p.link];
            }

            //找到头结点
            int j = 0;
            for (; h[j] == 0; j++);
            p = nodes[h[j]]; //p指向第一个

            //收集
            for (int k = j + 1; k < radix; k++) {
                if (h[k] == 0) {
                    continue;
                }

                nodes[t[j]].link = h[k];
                j = k;
            }

            //最后一个结束链接
            nodes[t[j]].link = 0;
        }

        while (p.link != 0) {
           System.out.println(p.data);
//           System.out.print(' ');
           p = nodes[p.link];
        }
    }

    static Node[] gen(int n, int m) {
        if (n <= 0 || m <= 0) {
            return new Node[0];
        }

        Node[] nodes = new Node[n];
        Node p = null;
        for (int i = 0; i < n; i++) {
            if (p == null) {
                p = nodes[i] = new Node(null); //头结点
                continue;
            }

            char[] chs = new char[m];
            for (int j = 0; j < m; j++) {  //m位长度
                chs[j] = random();
            }
            nodes[i] = new Node(new String(chs));
            p.link = i;
            p = nodes[i];
        }

        return nodes;
    }
    static char random() {
        return (char) (Math.random() * 10 + basicCode);
    }

    static class Node {
        private String data; //设等长且数字组成的字符串
        private int link;

        public Node(String data) {
            this.data = data;
        }
    }
}
