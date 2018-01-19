package com.scott.transer;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 13:05</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public interface TaskState {
    int STATE_ERROR = -1;
    int STATE_RUNNING = 1;
    int STATE_STOP = 2;
    int STATE_FINISH = 3;
    int STATE_READY = 4;
}
