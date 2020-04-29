package metrics;

import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import net.logstash.logback.marker.Markers;


public class Metric {

  private static final Logger LOGGER = LoggerFactory.getLogger("metric");
  
  private String action;
  
  private String message;
  
  private String status;
  
  private Collection<String> clusters;
  
  private Collection<String> sections;
  
  private Collection<String> teams;
  
  private String project;
  
  private String priority;
  
  private int prioritySort;
  
  private String app;
  
  private int score;
  
  private Collection<String> sorts;
  
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
  
  public Metric score(int score) {
	this.score = score;
	return this;
  }
  
  public Metric app(String app) {
	this.app = app;
	return this;
  }
  
  public Metric status(String status) {
	  this.status = status;
	  return this;
  }
  
  public Metric eventTime(Date eventTime) {
	  this.eventTime = Util.formatDate("yyyy-MM-dd'T'HH:mm:ssZ", eventTime);
	  return this;
  }
  
  public Metric userid(String userid) {
	  this.userid = userid;
	  return this;
  }
  
  public Metric project(String project) {
	  this.project = project;
	  return this;
  }
  
  public Metric priority(String priority) {
	  this.priority = priority;
	  return this;
  }
  
  public Metric prioritySort(int prioritySort) {
	  this.prioritySort = prioritySort;
	  return this;
  }
  
  public Metric sort(String sort) {
	  this.sorts = Lists.newArrayList(sort);
	  return this;
  }
  
  public Metric sort(Collection<String> sorts) {
	  this.sorts = sorts;
	  return this;
  }
  
  public Metric team(Collection<String> teams) {
	  this.teams = teams;
	  return this;
  }
  
  public Metric team(String team) {
	  this.teams = Lists.newArrayList(team);
	  return this;
  }
  
  public Metric section(Collection<String> sections) {
	  this.sections = sections;
	  return this;
  }
  
  public Metric section(String section) {
	  this.sections = Lists.newArrayList(section);
	  return this;
  }
  
  public Metric cluster(Collection<String> clusters) {
	  this.clusters = clusters;
	  return this;
  }
  
  public Metric cluster(String cluster) {
	  this.clusters = Lists.newArrayList(cluster);
	  return this;
  }
  
  public void send() {
    long duration = System.currentTimeMillis() - start;
    LOGGER.info(Markers.append("action", action).
    	and(Markers.append("eventTime", eventTime)).
    	and(Markers.append("status", status)).
    	and(Markers.append("project", project)).
    	and(Markers.append("priority", priority)).
    	and(Markers.append("prioritySort", prioritySort)).
    	and(Markers.append("sorts", sorts)).
    	and(Markers.append("userid", userid)).
    	and(Markers.append("score", score)).
    	and(Markers.append("app", app)).
    	and(Markers.append("teams", teams)).
    	and(Markers.append("sections", sections)).
    	and(Markers.append("clusters", clusters)).
        and(Markers.append("duration", duration)), message);
  }
  
}
