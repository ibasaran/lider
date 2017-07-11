/*
*
*    Copyright © 2015-2016 Tübitak ULAKBIM
*
*    This file is part of Lider Ahenk.
*
*    Lider Ahenk is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    Lider Ahenk is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with Lider Ahenk.  If not, see <http://www.gnu.org/licenses/>.
*/
package tr.org.liderahenk.lider.taskmanager.notifiers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.lider.core.api.configuration.IConfigurationService;
import tr.org.liderahenk.lider.core.api.mail.IMailService;
import tr.org.liderahenk.lider.core.api.messaging.enums.StatusCode;
import tr.org.liderahenk.lider.core.api.persistence.dao.ICommandDao;
import tr.org.liderahenk.lider.core.api.persistence.dao.IMailAddressDao;
import tr.org.liderahenk.lider.core.api.persistence.entities.ICommand;
import tr.org.liderahenk.lider.core.api.persistence.entities.ICommandExecution;
import tr.org.liderahenk.lider.core.api.persistence.entities.ICommandExecutionResult;
import tr.org.liderahenk.lider.core.api.persistence.entities.IMailAddress;
import tr.org.liderahenk.lider.core.api.persistence.factories.IEntityFactory;

/**
 *
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class MailNotifier {

	private static Logger logger = LoggerFactory.getLogger(MailNotifier.class);

	private IMailService mailService;
	private IMailAddressDao mailAddressDao;
	private ICommandDao commandDao;
	private IConfigurationService configurationService;
	private IEntityFactory entityFactory;
	private Timer timer;

	private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy H:m");

	public void init() {
		logger.info("Initializing mail notifier.");
		hookListener();
	}

	public void destroy() {
		logger.info("Destroying mail notifier...");
		if (timer != null) {
			timer.cancel();
			timer.purge();
		}
	}

	protected class TaskCompletionListener extends TimerTask {
		@Override
		public void run() {
			// No mail were sent for these commands:
			List<? extends ICommand> commands = commandDao.findTaskCommandsWithMailNotification();
			if (commands != null && commands.size() > 0) {
				// We'll send only ONE mail for each task!
				for (ICommand command : commands) {
					try {
						// Build mail to_list
						List<? extends IMailAddress> mailAddressList = mailAddressDao.findByProperty(IMailAddress.class,
								"plugin.id", command.getTask().getPlugin().getId(), 0);
						List<String> toList = new ArrayList<String>();
						for (IMailAddress iMailAddress : mailAddressList) {
							toList.add(iMailAddress.getMailAddress());
						}

						// Get mail_subject
						String mailSubject = getMailSubject(command);
						StringBuilder mailContent = new StringBuilder();

						int totalAgents = command.getUidList().size();
						int totalAccessibleAgents = command.getCommandExecutions().size();

						mailContent.append(command.getTask().getPlugin().getDescription()).append(" eklentisi ")
								.append(format.format(command.getCreateDate())).append(" tarihinde ")
								.append(command.getTask().getCommandClsId()).append(" görevi göndermiştir. \n")
								.append("Görev toplam ").append(totalAgents)
								.append(" adet istemci için çalıştırılmıştır. \n").append("Görev toplam ")
								.append(totalAccessibleAgents).append(" adet istemciye ulaşmıştır. \n")
								.append("Görev toplam ").append(totalAgents - totalAccessibleAgents)
								.append(" adet istemciye ulaşmamıştır. \n")
								.append("Görev sonuçlarına ilişkin detayları aşağıda inceleyebilirsiniz: \n\n");

						if (toList.size() > 0) {
							for (ICommandExecution execution : command.getCommandExecutions()) {
								for (ICommandExecutionResult result : execution.getCommandExecutionResults()) {
									if (StatusCode.getTaskEndingStates().contains(result.getResponseCode())
											&& result.getMailContent() != null && !result.getMailContent().isEmpty()) {
										mailContent.append("\nAhenk: ").append(execution.getUid()).append(", Sonuç: ")
												.append(result.getResponseCode().toString()).append(", Mesaj:")
												.append(result.getMailContent());
										break;
									}
								}
							}

							// Send mail
							mailService.sendMail(toList, mailSubject, mailContent.toString());

							// Mark command as 'sent mail'
							// So that the notifier may ignore it from now on.
							commandDao.update(entityFactory.createCommand(command, true));
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		}

		private String getMailSubject(ICommand command) {
			// TODO instead of returning first 'mail subject' of a task
			// result, a better solution would be to get the subject from
			// plugin.
			String subject = command.getCommandExecutions().get(0).getCommandExecutionResults().get(0).getMailSubject();
			return subject != null && !subject.isEmpty() ? subject : "Lider Ahenk Görev Sonucu";
		}
	}

	private void hookListener() {
		if (configurationService.getMailSendOnTaskCompletion()) {
			timer = new Timer();
			timer.schedule(new TaskCompletionListener(), 1000, configurationService.getMailCheckTaskCompletionPeriod());
		}
	}

	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}

	public void setMailAddressDao(IMailAddressDao mailAddressDao) {
		this.mailAddressDao = mailAddressDao;
	}

	public void setCommandDao(ICommandDao commandDao) {
		this.commandDao = commandDao;
	}

	public void setConfigurationService(IConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public void setEntityFactory(IEntityFactory entityFactory) {
		this.entityFactory = entityFactory;
	}

}
