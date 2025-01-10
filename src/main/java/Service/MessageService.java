package Service;

import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }
    
    /**
     * Use the messageDAO to create a new message
     * Message must not be blank
     * Message must not be longer than 255 characters
     * Account posting the message must exist
     * @param message a message object
     * @return message if it was successfully persisted, null if it was not successfully persisted
     */
    public Message createNewMessage(Message message) {
        if (message.getMessage_text().isBlank())
            return null;
        if (message.getMessage_text().length() > 255)
            return null;
        if (accountDAO.getAccountById(message.getPosted_by()) == null)
            return null;
        else return messageDAO.newMessage(message);
    }

    /**
     * Use the messageDAO to retrieve all messages
     * @return all messages
     */
    public List<Message> getAllMessages() {
        return this.messageDAO.getAllMessages();
    }
}
