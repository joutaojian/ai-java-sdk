package net.xtion;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.xtion.common.AiManager;
import net.xtion.common.Auth;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.List;

@Slf4j
@SpringBootApplication
public class AiJavaSdkApplication {

	public static void main(String[] args) {

		Auth auth = Auth.create("http://127.0.0.1:10600","5b3cd57441ee10468d5419fd345d6547","33afc824dd29495faa8df17edb9f0c1v");
		HashMap<String,String> params = Maps.newHashMap();
		params.put("imageUrl","http://om6hqi8hn.bkt.clouddn.com/aiplat/2974cd1b298344feb48b87155aed6e9a.jpg");
		List list = Lists.newArrayList();
		list.add(params);
		String result = AiManager.get().getBusiness(auth,"",params);
		String result2 = AiManager.post().inputTask(auth,"",list);
		log.info("{}",result2);


	}
}
