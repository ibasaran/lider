package tr.org.liderahenk.lider.core.api.messaging;

import java.util.List;

import tr.org.liderahenk.lider.core.api.messaging.messages.IExecutePoliciesMessage;
import tr.org.liderahenk.lider.core.api.messaging.messages.IExecuteScriptMessage;
import tr.org.liderahenk.lider.core.api.messaging.messages.IExecuteTaskMessage;
import tr.org.liderahenk.lider.core.api.messaging.messages.ILiderMessage;
import tr.org.liderahenk.lider.core.api.messaging.messages.IRequestFileMessage;
import tr.org.liderahenk.lider.core.api.persistence.entities.IProfile;
import tr.org.liderahenk.lider.core.api.persistence.entities.ITask;

/**
 * Factory interface to create {@link ILiderMessage}'s from various objects
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public interface IMessageFactory {

	/**
	 * Create task execution message from provided task instance.
	 * 
	 * @param task
	 * @param jid
	 * @return
	 */
	IExecuteTaskMessage createExecuteTaskMessage(ITask task, String jid);

	/**
	 * Create script execution message from provided file path and recipient.
	 * 
	 * @param filePath
	 *            script file path
	 * @param recipient
	 *            JID of recipient
	 * @return
	 */
	IExecuteScriptMessage createExecuteScriptMessage(String filePath, String recipient);

	/**
	 * Create file request message from provided file path and recipient.
	 * 
	 * @param filePath
	 *            script file path
	 * @param recipient
	 *            JID of recipient
	 * @return
	 */
	IRequestFileMessage createRequestFileMessage(String filePath, String recipient);

	/**
	 * Create execute policies message from provided (user and machine) profile
	 * lists.
	 * 
	 * @param recipient
	 * @param userPolicyProfiles
	 * @param userPolicyVersion
	 * @param userCommandExecutionId
	 * @param agentPolicyProfiles
	 * @param agentPolicyVersion
	 * @param agentCommandExecutionId
	 * @return
	 */
	IExecutePoliciesMessage createExecutePoliciesMessage(String recipient, List<IProfile> userPolicyProfiles,
			String userPolicyVersion, Long userCommandExecutionId, List<IProfile> agentPolicyProfiles,
			String agentPolicyVersion, Long agentCommandExecutionId);

}
