package metrics;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.logstash.logback.marker.Markers;


public class Metric {

  private static final Logger LOGGER = LoggerFactory.getLogger("metric");
  
  private String action;
  
  private String message;
  
  private long start;
  
  private String eventTime;
  
  private String userid;
  
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
  
  public Metric eventTime(Date eventTime) {
	  this.eventTime = Util1.formatDate("yyyy-MM-dd'T'HH:mm:ssZ", eventTime);
	  return this;
  }
  
  public Metric userid(String userid) {
	  this.userid = userid;
	  return this;
  }
  
  public void send() {
    long duration = System.currentTimeMillis() - start;
    LOGGER.info(Markers.append("action", action).
    	and(Markers.append("eventTime", eventTime)).
    	and(Markers.append("userid", userid)).
        and(Markers.append("duration", duration)), message);
  }
  
}
