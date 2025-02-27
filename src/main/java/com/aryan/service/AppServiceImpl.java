package com.aryan.service;

import com.aryan.entity.UrlData;
import com.aryan.repo.UrlDataRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AppServiceImpl implements AppService {

	private final UrlDataRepo urlDataRepo;

	@Value("${app.base-url}")
	private String baseUrl;

	public AppServiceImpl(UrlDataRepo urlDataRepo) {
		this.urlDataRepo = urlDataRepo;
	}

	@Override
	public String saveUrl(String url) throws Exception {
		if (url.isEmpty()) throw new Exception("Empty link! Please enter a link");

		if (!url.startsWith("https://")) {
			url = "https://" + url;
		}

		String unique;
		do {
			unique = unique();
		} while (urlDataRepo.existsById(unique));

		UrlData data = new UrlData();
		data.setShortUrl(unique);
		data.setOriginalUrl(url);
		urlDataRepo.save(data);

		return "https://urlshortner-production-07b3.up.railway.app/tiny/" + unique;
	}

	@Override
	public UrlData getOriginalUrl(String shortUrl) throws Exception {
		return urlDataRepo.findById(shortUrl)
				.orElseThrow(() -> new Exception("Enter a valid url!"));
	}

	// Method to generate unique string
	private String unique() {
		Random r = new Random();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 6; ++i) {
			if (r.nextBoolean()) {
				sb.append((char) ('A' + r.nextInt(26)));
			} else {
				sb.append(r.nextInt(10));
			}
		}
		return sb.toString();
	}
}
