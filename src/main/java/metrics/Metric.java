package metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.logstash.logback.marker.Markers;


public class Metric {

  private static final Logger LOGGER = LoggerFactory.getLogger("metric");
  
  private String action;
  
  private String message;
  
  private long start;
  
  private Metric() {
    start = System.currentTimeMillis();
  }
  
  public static Metric start() {
    Metric metric = new Metric();
    return metric;
  }
  
  public Metric action(String action) {
    this.action = action;
    return this;
  }
  
  public Metric message(String message) {
    this.message = message;
    return this;
  }
  
  public void send() {
    long duration = System.currentTimeMillis() - start;
    LOGGER.info(Markers.append("action", action).
        and(Markers.append("duration", duration)), message);
  }
  
}
