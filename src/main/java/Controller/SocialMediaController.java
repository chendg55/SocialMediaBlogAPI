package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postNewAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postNewMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserId);
        return app;
    }

    /**
     * Handler to post a new account.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into an Account object.
     * If AccountService returns a null Account (meaning posting an Account was unsuccessful), the API will return status code 400 (Client error)
     * If posting an Account was successful, the API will return status code 200 (OK)
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postNewAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAccount = accountService.registerNewAccount(account);
        if (newAccount != null) {
            ctx.json(mapper.writeValueAsString(newAccount));
            ctx.status(200);
        }
        else {
            ctx.status(400);
        }
    }

    /**
     * Handler for logging in
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into an Account object.
     * If AccountService returns a null Account (meaning logging in was unsuccessful), the API will return status code 401 (Unauthorized)
     * If logging in was successful, the API will return status code 200 (OK)
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account login = accountService.verifyLogin(account);
        if (login != null) {
            ctx.json(mapper.writeValueAsString(login));
            ctx.status(200);
        }
        else {
            ctx.status(401);
        }
    }

    /**
     * Handler to post a new message.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into a Message object.
     * If MessageService returns a null Message (meaning posting a Message was unsuccessful), the API will return status code 400 (Client error)
     * If posting a Message was successful, the API will return status code 200 (OK)
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postNewMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = messageService.createNewMessage(message);
        if (newMessage != null) {
            ctx.json(mapper.writeValueAsString(newMessage));
            ctx.status(200);
        }
        else {
            ctx.status(400);
        }
    }

    /**
     * Handler to retrieve all messages.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
        ctx.status(200);
    }

    /**
     * Handler to retrieve message by id
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(id);
        if (message == null)
            ctx.json("");
        else ctx.json(message);
        ctx.status(200);
    }

    /**
     * Handler to delete message by id
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessageById(id);
        if (message == null)
            ctx.json("");
        else ctx.json(message);
        ctx.status(200);
    }

    /**
     * Handler to update a message.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into a Message object.
     * If MessageService returns a null Message (meaning updating a Message was unsuccessful), the API will return status code 400 (Client error)
     * If updating a Message was successful, the API will return status code 200 (OK)
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void patchMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message updatedMessage = messageService.updateMessageById(id, message.getMessage_text());
        if (updatedMessage != null) {
            ctx.json(mapper.writeValueAsString(updatedMessage));
            ctx.status(200);
        }
        else {
            ctx.status(400);
        }
    }

    /**
     * Handler to retrieve all messages of a user by user id.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getAllMessagesByUserId(Context ctx) throws JsonProcessingException {
        int id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByUserId(id);
        ctx.json(messages);
        ctx.status(200);
    }
}