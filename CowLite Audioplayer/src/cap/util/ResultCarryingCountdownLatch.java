/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.util;

import java.util.concurrent.CountDownLatch;

/**
 *
 * @author Wessel
 */
public class ResultCarryingCountdownLatch<ResultType> extends CountDownLatch {

    private ResultType result;

    public ResultCarryingCountdownLatch(int count) {
        super(count);
    }

    public void countDown(ResultType result) {
        this.result = result;
        super.countDown();
    }
    
    public ResultType awaitResult() throws InterruptedException {
        super.await();
        return result;
    }

}
