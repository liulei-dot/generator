package com.atguigu.test;

import com.atguigu.dao.EmployeeMapper;
import com.atguigu.domain.Employee;
import com.atguigu.domain.EmployeeExample;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Liulei
 * @create 2020-08-24 15:54
 */
public class generator {
    @Test
    public void test() throws Exception{
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        File configFile = new File("generator.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                callback, warnings);
        myBatisGenerator.generate(null);
    }

    @Test
    public void testMyBatis3() throws IOException{
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(ClassLoader.getSystemClassLoader().getResourceAsStream("mybatis-config.xml"));

        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            //xxxExample就是封装查询条件的
            //1、查询所有
            //List<Employee> emps = mapper.selectByExample(null);
            //2、查询员工名字中有e字母的，和员工性别是1的
            //封装员工查询条件的example
            EmployeeExample example = new EmployeeExample();
            //3、创建一个Criteria，这个Criteria就是拼装查询条件（条件为：last_name含有“三”并且性别为男，或email含有“三”）
            //select id, last_name, email, gender, d_id from tbl_employee
            //WHERE ( last_name like ? and gender = ? ) or email like ?
            EmployeeExample.Criteria criteria = example.createCriteria();
            criteria.andLastNameLike("%三%");
            criteria.andGenderEqualTo("1");

            EmployeeExample.Criteria criteria2 = example.createCriteria();
            criteria2.andEmailLike("%三%");  //用于封装“or”条件
            example.or(criteria2);

            List<Employee> list = mapper.selectByExample(example);
            for (Employee employee : list) {
                System.out.println(employee.getId());
            }

        }finally{
            openSession.close();
        }
    }
}
