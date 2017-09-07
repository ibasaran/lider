package tr.org.liderahenk.lider.taskmanager.notifiers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.lider.core.api.i18n.ILocaleService;
import tr.org.liderahenk.lider.core.api.mail.IMailService;
import tr.org.liderahenk.lider.core.api.messaging.enums.StatusCode;
import tr.org.liderahenk.lider.core.api.messaging.messages.ITaskStatusMessage;
import tr.org.liderahenk.lider.core.api.persistence.dao.ICommandDao;
import tr.org.liderahenk.lider.core.api.persistence.dao.IMailAddressDao;
import tr.org.liderahenk.lider.core.api.persistence.dao.IMailContentDao;
import tr.org.liderahenk.lider.core.api.persistence.entities.ICommand;
import tr.org.liderahenk.lider.core.api.persistence.entities.ICommandExecution;
import tr.org.liderahenk.lider.core.api.persistence.entities.ICommandExecutionResult;
import tr.org.liderahenk.lider.core.api.persistence.entities.IMailAddress;
import tr.org.liderahenk.lider.core.api.persistence.entities.IMailContent;
import tr.org.liderahenk.lider.core.api.persistence.entities.ITask;

public class MailManagerNotifier implements EventHandler {

	private static Logger logger = LoggerFactory.getLogger(MailManagerNotifier.class);

	private ScheduledThreadPoolExecutor threadExecutor;

	public static int SEND_MAIL_DIRECTLY = 0;
	public static int SEND_MAIL_WITH_SCHEDULER = 1;
	public static int SEND_MAIL_WITH_AGENT_TIMER = 2;

	public static int MINUTES = 0;
	public static int HOURS = 1;
	public static int DAYS = 2;

	private ICommandDao commandDao;
	private IMailAddressDao mailAddressDao;
	private IMailContentDao mailContentDao;
	private IMailService mailService;
	private ILocaleService localeService;

	private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy H:m");

	@Override
	public void handleEvent(Event event) {

		logger.info("Started handling mail sending.");

		ITaskStatusMessage message = (ITaskStatusMessage) event.getProperty("message");
		ICommandExecutionResult commanExecutionResult = (ICommandExecutionResult) event.getProperty("result");

		
		if(commanExecutionResult!=null){
		
		ITask task = commanExecutionResult.getCommandExecution().getCommand().getTask();

		long pluginId = task.getPlugin().getId();

		IMailContent mailConfiguration = getMailSendingStrategy(pluginId);

		logger.info("Mail Send Strategy for plugin {} is {}", new Object[] { task.getPlugin().getDescription(),
				mailConfiguration.getMailSendStartegy() == 0 ? "" : "" });

		if(mailConfiguration!=null){
			
			switch (mailConfiguration.getMailSendStartegy()) {
			case 0: // SEND_MAIL_DIRECTLY
				sendMailDirectly(commanExecutionResult, mailConfiguration);
				break;
	
			case 1: // SEND_MAIL_WITH_SCHEDULER
	
				sendMailWithScheduler(commanExecutionResult, mailConfiguration);
	
				break;
	
			case 2:// SEND_MAIL_WITH_AGENT_TIMER No NEED
	
				sendMailWithAgentTimer(commanExecutionResult, mailConfiguration);
				break;
	
			default:
				break;
			}
		}
		}
	}

	private void sendMailWithAgentTimer(ICommandExecutionResult commanExecutionResult, IMailContent mailConfiguration) {

	}

	private void sendMailWithScheduler(ICommandExecutionResult commanExecutionResult, IMailContent mailConfiguration) {

		long period = Long.parseLong(mailConfiguration.getMailSchdTimePeriod());
		int mailTimePeriodType = mailConfiguration.getMailSchdTimePeriodType();
		TimeUnit type = null;

		switch (mailTimePeriodType) {
		case 0: // MINUTES
			type = TimeUnit.MINUTES;
			break;
		case 1: // HOURS
			type = TimeUnit.HOURS;
			break;
		case 2: // DAYS
			type = TimeUnit.DAYS;
			break;

		default:
			break;
		}

		if (threadExecutor == null) {
			threadExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1000);
		}

		ICommand command = commanExecutionResult.getCommandExecution().getCommand();

		if (!command.isMailThreadingActive()) {

			threadExecutor.scheduleAtFixedRate(new MailScheduler(command, threadExecutor), 0, period, type);
		}

	}
	
	/*
	 * if mail have been not sending, ahenk mail content must be "";
	 */
	private void sendMailDirectly(ICommandExecutionResult commanExecutionResult, IMailContent mailConfiguration) {
		
		List<String> toList = getMailToList(commanExecutionResult.getCommandExecution().getCommand());

		String mailContent=commanExecutionResult.getMailContent();
		
		String mailSubject=commanExecutionResult.getMailSubject();
		
		
		if(mailSubject!=null){
			mailSubject = "Lider Ahenk Görev Sonucu " + mailSubject;
		}
		
		if(mailContent!=null && !"".equals(mailContent)){
			ICommand  command=commanExecutionResult.getCommandExecution().getCommand();
			
			List<? extends ICommandExecution> ceList = command.getCommandExecutions();

			List<ICommandExecutionResult> cerList = new ArrayList<ICommandExecutionResult>();

			for (ICommandExecution iCommandExecution : ceList) {
				List<? extends ICommandExecutionResult> commandExecutionResultList = iCommandExecution.getCommandExecutionResults();
				if (commandExecutionResultList != null && commandExecutionResultList.size() > 0) {
					cerList.add(commandExecutionResultList.get(0));
				}
			}
			StringBuilder mailContentBuilder= new StringBuilder();
			
			mailContentBuilder.append(command.getTask().getPlugin().getDescription()).append(" eklentisi ")
			.append(format.format(command.getCreateDate())).append(" tarihinde ")
			.append(command.getTask().getCommandClsId()).append(" görevi göndermiştir. \n").append("Görev toplam ")
			.append(ceList.size())
			.append(" adet istemci için çalıştırılmıştır. " + "\nGörev toplam " + cerList.size()
					+ "adet istemciye ulaşmıştır. " + "\nGörev toplam " + (ceList.size() - cerList.size())
					+ " adet istemciye ulaşmamıştır.\nGörev sonuçlarına ilişkin detayları aşağıda inceleyebilirsiniz: \n\n");
			
			mailContent= mailContentBuilder.toString()+ mailContent;
		}
		
		if(toList.size()>0 && mailContent!=null && !"".equals(mailContent) && !mailContent.isEmpty()){
			
			mailService.sendMail(toList, mailSubject, mailContent);
		}
	
	}

	// IMailContent hold mail configuration information
	private IMailContent getMailSendingStrategy(long pluginId) {

		List<? extends IMailContent> list = mailContentDao.findByProperty(IMailContent.class, "plugin.id", pluginId, 0);

		IMailContent mailContent = null;
		if (list.size() > 0) {
			mailContent = list.get(list.size()-1);
		}
		return mailContent;

	}

	class MailScheduler implements Runnable {

		ICommand command;
		ScheduledThreadPoolExecutor threadExecutor;

		public MailScheduler(tr.org.liderahenk.lider.core.api.persistence.entities.ICommand command,
				ScheduledThreadPoolExecutor threadExecutor) {
			this.command = command;
			this.threadExecutor = threadExecutor;
		}

		@Override
		public void run() {

			command = commandDao.find(command.getId()); // find new execution result

			List<? extends ICommandExecution> ceList = command.getCommandExecutions();

			List<ICommandExecutionResult> cerList = new ArrayList<ICommandExecutionResult>();

			for (ICommandExecution iCommandExecution : ceList) {
				List<? extends ICommandExecutionResult> commandExecutionResultList = iCommandExecution.getCommandExecutionResults();
				if (commandExecutionResultList != null && commandExecutionResultList.size() > 0) {
					cerList.add(commandExecutionResultList.get(0));
				}
			}

			if (ceList.size() == cerList.size()) {
				this.threadExecutor.shutdown();
			}

			createAndSendMail(ceList, cerList, command);

			// flag to threading is starting
			command.setMailThreadingActive(true);

			try {
				commandDao.update(command);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public ICommandDao getCommandDao() {
		return commandDao;
	}

	public void setCommandDao(ICommandDao commandDao) {
		this.commandDao = commandDao;
	}

	public IMailAddressDao getMailAddressDao() {
		return mailAddressDao;
	}

	public void setMailAddressDao(IMailAddressDao mailAddressDao) {
		this.mailAddressDao = mailAddressDao;
	}

	public IMailContentDao getMailContentDao() {
		return mailContentDao;
	}

	public void setMailContentDao(IMailContentDao mailContentDao) {
		this.mailContentDao = mailContentDao;
	}

	public IMailService getMailService() {
		return mailService;
	}

	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}

	public ILocaleService getLocaleService() {
		return localeService;
	}

	public void setLocaleService(ILocaleService localeService) {
		this.localeService = localeService;
	}

	private void createAndSendMail(List<? extends ICommandExecution> ceList, List<ICommandExecutionResult> cerList,
			ICommand command) {
		// Build mail to_list
		List<String> toList = getMailToList(command);

		String mailSubject = "";
		StringBuilder mailContent = new StringBuilder();
		boolean hasContent = false;

		mailContent.append(command.getTask().getPlugin().getDescription()).append(" eklentisi ")
				.append(format.format(command.getCreateDate())).append(" tarihinde ")
				.append(command.getTask().getCommandClsId()).append(" görevi göndermiştir. \n").append("Görev toplam ")
				.append(ceList.size())
				.append(" adet istemci için çalıştırılmıştır. " + "\nGörev toplam " + cerList.size()
						+ "adet istemciye ulaşmıştır. " + "\nGörev toplam " + (ceList.size() - cerList.size())
						+ " adet istemciye ulaşmamıştır.\nGörev sonuçlarına ilişkin detayları aşağıda inceleyebilirsiniz: \n\n");

		if (toList.size() > 0) {
			for (ICommandExecution execution : command.getCommandExecutions()) {

				for (ICommandExecutionResult result : execution.getCommandExecutionResults()) {
					
					if (mailSubject.isEmpty() && result.getMailSubject() != null	&& !result.getMailSubject().isEmpty()) {

						mailSubject = result.getMailSubject();
					}
					
					if (StatusCode.getTaskEndingStates().contains(result.getResponseCode())
							&& result.getMailContent() != null && !result.getMailContent().trim().isEmpty()) {
						hasContent = true;
						mailContent.append("\nAhenk: ").append(execution.getUid()).append(", Sonuç: ")
								.append(localeService.getString(result.getResponseCode().toString())).append(", Mesaj:")
								.append(result.getMailContent());
						break;
					}
				}
			}

			// Send mail
			if (hasContent) {
				logger.debug("Sending mail notification.");
				mailSubject = "Lider Ahenk Görev Sonucu " + mailSubject;
				String body = mailContent.toString();
				logger.debug("Task mail content: {}", body);
				mailService.sendMail(toList, mailSubject, body);
			}

		}
	}

	private List<String> getMailToList(ICommand command) {
		List<? extends IMailAddress> mailAddressList = mailAddressDao.findByProperty(IMailAddress.class, "plugin.id",
				command.getTask().getPlugin().getId(), 0);
		List<String> toList = new ArrayList<String>();
		for (IMailAddress iMailAddress : mailAddressList) {
			toList.add(iMailAddress.getMailAddress());
		}
		return toList;
	}

}
