package com.rutine.troubleshoot.learn;

import com.rutine.troubleshoot.utils.JsonUtils;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * 连接池大于线程数略优于单连接方式, 连接池小于线程数不如单连接方式
 *
 * @author rutine
 * @date 2019/3/12 16:43
 */
public class LearnHttp {
    private static Logger logger = LoggerFactory.getLogger(LearnHttp.class);
    private static int timeout = 8000;
    private static int totalThreads = 1000;
    private static int totalRequests = 100;

    private String url = "http://localhost:8080/test";


    public static void main(String[] args) throws Exception {
        LearnHttp.test();
    }


    public static void test() {
        new LearnHttp().testPostWithPool();
    }

    private void testPostWithPool() {
        final long start = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(totalThreads);
        CyclicBarrier barrier = new CyclicBarrier(totalThreads, new Runnable() {
            @Override
            public void run() {
                long end = System.currentTimeMillis();
                logger.info("连接池方式: {}个线程 10w个请求耗时={}", totalThreads, (end - start));
            }
        });
        HttpClient client = httpClient();
        for (int i = 0; i < totalThreads; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        logger.info("---> 已准备");
                        latch.await();
                        for (int j = 0; j < totalRequests; j++) {
                            doPostWithPool(url, Collections.singletonMap("name", "rutine"), client);
                        }
                    } catch (Exception e) {
                        logger.error("---> 线程执行异常", e);
                    } finally {
                        try {
                            logger.info("---> 已完成");
                            barrier.await();
                        } catch (InterruptedException|BrokenBarrierException e) {
                            logger.error("---> 线程等待异常", e);
                        }
                    }
                }
            }, "http-pool-" + i).start();

            latch.countDown();
        }
    }

    private void testPostWithOne() {
        final long start = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(totalThreads);
        CyclicBarrier barrier = new CyclicBarrier(totalThreads, new Runnable() {
            @Override
            public void run() {
                long end = System.currentTimeMillis();
                logger.info("单连接方式: {}个线程 10w个请求耗时={}", totalThreads, (end - start));
            }
        });
        for (int i = 0; i < totalThreads; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        logger.info("---> 已准备");
                        latch.await();
                        for (int j = 0; j < totalRequests; j++) {
                            doPostWithOne(url, Collections.singletonMap("name", "rutine"));
                        }
                    } catch (Exception e) {
                        logger.error("---> 线程执行异常", e);
                    } finally {
                        try {
                            logger.info("---> 已完成.");
                            barrier.await();
                        } catch (InterruptedException|BrokenBarrierException e) {
                            logger.error("---> 线程等待异常", e);
                        }
                    }
                }
            }, "http-conn-" + i).start();

            latch.countDown();
        }
    }

    private String doPostWithPool(String uri, Map<String, Object> params, HttpClient client)  {
        logger.debug("------> request url={} data={}", uri, JsonUtils.toJson(params));

        StringEntity stringEntity = new StringEntity(JsonUtils.toJson(params), Charsets.UTF_8);
        stringEntity.setContentEncoding(Charsets.UTF_8.name());
        stringEntity.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpPost post = new HttpPost(uri);
        post.setEntity(stringEntity);
        try {
            HttpResponse response = client.execute(post);
            String result = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);

            logger.debug("------> url={} result={}", uri, result.toString());
            return result;
        } catch (Exception e) {
            logger.error("------> url={} post data error!", uri, e);
        }

        return null;
    }
    private HttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();


        PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager(registry);
        pool.setMaxTotal(600);
        pool.setDefaultMaxPerRoute(pool.getMaxTotal() >>> 2);

        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(timeout)
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout << 4)
                .build();

        //监控连接池
        //说明：https://www.cnblogs.com/softidea/p/5683083.html
        new HttpClientConnectionMonitorThread(pool);

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .setConnectionManager(pool)
                .build();
    }

    private String doPostWithOne(String uri, Map<String, Object> params) {
        logger.debug("------> request url={} data={}", uri, JsonUtils.toJson(params));

        HttpURLConnection conn = null;
        try {
            URL url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            conn.connect();

            // post data
            try(OutputStream out = conn.getOutputStream()) {
                out.write(JsonUtils.toJson(params).getBytes(Charsets.UTF_8));
                out.flush();
            } catch (IOException e) {
                throw e;
            }

            // read data
            try(InputStream in = ((conn.getResponseCode() < 400)) ? conn.getInputStream() : conn.getErrorStream()) {
                List<String> lines = IOUtils.readLines(in, conn.getContentEncoding());
                StringBuffer result = new StringBuffer();
                for (String line : lines) {
                    result.append(line);
                }
                logger.debug("------> url={} result={}", uri, result.toString());

                return result.toString();
            } catch (IOException e) {
                throw e;
            }
        } catch (Exception e) {
            logger.error("------> request url={} data error!", uri, e);
        } finally {
            IOUtils.close(conn);
        }

        return null;
    }

    /**
     * 使用管理器，管理HTTP连接池 无效链接定期清理功能
     */
    public class HttpClientConnectionMonitorThread extends Thread {
        private final HttpClientConnectionManager pool;
        private volatile boolean shutdown;

        public HttpClientConnectionMonitorThread(HttpClientConnectionManager pool) {
            super();
            this.setName("http-pool-monitor");
            this.setDaemon(true);
            this.pool = pool;
            this.start();
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(5000); // 等待5秒
                        // 关闭过期的链接
                        pool.closeExpiredConnections();
                        // 选择关闭 空闲30秒的链接
                        pool.closeIdleConnections(30, TimeUnit.SECONDS);
                    }
                }
            } catch (InterruptedException ex) {
                logger.error("---> 中断错误!", ex);
            }
        }

        /**
         * 方法描述: 停止 管理器 清理无效链接  (该方法当前暂时关闭)
         */
        @Deprecated
        public void shutDownMonitor() {
            synchronized (this) {
                shutdown = true;
                notifyAll();
            }
        }
    }
}
