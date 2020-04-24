package metrics;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataGenerator.class);
	
	private Random random = new Random();
	
	public void generateDate(int numDays) {
		LOGGER.info("Generating data for {} days", numDays);
		for(int i=numDays-1; i>=0; i--) {
			Date day = Util.addDays(new Date(), -1*i);
			generate(day);
		}
		LOGGER.info("Completed");
	}
	
	private void generate(Date day) {
		LOGGER.info("Generating for {}", Util.formatDate("dd-MM-yyyy", day));
		
		int events = random.nextInt(3000) + 2500;
		for(int i=0; i<events; i++) {
			String user = getUser();
			
			Metric.start().action("Load").eventTime(randomTime(day)).userid(user).send();
			if(random.nextInt(5)%5==0)
				Metric.start().action("Content Search").eventTime(randomTime(day)).userid(user).send();
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
	}
	
	private String getUser() {
		final String[] users = new String[] {"Tommy", "Sam", "John", "Mary", "Bot", "Dot", "Kubes", "Kate",
				"Betty", "Ling Ling", "Willy", "Ped", "Jordan", "Wed", "Weaton", "Billy", "Polly", "Mocky",
				"Ash", "Ashley", "Groke", "Thanos", "Iron", "Meth"};
		return users[random.nextInt(users.length)];
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
