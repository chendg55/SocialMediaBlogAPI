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
     * Message text must not be blank
     * Message text must not be longer than 255 characters
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
        return messageDAO.getAllMessages();
    }

    /**
     * Use the messageDAO to retrieve message by id
     * @param id message id
     * @return message
     */
    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    /**
     * Use the messageDAO to delete message by id
     * @param id message id
     * @return deleted message
     */
    public Message deleteMessageById(int id) {
        Message message = messageDAO.getMessageById(id);
        if (message == null)
            return null;
        else {
            messageDAO.deleteMessageById(id);
            return message;
        }
    }

    /**
     * Use the messageDAO to update a message by id
     * Message id must already exist
     * Message text must not be blank
     * Message text must not be longer than 255 characters
     * @param id message id
     * @param message message to be updated
     * @return updated message
     */
    public Message updateMessageById(int id, String newMessageText) {
        if (newMessageText.isBlank())
            return null;
        if (newMessageText.length() > 255)
            return null;
        if (messageDAO.getMessageById(id) == null)
            return null;
        else return messageDAO.updateMessageById(id, newMessageText);
    }

    /**
     * Use the messageDAO to retrieve all messages of a user by user id
     * @param id user id
     * @return all messages of user
     */
    public List<Message> getAllMessagesByUserId(int id) {
        return messageDAO.getAllMessagesByUserId(id);
    }
}
