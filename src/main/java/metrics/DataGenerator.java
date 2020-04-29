package metrics;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataGenerator.class);
	
	private Random random = new Random();
	
	private String[] words;
	
	public void generateDate(int numDays) throws IOException {
		LOGGER.info("Generating data for {} days", numDays);
		for(int i=numDays-1; i>=0; i--) {
			Date day = Util.addDays(new Date(), -1*i);
			generate(day);
		}
		LOGGER.info("Completed");
	}
	
	private void generate(Date day) throws IOException {
		LOGGER.info("Generating for {}", Util.formatDate("dd-MM-yyyy", day));
		
		int events = random.nextInt(150) + 200;
		for(int i=0; i<events; i++) {
			String user = getUser();
			String team = getTeam(user);
			String section = getSection(team);
			String cluster = getCluster(team);
			
			Metric.start().action("Load").eventTime(randomTime(day)).userid(user).send();
			if(random.nextInt(5)%5==0)
				Metric.start().action("Content Search").eventTime(randomTime(day)).
				userid(user).team(team).section(section).cluster(cluster).message(getSearchTerms()).send();
			if(random.nextInt(3)%3==0 && !user.startsWith("B"))
				Metric.start().action("Instant Message").eventTime(randomTime(day)).
				userid(user).team(team).section(section).cluster(cluster).send();
			if(random.nextInt(7)%7==0 && !user.contains("o"))
				Metric.start().action("Folders").eventTime(randomTime(day)).
				userid(user).team(team).section(section).cluster(cluster).send();
			
			if(random.nextInt(3)%3==0)				
				Metric.start().sort(getSort(user)).eventTime(randomTime(day)).
				userid(user).team(team).section(section).cluster(cluster).send();
		}
		
		int dataCount = random.nextInt(250) + 120;
		for(int i=0; i<dataCount; i++) {
			String user = getUser();
			String team = getTeam(user);
			String section = getSection(team);
			String cluster = getCluster(team);
			String project = getProject();
			String priority = String.valueOf(random.nextInt(4)+1);
			int prioritySort = getPrioritySort(priority);
			String status = getStatus(priority);
			String app = getApp();
			int score = getScore(status, prioritySort);
			
			Metric.start().status(status).priority(priority).prioritySort(prioritySort).score(score).app(app).eventTime(randomTime(day)).
				userid(user).team(team).section(section).cluster(cluster).project(project).send();
			
			long delay = random.nextInt(120);
			if(priority.equals("1"))
				delay = random.nextInt(15);
			else if(priority.equals("2"))
				delay = random.nextInt(25);
			DataMetric.start().eventTime(randomTime(day)).delay(delay).
				priority(priority).project(project).team(team).send();
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}
	}
	
	private int getPrioritySort(String priority) {
		return random.nextInt(10) + Integer.parseInt(priority) * 10;
	}

	private int getScore(String status, int priority) {
		//outlier generation
		if(random.nextInt(500)==23)
			return random.nextInt(100);
		
		int statusScore = 0;
		int priorityScore = 0;
		
		if("REPORTED".equals(status))
			statusScore = 60;
		else if("DISCARDED".equals(status))
			statusScore = 3;
		else if("ARCHIVED".equals(status))
			statusScore = 23;
		else if("DRAFT".equals(status))
			statusScore = 11 + random.nextInt(7);
		
		if(priority < 30)
			priorityScore = 15 + random.nextInt(10);
		else
			priorityScore = random.nextInt(10);
		
		return statusScore + priorityScore + random.nextInt(17);
	}

	private String getApp() {
		String[] apps = new String[] {"OUTLOOK", "OUTLOOK", "AUDIO", "DOCS", "SMS", "IM", "IM", "IM", "IM", "IM", "GAMES", "BROWSER"};
		return apps[random.nextInt(apps.length)];
	}

	private String getStatus(String priority) {
		if("3".equals(priority) || "4".equals(priority) || "5".equals(priority)) {
			final String[] statuses = new String[] {"RAW", "RAW", "RAW", "RAW", "RAW", "RAW", "RAW", "RAW", "RAW","RAW", "RAW", "RAW", "RAW",
					"DISCARDED", "DISCARDED", "DISCARDED", "DISCARDED", "DISCARDED", 
					"DRAFT", 
					"ARCHIVED"};
			return statuses[random.nextInt(statuses.length)];
		}
		
		final String[] statuses = new String[] {"RAW", "RAW", "RAW", "RAW", "RAW", "RAW", "RAW", "RAW", "RAW","RAW", "RAW", "RAW", "RAW",
				"DISCARDED", "DISCARDED", "DISCARDED", "DISCARDED", "DISCARDED", 
				"DRAFT", "DRAFT", "DRAFT", 
				"ARCHIVED", "REPORTED"};
		return statuses[random.nextInt(statuses.length)];
	}
	
	private String getSort(String user) {
		if(user.startsWith("D"))
			return "id";
		else if(user.contains("o"))
			return "priority";
		else 
			return "date";
	}
	
	private String getSearchTerms() throws IOException {
		if(words==null) {
			List<String> lines = FileUtils.readLines(new File("src/main/resources/google-10000-words.txt"), Charset.defaultCharset());
			words = lines.toArray(new String[lines.size()]);
		}
		int length = random.nextInt(5);
		String s = "";
		for(int i=0; i<length; i++)
			s += words[random.nextInt(words.length)] + " ";
		return s;
	}
	
	private String getUser() {
		final String[] users = new String[] {"Tommy", "Sam", "John", "Mary", "Bot", "Dot", "Kubes", "Kate",
				"Betty", "Ling Ling", "Willy", "Ped", "Jordan", "Wed", "Weaton", "Billy", "Polly", "Mocky",
				"Ash", "Ashley", "Groke", "Thanos", "Iron", "Meth"};
		return users[random.nextInt(users.length)];
	}
	
	private String getTeam(String user) {
		final String[] teams = new String[] {"Alpha", "Arrows", "Anchorage", "Bravo", "Bloop", "Banner", "Delta", "Decho", "Dargate"};
		return teams[user.charAt(0)%teams.length];
	}
	
	private String getProject() {
		final String[] projects = new String[] {"W-1000", "W-1001", "GH-1001", "GG-0023", "Others"};
		return projects[random.nextInt(projects.length)];
	}
	
	private String getSection(String team) {
		return team.charAt(0) + "-1";
	}
	
	private String getCluster(String team) {
		return "Big Gang";
	}
	
	private Date randomTime(Date date) {
		Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 7 + random.nextInt(14));
        c.set(Calendar.MINUTE, random.nextInt(60));
        c.set(Calendar.SECOND, random.nextInt(60));
        return c.getTime();
	}
}
