package com.js.leavejisu;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)	// "이 테스트는 스프링과 함께 실행하겠다"라고 스프링에게 알림
@ContextConfiguration(locations= {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})	// 테스트 필요한 설정 파일은 root-context.xml 이다
public class DataSourceTest {	// DB 연결 테스트
	@Autowired
	DataSource ds;	// root-context.xml에 등록한 dataSource 빈을 필드에 자동주입
	
	@Test
	public void testConnection() throws Exception {
		Connection conn = ds.getConnection();
		System.out.println("DB에 접속됨 : " + (conn != null));
		
		conn.close();
	}
}
