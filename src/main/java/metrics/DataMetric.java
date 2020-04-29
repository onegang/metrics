package metrics;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.logstash.logback.marker.Markers;


public class DataMetric {

  private static final Logger LOGGER = LoggerFactory.getLogger("metric");
  
  private String team;
  
  private String project;
  
  private String priority;
  
  private long delay; //in mins
  
  private String eventTime;
  
  private DataMetric() {
    
  }
  
  public static DataMetric start() {
    DataMetric metric = new DataMetric();
    return metric;
  }
  
  public DataMetric delay(long delay) {
    this.delay = delay;
    return this;
  }
  
  public DataMetric priority(String priority) {
	  this.priority = priority;
	  return this;
  }
  
  public DataMetric eventTime(Date eventTime) {
	  this.eventTime = Util.formatDate("yyyy-MM-dd'T'HH:mm:ssZ", eventTime);
	  return this;
  }
  
  public DataMetric project(String project) {
	  this.project = project;
	  return this;
  }
  
  public DataMetric team(String team) {
	  this.team = team;
	  return this;
  }
  
  public void send() {
    LOGGER.info(Markers.append("priority", priority).
    	and(Markers.append("eventTime", eventTime)).
    	and(Markers.append("team", team)).
    	and(Markers.append("delay", delay)).
    	and(Markers.append("project", project)), "");
  }
  
}
