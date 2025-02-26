package com.aryan.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aryan.entity.UrlData;

public interface UrlDataRepo extends JpaRepository<UrlData, String>{

}
