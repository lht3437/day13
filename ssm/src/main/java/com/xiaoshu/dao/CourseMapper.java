package com.xiaoshu.dao;

import java.util.List;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.Course;

public interface CourseMapper extends BaseMapper<Course> {



	void insertCourse(Course param);


   
}