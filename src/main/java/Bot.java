import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Bot  extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        String token = ""; /*---Discord bot token---*/
        String activityText = "Type !quote"; /*---text to display for bot activity in sidebar---*/

        JDABuilder bot = JDABuilder.createDefault(token);

        bot.setActivity(Activity.playing(activityText));
        bot.addEventListeners(new Bot());
        bot.build();
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        if(msg.getContentRaw().equals("!quote"))
        {
            MessageChannel channel = event.getChannel();
            String quote;
            try {
                quote = getQuote();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            String finalQuote = quote;
            channel.sendMessage(quote) /* => RestAction<Message> */
                    .queue((Message response) /* => Message */ -> {
                        response.editMessageFormat(finalQuote).queue();
                    });
        }
    }

    public String getQuote() throws FileNotFoundException {
        String filepath = ""; /*---file path of the quotes. make it a text file---*/
        int quotesLength = 10; /*---number of quotes in file---*/
        String delimiter = "\n"; /*---delimiter used to show end of quote, can be set to personal preference---*/

        String[] quotes = new String[quotesLength];

        //file object with file location
        File file = new File(filepath);
        Scanner sc = new Scanner(file);

        sc.useDelimiter(delimiter);
        //get all quotes in file and add them to quotes array
        for(int i = 0; i < quotes.length; i++)
            quotes[i] = sc.next();

        //pick a random quote to show
        Random rand = new Random();
        int randIndex = rand.nextInt(quotesLength + 1);

        return quotes[randIndex];
    }
}
