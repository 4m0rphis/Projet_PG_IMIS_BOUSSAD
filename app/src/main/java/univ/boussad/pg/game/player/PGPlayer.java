package univ.boussad.pg.game.player;

import java.util.List;

import univ.boussad.pg.game.PGGame;
import univ.boussad.pg.game.deck.Card;

public class PGPlayer {

    public final List<Card> cards;

    public PGPlayer(List<Card> cards) {
        this.cards = cards;
    }
}
