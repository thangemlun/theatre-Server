package server;

import lombok.extern.slf4j.Slf4j;
import model.MasterData;

import java.util.Date;

@Slf4j
public class TheatreAutoUpdateScheduler extends Thread{
    @Override
    public void run() {
        try {
            System.out.println("Update master data start ...");
            job();
        } catch (Exception e) {

        }
    }

    private void job() throws InterruptedException {
        Thread.sleep(60000 * 60);
        log.info("Job run at : {}", new Date());
        MasterData.fetchData();
        job();
    }
}
