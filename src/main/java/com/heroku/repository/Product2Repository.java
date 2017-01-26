/*
 * Created on 26 Jan 2017 ( Time 08:55:17 )
 * Generated by Telosys Tools Generator ( version 2.1.1 )
 */

package com.heroku.repository;

import com.heroku.entities.Product2 ;

import org.springframework.data.repository.PagingAndSortingRepository;



/**
 * Spring JPA Repository for Product2
 * 
 * @author Telosys Tools Generator
 *
 */
public interface Product2Repository extends PagingAndSortingRepository<Product2, Integer> {
		Product2 findBySfid(String sfid);


}