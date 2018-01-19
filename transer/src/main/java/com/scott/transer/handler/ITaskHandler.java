package com.scott.transer.handler;


import com.scott.transer.ITaskHolder;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 11:52</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public interface ITaskHandler extends ITaskHolder {

    void setThreadPool(ThreadPoolExecutor threadPool);

    void start();

    void stop();

    Map<String,String> getHeaders();

    Map<String,String> getParams();

    void setHeaders(Map<String,String> headers);

    void setParams(Map<String,String> params);

    void setHandlerListenner(ITaskHandlerCallback l);
}
