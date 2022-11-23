//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.dynamodbv2.document.DynamoDB;
//import com.amazonaws.services.dynamodbv2.document.Table;
//import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.lambdaworks.redis.*;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.providers.PooledConnectionProvider;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public class Consumer {
  private static final String QUEUE_NAME = "LiftServer";
  private static final Integer NUM_THREADS = 5;

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("172.31.15.42");
    factory.setUsername("admin");
    factory.setPassword("s3crEt");
    factory.setVirtualHost("/");
    factory.setPort(5672);

      final JedisPoolConfig poolConfig = buildPoolConfig();
      JedisPool jedisPool = new JedisPool(poolConfig, "44.242.61.37", 6379);
//      HostAndPort config = new HostAndPort("redis-18303.c1.us-west-2-2.ec2.cloud.redislabs.com", 18303,);
//      PooledConnectionProvider provider = new PooledConnectionProvider(config);
//      UnifiedJedis client = new UnifiedJedis(provider);

//    RedisClient redisClient = new RedisClient(
//        RedisURI.create("redis://2csOWF2Y8wjcw75PChJoIIUUmZj2sQCf@redis-18303.c1.us-west-2-2.ec2.cloud.redislabs.com:18303"));
//    RedisConnection<String, String> redisConnection = redisClient.connect();
//
//    System.out.println("Connected to Redis");

//    BasicAWSCredentials awsCredentials = new BasicAWSCredentials("AKIA3ESIMGSXPJO7KXN3",
//        "NSPq56uRvxiliMbwLeraCdXVaHZvdxfchNfHWpx6");
//    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
//        .withRegion(Regions.US_WEST_2)
//        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//        .build();
//
//    DynamoDB dynamoDB = new DynamoDB(client);
//
//    Table table = dynamoDB.getTable("LiftServerDB");
//    ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
//        .withReadCapacityUnits(15L)
//        .withWriteCapacityUnits(12L);
//
//    table.updateTable(provisionedThroughput);
//
//    table.waitForActive();
    //r = redis.StrictRedis()
    Connection connection = factory.newConnection();
    ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
    for (int i = 0; i < NUM_THREADS; i++) {
      pool.execute(new ConsumerRunnable(connection, jedisPool, QUEUE_NAME));
    }
//
    pool.shutdown();
    pool.awaitTermination(10, TimeUnit.SECONDS);
  }

  private static JedisPoolConfig buildPoolConfig() {
    final JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxTotal(128);
    poolConfig.setMaxIdle(128);
    poolConfig.setMinIdle(16);
    poolConfig.setTestOnBorrow(true);
    poolConfig.setTestOnReturn(true);
    poolConfig.setTestWhileIdle(true);
    poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
    poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
    poolConfig.setNumTestsPerEvictionRun(3);
    poolConfig.setBlockWhenExhausted(true);
    return poolConfig;
  }
}
