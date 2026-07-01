package com.game.devilseries.structs;

import game.message.DevilSeriesMessage;

import java.util.HashMap;
import java.util.Map;

public class DevilCamp {
    //阵营id
    private int id;

    private Map<Integer, DevilCard> cards = new HashMap<>();

    private boolean active = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Integer, DevilCard> getCards() {
        return cards;
    }

    public void setCards(Map<Integer, DevilCard> cards) {
        this.cards = cards;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public DevilSeriesMessage.DevilCardCamp.Builder toProto() {
        DevilSeriesMessage.DevilCardCamp.Builder obj = DevilSeriesMessage.DevilCardCamp.newBuilder();
        obj.setActive(this.active);
        obj.setCampId(this.id);
        for(DevilCard card : cards.values()){
            obj.addCard(card.toProto());
        }
        return obj;
    }
}
