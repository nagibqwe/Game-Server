package game.core.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

/**
 * 异步的httpClient
 *
 * @Auther: gouzhongliang
 * @Date: 2021/12/17 11:12
 */
public enum HttpAsyncClient {

    instance;

    private CloseableHttpAsyncClient client;

    public void init() {
        init(null, Runtime.getRuntime().availableProcessors(), 1000, 100);
    }

    public void init(int threadCount) {
        init(null, threadCount, 1000, 100);
    }

    public void init(int threadCount, int maxTotal, int preTotal) {
        init(null, threadCount, maxTotal, preTotal);
    }

    public void init(RequestConfig requestConfig, int threadCount, int maxTotal, int preTotal) {
        if (requestConfig == null) {
            requestConfig = RequestConfig.custom()
                    .setConnectTimeout(3000)
                    .setSocketTimeout(3000)
                    .setConnectionRequestTimeout(1000)
                    .build();
        }
        createClient(requestConfig, threadCount, maxTotal, preTotal);
    }

    public void createClient(RequestConfig requestConfig, int threadCount, int maxTotal, int preTotal) {
        //配置io线程
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(threadCount)
                .setSoKeepAlive(true)
                .build();
        //设置连接池大小
        ConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            e.printStackTrace();
        }
        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
        connManager.setMaxTotal(maxTotal);
        connManager.setDefaultMaxPerRoute(preTotal);
        client = HttpAsyncClients.custom().setConnectionManager(connManager).setDefaultRequestConfig(requestConfig).build();
        client.start();
    }

    public CloseableHttpAsyncClient getClient() {
        return client;
    }

    public void setClient(CloseableHttpAsyncClient client) {
        this.client = client;
    }

    /**
     * 用法
     */
    public void execute() {
        client.execute(new HttpPost(), new FutureCallback<HttpResponse>() {

            @Override
            public void completed(HttpResponse result) {
                // TODO Auto-generated method stub

            }

            @Override
            public void failed(Exception ex) {
                // TODO Auto-generated method stub

            }

            @Override
            public void cancelled() {
                // TODO Auto-generated method stub
            }
        });
    }
}
