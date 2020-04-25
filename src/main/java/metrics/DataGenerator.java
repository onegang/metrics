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
		
		int events = random.nextInt(1500) + 2000;
		for(int i=0; i<events; i++) {
			String user = getUser();
			
			Metric.start().action("Load").eventTime(randomTime(day)).userid(user).send();
			if(random.nextInt(5)%5==0)
				Metric.start().action("Content Search").eventTime(randomTime(day)).userid(user).message(getSearchTerms()).send();
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
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
	
	private Date randomTime(Date date) {
		Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 7 + random.nextInt(14));
        c.set(Calendar.MINUTE, random.nextInt(60));
        c.set(Calendar.SECOND, random.nextInt(60));
        return c.getTime();
	}
}
