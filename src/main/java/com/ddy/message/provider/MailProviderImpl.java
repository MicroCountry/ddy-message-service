/**
 *
 */
package com.ddy.message.provider;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service; 
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import com.ddy.message.param.EMailMessage;
import com.ddy.message.param.EmailTarget;
import com.ddy.message.result.ResponseDTO;
import com.ddy.message.service.EmailService;

/**
 * @author wgm
 *
 */
@Service
public class MailProviderImpl implements MailProvider {

	private static final Logger logger = LoggerFactory.getLogger(MailProviderImpl.class);

	@Resource
	private EmailService emailService;

	@Value("${mail.switch.open}")
	private boolean isSwitchOpen;

	@Resource
	private TaskExecutor mailTaskExecutor;

	@Override
	public void sendEMail(final EMailMessage eMailMessage, final EmailTarget emailTarget, boolean isAsync,final boolean isAttachment,final Long delay,final TimeUnit unit) {
		checkSendMailParam(eMailMessage, emailTarget);
		try {
			Thread.sleep(unit.toMillis(delay));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!isMailSendingOpen()) {
			logger.info("current env mail sending is not opened!!!");
			return;
		}
	    emailService.sendEmail(eMailMessage,emailTarget,isAsync,isAttachment);
	}

	@Override
	public ResponseDTO<?> sendEmail(final List<String> receiver,final List<String> ccReceiver,final String subject,final String content,final Long delay,final TimeUnit unit) {
		try {
			Thread.sleep(unit.toMillis(delay));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return sendEmailHandle(receiver, ccReceiver, subject, content);
	}

	@Override
	public void sendEmailAsync(final List<String> receiver, final List<String> ccReceiver, final String subject, final String content,final Long delay,final TimeUnit unit) {
		try {
			Thread.sleep(unit.toMillis(delay));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			mailTaskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					sendEmailHandle(receiver, ccReceiver, subject, content);
				}
			});
		} catch (Throwable e) {
			logger.warn("", e);
		}
	}

	private ResponseDTO<?> sendEmailHandle(List<String> receiver,List<String> ccReceiver,String subject,String content){
		@SuppressWarnings("rawtypes")
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			boolean result = emailService.sendEmail(receiver, subject, content, ccReceiver);
			logger.info("Email send result is {}, to={}, subject={}", new Object[]{BooleanUtils.toString(result, "success", "failed"), receiver, subject});
		} catch (Exception e) {
			logger.error("Email to " + receiver + " exception", e);
			responseDTO.setStatus(false);
			responseDTO.setMsg(e.getMessage());
		}
		return responseDTO;
	}

	private void checkSendMailParam(EMailMessage eMailMessage, EmailTarget emailTarget) {
		if (eMailMessage == null || emailTarget == null) {
			throw new IllegalArgumentException("参数为null!");
		}
		if (StringUtils.isBlank(eMailMessage.getContent()) || CollectionUtils.isNotEmpty(emailTarget.getReceiver())) {
			throw new IllegalArgumentException("内容为空或没有收件人");
		}
	}

	private boolean isMailSendingOpen(){
		return isSwitchOpen;
	}

	@Override
	public ResponseDTO<?> sendEmailWithAffix(List<String> receiver, List<String> ccReceiver, String subject, String content,
			List<String> affixNames,List<byte[]> files,Long delay,TimeUnit unit) {
		return sendEmailWithAffixHandle(receiver, ccReceiver, subject, content, affixNames, files,delay,unit);
	}

	@Override
	public void sendEmailWithAffixAsync(final List<String> receiver,final List<String> ccReceiver,final String subject,
			final String content,final List<String> affixNames,final List<byte[]> files,final Long delay,final TimeUnit unit) {
		mailTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendEmailWithAffixHandle(receiver, ccReceiver, subject, content, affixNames, files,delay,unit);
			}
		});
	}

	private ResponseDTO<?> sendEmailWithAffixHandle(final List<String> receiver,final List<String> ccReceiver,final String subject, final String content,
			final List<String> affixNames,final List<byte[]> files,final Long delay,final TimeUnit unit) {
		try {
			Thread.sleep(unit.toMillis(delay));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		@SuppressWarnings("rawtypes")
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			boolean result = emailService.sendEmailWithAffix(receiver, subject, content, ccReceiver, affixNames, files);
			logger.info("Email send result is {}, to={}, subject={}", new Object[]{BooleanUtils.toString(result, "success", "failed"), receiver, subject});
		} catch (Exception e) {
			logger.error("Email to " + receiver + " exception", e);
			responseDTO.setStatus(false);
			responseDTO.setMsg(e.getMessage());
		}
		return responseDTO;
	}

	@Override
	public boolean sendEmailWhithFile(List<String> toList, String subject,
			String content, List<String> ccList, List<String> affixNames,
			List<String> filesPath,Long delay,TimeUnit unit) {
		try {
			Thread.sleep(unit.toMillis(delay));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return emailService.sendEmailWhithFile(toList, subject, content, ccList, affixNames, filesPath);
	}

	@Override
	public void sendEmailWhithFileAsync(final List<String> toList, final String subject,
			final String content, final List<String> ccList, final List<String> affixNames,
			final List<String> filesPath,final Long delay,final TimeUnit unit) {
		try {
			Thread.sleep(unit.toMillis(delay));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mailTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				 emailService.sendEmailWhithFile(toList, subject, content, ccList, affixNames, filesPath);
			}
		});
	}
}
