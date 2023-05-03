package com.example.springsecuritywithjwt.authentication;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PersonRepository {

	Person findByCondition(Person person);
}
