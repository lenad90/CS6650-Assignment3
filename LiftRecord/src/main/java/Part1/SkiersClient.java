package Part1;

import static java.lang.Math.round;

import Part1.Model.SkiersRunner;
import Part1.Threads.Phase;
import Part1.Threads.Producer;
import io.swagger.client.ApiClient;
import io.swagger.client.api.SkiersApi;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class SkiersClient {
  private static final Integer NUM_POSTS = 200000;

  public static void main(String[] args) throws InterruptedException {
    int processors = Runtime.getRuntime().availableProcessors();

    SkiersApi skierApi = new SkiersApi();
    ApiClient client = skierApi.getApiClient();
    BlockingQueue<SkiersRunner> dataBuffer = new LinkedBlockingQueue<>();

    //client.setBasePath("http://localhost:8080/LiftServer_copy_war/");
    client.setBasePath("http://ec2-35-92-85-30.us-west-2.compute.amazonaws.com:8080/LiftServer_war/");
    //client.setBasePath("http://localhost:8080/LiftServer_war/");
    new Thread(new Producer(NUM_POSTS, dataBuffer)).start();

    int phase1Threads = processors * 4;
    int phase1Post = 1000;
    int phase2Trigger = phase1Threads / 4;


    long start = System.currentTimeMillis();
    CountDownLatch phase2Signal = new CountDownLatch(phase2Trigger);
    Phase phase1 = new Phase("Phase 1", phase1Threads, phase1Post, skierApi,
        phase2Signal, dataBuffer);
    phase1.startPhase();

    int phase2Threads = phase1Threads * 2;
    int phase2Post = 801;
    CountDownLatch phase3Signal = new CountDownLatch((int) (phase2Threads * 0.5));
    Phase phase2 = new Phase("Phase 2", phase2Threads, phase2Post, skierApi,
        phase3Signal, dataBuffer);
    phase2Signal.await();
    phase2.startPhase();


    int phase3Threads = phase2Threads * 2;
    int phase3Post = 500;
    CountDownLatch phase4Signal = new CountDownLatch((int) (phase3Threads*0.3));
    Phase phase3 = new Phase("Phase 3", phase3Threads,
        phase3Post, skierApi, phase4Signal, dataBuffer);
    phase3Signal.await();
    phase3.startPhase();

    int phase4Threads = phase3Threads * 2;
    int phase4Post = 206;
    CountDownLatch completeTrigger = new CountDownLatch((int) (phase4Threads*0.1));
    Phase phase4 = new Phase("Phase 4", phase4Threads,
        phase4Post, skierApi, completeTrigger, dataBuffer);
    phase4Signal.await();
    phase4.startPhase();
    long end = System.currentTimeMillis();

    phase1.finishPhase();
    phase2.finishPhase();
    phase3.finishPhase();
    phase4.finishPhase();
    phase1.phaseStats();
    phase2.phaseStats();
    phase3.phaseStats();
    phase4.phaseStats();
    double runtime = (end - start)*0.001;
    System.out.println("============ PART 1 STATS =============");
    System.out.println("Total run time after all phases completed: " + round(runtime) + "/s");
    System.out.println("Total throughput after all phases completed: " + round(NUM_POSTS/runtime) + "/s");
  }
}
