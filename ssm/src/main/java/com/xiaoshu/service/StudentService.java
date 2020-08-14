package com.xiaoshu.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.CourseMapper;
import com.xiaoshu.dao.StudentMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Course;
import com.xiaoshu.entity.Log;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentVo;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class StudentService {

	@Autowired
	StudentMapper studentMapper;

	@Autowired
	CourseMapper courseMapper;

	public PageInfo<StudentVo> findList(StudentVo studentVo, Integer pageNum, Integer pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		List<StudentVo> list=studentMapper.findList(studentVo);
		return new PageInfo<>(list);
	}

	// 通过用户名判断是否存在，（新增时不能重名）
	public Student existUserWithUserName(String name) throws Exception {
		
		List<Student> userList = studentMapper.selectByName(name);
		return userList.isEmpty()?null:userList.get(0);
	};

	public void updateStudent(Student student) {
		// TODO Auto-generated method stub
		studentMapper.updateByPrimaryKeySelective(student);
	}


	public void addStudent(Student student) {
		// TODO Auto-generated method stub
		studentMapper.insert(student);	
	}

	public List<Course> findAll() {
		

		return  courseMapper.selectAll();
	}

	public List<StudentVo> findLog(StudentVo studentVo) {
		// TODO Auto-generated method stub
		return studentMapper.findList(studentVo);
	}

	public void importStudent(MultipartFile studentFile) throws InvalidFormatException, IOException  {
		Workbook workbook = WorkbookFactory.create(studentFile.getInputStream());
		Sheet sheet = workbook.getSheetAt(0);
		int lastRowNum = sheet.getLastRowNum();
		for (int i = 0; i <lastRowNum; i++) {
			Row row = sheet.getRow(i+1);
			
			String code = row.getCell(0).toString();
			String name = row.getCell(1).toString();
			String age = row.getCell(2).toString();
			String sname = row.getCell(3).toString();
			String grade = row.getCell(4).toString();
			Date entrytime = row.getCell(5).getDateCellValue();
			
			
			
			if(!sname.equals("全栈")){
				continue;
			}
			
			Student  s=new Student();
			s.setCode(code);
			s.setName(name);
			s.setAge(age);
						
			s.setGrade(grade);			
			s.setEntrytime(entrytime);
			
			
			int cid=findCourse(sname);
			/*Course  param=new Course();
			param.setName(sname);
			Course course = courseMapper.selectOne(param);*/
			
			s.setCourseId(cid);
			s.setCreatetime(new Date());
			studentMapper.insert(s);
			
		}
		
	}

	private int findCourse(String sname) {
		// TODO Auto-generated method stub
		Course  param=new Course();
		param.setName(sname);
		Course course = courseMapper.selectOne(param);
		if(course==null){
			param.setCreatetime(new Date());
			param.setCode("q1");
			
			courseMapper.insertCourse(param);
			course=param;
			
		}	
		return course.getId();
	}

	public List<StudentVo> countStudent() {
		// TODO Auto-generated method stub
		return studentMapper.countStudent();
	}

	



}
