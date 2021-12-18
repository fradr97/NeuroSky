import utils.FileUtils;

import java.io.*;

public class Main {
    private static NeuroSkyAttention neuroSkyAttention;
    public static void main (String[] args) throws IOException {
        neuroSkyAttention = new NeuroSkyAttention();

        //FileUtils fileUtils = new FileUtils();

        boolean isStarted = neuroSkyAttention.waitForStarting();

        if(isStarted) {
            Thread attentionThread = new Thread(new AttentionThread());
            attentionThread.start();
            //fileUtils.writeFile("C:\\Users\\Francesco\\Desktop\\attention.csv",  list, false);
        }
        else {
            System.out.println("Neurosky not working!");
        }

    }

    protected static class AttentionThread implements Runnable {
        @Override
        public void run() {
            neuroSkyAttention.getAttention();
        }
    }
}
