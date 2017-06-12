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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.lider.core.api.mail.IMailService;
import tr.org.liderahenk.lider.core.api.messaging.messages.ITaskStatusMessage;
import tr.org.liderahenk.lider.core.api.persistence.dao.IMailAddressDao;
import tr.org.liderahenk.lider.core.api.persistence.entities.ICommandExecutionResult;
import tr.org.liderahenk.lider.core.api.persistence.entities.IMailAddress;
import tr.org.liderahenk.lider.core.api.persistence.entities.ITask;
import tr.org.liderahenk.lider.core.api.plugin.ITaskAwareCommand;

/**
 * Plugin notifier implementation for {@link EventHandler}. This class is
 * responsible for notifying all classes which implements
 * {@link ITaskAwareCommand} interface.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class PluginNotifier implements EventHandler {

	private static Logger logger = LoggerFactory.getLogger(PluginNotifier.class);
	
	private IMailService mailService;
	private IMailAddressDao mailAddressDao;

	/**
	 * A map to store all available commands. Key format of the key is as
	 * follows:<br/>
	 * {PLUGIN_NAME}:{PLUGIN_VERSION}:{COMMAND_ID}
	 */
	private HashMap<String, ITaskAwareCommand> subscribers;

	/**
	 * 
	 * @param command
	 */
	public void bindCommand(ITaskAwareCommand command) {
		if (subscribers == null) {
			subscribers = new HashMap<String, ITaskAwareCommand>();
		}
		String key = buildKey(command.getPluginName(), command.getPluginVersion(), command.getCommandId());
		subscribers.put(key, command);
		logger.info("Registered command: {}", key);
	}

	/**
	 * 
	 * @param command
	 */
	public void unbindCommand(ITaskAwareCommand command) {
		if (subscribers == null)
			return;
		String key = buildKey(command.getPluginName(), command.getPluginVersion(), command.getCommandId());
		subscribers.remove(key);
		logger.info("Unregistered command: {}", key);
	}

	/**
	 * Builds key string from provided parameters. Key format is as follows:
	 * <br/>
	 * {PLUGIN_NAME}:{PLUGIN_VERSION}:{COMMAND_ID}
	 * 
	 * @param pluginName
	 * @param pluginVersion
	 * @param commandId
	 * @return
	 */
	public String buildKey(String pluginName, String pluginVersion, String commandId) {
		StringBuilder key = new StringBuilder();
		key.append(pluginName).append(":").append(pluginVersion).append(":").append(commandId);
		return key.toString().toUpperCase(Locale.ENGLISH);
	}

	@Override
	public void handleEvent(Event event) {
		logger.debug("Started handling task status.");

		ITaskStatusMessage message = (ITaskStatusMessage) event.getProperty("message");
		ICommandExecutionResult result = (ICommandExecutionResult) event.getProperty("result");

		logger.info("Sending task status message to plugins. Task: {} Status: {}",
				new Object[] { message.getTaskId(), message.getResponseCode() });

		ITask task = result.getCommandExecution().getCommand().getTask();
		String key = buildKey(task.getPlugin().getName(), task.getPlugin().getVersion(), task.getCommandClsId());
		
		byte[] data = result.getResponseData();
		
		
		try {
			Map<String, Object> responseData = new ObjectMapper().readValue(data, 0, data.length,new TypeReference<HashMap<String, Object>>() {
					});
			
			if(responseData!=null){
				
			Boolean mail_send = (Boolean) responseData.get("mail_send");
			
			if(mail_send!=null && mail_send){

					String mail_subject = (String) responseData.get("mail_subject");
					String mail_content = (String) responseData.get("mail_content");

					if (mail_subject != null && mail_content != null) {

						List<? extends IMailAddress> mailAddressList = getMailAddressDao().findByProperty(IMailAddress.class, "plugin.id", task.getPlugin().getId(), 0);

						List<String> toList = new ArrayList<String>();
						for (IMailAddress iMailAddress : mailAddressList) {
							toList.add(iMailAddress.getMailAddress());
						}
						if (toList.size() > 0)
							getMailService().sendMail(toList, mail_subject, mail_content);

					}
				}
			}

			ITaskAwareCommand subscriber = subscribers != null ? subscribers.get(key.toUpperCase(Locale.ENGLISH)) : null;
			if (subscriber != null) {
				try {
					subscriber.onTaskUpdate(result);
					logger.debug("Notified subscriber: {} with task status: {}", new Object[] { subscriber, message });
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
				}
			}

			logger.info("Handled task status.");
			
			
			
		} catch (JsonParseException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	
	}

	public IMailService getMailService() {
		return mailService;
	}

	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}

	public IMailAddressDao getMailAddressDao() {
		return mailAddressDao;
	}

	public void setMailAddressDao(IMailAddressDao mailAddressDao) {
		this.mailAddressDao = mailAddressDao;
	}

}
