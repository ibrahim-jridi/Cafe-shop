package utils;

import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailUtils {
	@Autowired
	private JavaMailSender emailSend;
	public void sendMail(String to,String subject,String text,List<String> list) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom("xhunter2019@gmail.com");
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(text);
		if (list != null && list.size() > 0) {
			msg.setCc(getCcArray(list));
		}
		emailSend.send(msg);
	}
	private String[] getCcArray(List<String>ccList) {
		String[] cc =new String[ccList.size()];
		for (int i = 0; i < ccList.size(); i++) {
			cc[i]=ccList.get(i);
		}
		return cc;
	}

	public void forgetMail(String to,String subject,String password)throws MessagingException {
		MimeMessage msg = emailSend.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg,true);
		helper.setFrom("xhunter2019@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		String Msg = "<p><b>Your Login details are :  </b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click to login</a></p>";
		msg.setContent(Msg,"text/html");
		emailSend.send(msg);
	}

}
