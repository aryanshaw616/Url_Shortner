package com.aryan.service;

import com.aryan.entity.UrlData;
import com.aryan.repo.UrlDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AppServiceImpl implements AppService {

	@Autowired
	private UrlDataRepo urlDataRepo;

	@Value("${app.base-url}")
	private String baseUrl;

	@Override
	public String saveUrl(String url) throws Exception {
		if (url.isEmpty()) throw new Exception("Empty link! Please enter a link");

		if (!url.startsWith("https://")) {
			url = "https://" + url;
		}

		String unique = unique();
		while (urlDataRepo.existsById(unique)) {
			unique = unique();
		}

		UrlData data = new UrlData();
		data.setShortUrl(unique);
		data.setOriginalUrl(url);
		urlDataRepo.save(data);

		return "https://urlshortner-production-07b3.up.railway.app/tiny/" + unique;
	}

	@Override
	public UrlData getOriginalUrl(String shortUrl) throws Exception {
		Optional<UrlData> optUrlData = urlDataRepo.findById(shortUrl);

		if (optUrlData.isPresent()) {
			return optUrlData.get();
		} else {
			throw new Exception("Enter a valid url!");
		}
	}

	// Method to generate unique string
	private String unique() {
		Random r = new Random();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 6; ++i) {
			int choice = r.nextInt(2);
			if (choice == 0) {
				choice = r.nextInt(2);
				int index = r.nextInt(26);
				if (choice == 0) {
					sb.append((char) ('a' + index));
				} else {
					sb.append((char) ('A' + index));
				}
			} else {
				int num = r.nextInt(10);
				sb.append(num);
			}
		}
		return sb.toString();
	}
}
