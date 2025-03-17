package me.yejun.famabot.listeners;

/*
    Written by Yejun Kang on 3/14/25
 */

import me.yejun.famabot.Main;
import me.yejun.famabot.data.quote.QuoteData;
import me.yejun.famabot.data.StockDataApiHandler;
import me.yejun.famabot.formatter.StockOverview;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;

import static me.yejun.famabot.data.stock.SpyCompaniesMap.sp500Companies;

public class MessageListener extends ListenerAdapter {
    public StockOverview stockOverview = new StockOverview();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String content = message.getContentRaw();

        MessageChannel channel = event.getChannel();
        switch (content) {
            case "ping" -> channel.sendMessage("Pong!").queue();
            case "version" -> channel.sendMessage("v" + Main.VERSION).queue();
            default -> {
                if (content.startsWith("$")) {
                    String companyName = content.substring(1).trim().toUpperCase();
                    String symbol = sp500Companies.getOrDefault(companyName, companyName);
                    try {
                        stockOverview.createStockOverview(companyName, symbol, channel);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

}
