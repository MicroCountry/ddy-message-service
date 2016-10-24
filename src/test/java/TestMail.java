import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ddy.message.service.EmailService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/spring-model.xml"})
public class TestMail {
	
	@Resource
	private EmailService emailService;
	@Test
	public void testSend(){
		List<String> array = new ArrayList<String>();
		array.add("1570424984@qq.com");
		//发送邮件
		System.out.println(this.emailService.sendEmail(array, "测试", "收到请回复", null));
		
		//发送附件版本1
		/*List<String> affixNames = new ArrayList<String>();
		affixNames.add("附件1");
		List<byte[]> files = new ArrayList<byte[]>();
		byte[] e={'a','b'};
		files.add(e);
		System.out.println(this.emailService.sendEmailWithAffix("1570424984@qq.com", "测试附件", "收到请回复",  new ArrayList<String>(), affixNames, files));*/
		
		/*List<String> affixNames = new ArrayList<String>();
		affixNames.add("att2.xml");
		List<String> files = new ArrayList<String>();
		files.add("D:\\vote.html");
		System.out.println(this.emailService.sendEmailWhithFile("1570424984@qq.com", "测试附件2", "收到请回复",  new ArrayList<String>(), affixNames, files));*/
		
		/*StringBuilder sb = new StringBuilder();
		String startHtml = "<html>";
		String endHtml = "</html>";
		String header =  " <head>                                                                         "
						+"     <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />      "
						+"     <title>Demystifying Email Design</title>                                   "
						+"     <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>    "
						+" </head>                                                                        ";
		String startBody = " <body>";
		String endBody   = " </body>";
		System.out.println(this.emailService.sendEmail("1570424984@qq.com", "测试", sb.toString(), null));*/
	}
}
