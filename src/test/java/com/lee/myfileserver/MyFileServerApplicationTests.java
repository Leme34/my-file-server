package com.lee.myfileserver;

import com.lee.myfileserver.entity.File;
import com.lee.myfileserver.repository.FileRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyFileServerApplicationTests {

	@Autowired
	private FileRepository fileRepository;

	@Test
	public void contextLoads() {
	}

}
