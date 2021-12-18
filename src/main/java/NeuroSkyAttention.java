import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

public class NeuroSkyAttention {
    private final ThinkGearSocketClient thinkGearSocketClient;
    private boolean started;

    private final int WAITING_TIME = 20;
    private int timer;

    public NeuroSkyAttention() throws IOException {
        this.thinkGearSocketClient = new ThinkGearSocketClient();
        this.thinkGearSocketClient.connect();
        this.timer = 0;
    }

    public boolean isStarted() {
        return this.started;
    }

    private boolean starting() {
        this.started = false;

        if (thinkGearSocketClient.isDataAvailable()) {
            while (!this.started && timer < WAITING_TIME) {
                String jsonString = thinkGearSocketClient.getData();

                try {
                    JSONObject jsonObject = (new JSONObject(jsonString)).getJSONObject("eSense");

                    if(jsonObject != null)
                        this.started = true;
                } catch (Exception ignored) { }
            }
        }
        return this.started;
    }

    public boolean waitForStarting() {
        Thread timerThread = new Thread(new TimerThread());
        timerThread.start();
        this.starting();
        return timer < WAITING_TIME && started;
    }

    public List<String[]> getAttention() {
        int attention = 0;
        Timestamp timestamp = null;
        Date date = new Date();
        List<String[]> list = new ArrayList<>();

        if (thinkGearSocketClient.isDataAvailable()) {
            while (thinkGearSocketClient.isConnected() && started) {
                String jsonString = thinkGearSocketClient.getData();
                System.out.println(jsonString);

                try {
                    JSONObject jsonObject = (new JSONObject(jsonString)).getJSONObject("eSense");

                    //System.out.println(jsonObject);
                    attention = jsonObject.getInt("attention");
                } catch (Exception ex) {
                    attention = -1;
                }
                timestamp = new Timestamp(date.getTime());
                System.out.println(attention);
                list.add(new String[] {
                        String.valueOf(timestamp),
                        String.valueOf(attention)
                });
            }
            return list;
        }
        else return null;
    }

    public void stopConnection() throws IOException {
        this.started = false;
        this.thinkGearSocketClient.close();
    }

    protected class TimerThread implements Runnable {
        @Override
        public void run() {
            try {
                while(!started && timer < WAITING_TIME) {
                    timer ++;
                    System.out.println(timer);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
