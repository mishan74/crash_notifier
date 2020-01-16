package io.mywish.bot.model;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "chat")
@Getter
public class SubscribedChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "bot", nullable = false)
    private String botName;


    public SubscribedChat() {
    }

    public SubscribedChat(Long chatId, String botName) {
        this.chatId = chatId;
        this.botName = botName;
    }
}
