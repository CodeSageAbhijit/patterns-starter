package com.upgrad.patterns.Strategies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrad.patterns.Config.RestServiceGenerator;
import com.upgrad.patterns.Entity.JohnHopkinResponse;
import com.upgrad.patterns.Interfaces.IndianDiseaseStat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Service
public class JohnHopkinsStrategy implements IndianDiseaseStat {

	private Logger logger = LoggerFactory.getLogger(JohnHopkinsStrategy.class);

	private RestTemplate restTemplate;

	@Value("${config.john-hopkins-url}")
	private String baseUrl;

	public JohnHopkinsStrategy() {
		restTemplate = RestServiceGenerator.GetInstance();
	}

	@Override
	public String GetActiveCount() {
		try {
			// Read the JSON file and calculate the India count.
			JohnHopkinResponse[] responses = getJohnHopkinResponses();
			float total = Arrays.stream(responses)
					// Keep only records for India.
					.filter(response -> response.getCountry().equalsIgnoreCase("India"))
					.map(response -> response.getStats().getConfirmed())
					.reduce(0f, Float::sum);
			return String.valueOf(Math.round(total));
		} catch (Exception e) {
			// Log the error if the file cannot be read.
			logger.error("Error occurred while fetching active count from John Hopkins API", e);
			throw new IllegalStateException("Unable to read John Hopkins statistics", e);
		}

	}

	private JohnHopkinResponse[] getJohnHopkinResponses() throws IOException {
		// Load the JSON file from the application resources.
		ObjectMapper objectMapper = new ObjectMapper();
		ClassPathResource resource = new ClassPathResource("JohnHopkins.json");
		try (InputStream inputStream = resource.getInputStream()) {
			return new JohnHopkinResponse[] { objectMapper.readValue(inputStream, JohnHopkinResponse.class) };
		}
	}
}
