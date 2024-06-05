package com.poscodx.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poscodx.mysite.repository.GuestbookRepository;
import com.poscodx.mysite.vo.GuestbookVo;

@Service
public class GuestbookService {
	
	@Autowired
	private GuestbookRepository guestbookRepository;

	public List<GuestbookVo> getContentsList() {
		return guestbookRepository.findAll();
	}
	
	public void deleteContents(Long no, String password) { 
		guestbookRepository.deleteByNoAndPassword(no, password);
	}
	
	public void addContents(GuestbookVo vo) {
		// insert 후 pk 세팅 테스트
		System.out.println(vo);
		guestbookRepository.insert(vo);
		System.out.println(vo);
	}
	
	
}
