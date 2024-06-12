package com.poscodx.mysite.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.poscodx.mysite.repository.SiteRepository;
import com.poscodx.mysite.vo.SiteVo;

@Service
public class SiteService {
	private static String SAVE_PATH = "/users/songchanhwan/fileupload-files";
	private static String URL_PATH = "/images";

	private SiteRepository siteRepository;
	private ServletContext servletContext;

	public SiteService(SiteRepository siteRepository, ServletContext servletContext) {
		this.siteRepository = siteRepository;
		this.servletContext = servletContext;
	}

	public SiteVo getSite() {
		return siteRepository.find();
	}

	public void updateSite(SiteVo vo) {
		int result = siteRepository.updateSite(vo);
		if (result > 0) {	// 변경되었다면 ServletContext의 site 정보 갱신
			servletContext.setAttribute("siteVo", getSite());
		}
	}

	public String restore(MultipartFile file) {
		String url = null;

		try {
			File uploadDirectory = new File(SAVE_PATH);

			if (!uploadDirectory.exists()) {
				uploadDirectory.mkdirs();
			}

			if (file.isEmpty()) {
				return url;
			}

			String originFilename = file.getOriginalFilename();
			String extName = originFilename.substring(originFilename.lastIndexOf(".") + 1);
			String saveFilename = generateSaveFilename(extName);
			Long fileSize = file.getSize();

			byte[] data = file.getBytes();
			FileOutputStream os = new FileOutputStream(SAVE_PATH + "/" + saveFilename);
			os.write(data);
			os.close();

			url = URL_PATH + "/" + saveFilename;
		} catch (IOException e) {
			throw new RuntimeException();
		}

		return url;
	}

	private String generateSaveFilename(String extName) {
		String filename = "";

		Calendar calendar = Calendar.getInstance();
		filename += calendar.get(Calendar.YEAR);
		filename += calendar.get(Calendar.MONTH);
		filename += calendar.get(Calendar.DATE);
		filename += calendar.get(Calendar.HOUR);
		filename += calendar.get(Calendar.MINUTE);
		filename += calendar.get(Calendar.SECOND);
		filename += calendar.get(Calendar.MILLISECOND);
		filename += ("." + extName);

		return filename;
	}
}
