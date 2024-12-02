package univ.boussad.pg.game;

import univ.boussad.pg.game.deck.Card;
import univ.boussad.pg.game.objects.Shape;
import univ.boussad.pg.game.objects.color.Coloration;
import univ.boussad.pg.game.objects.shape.button.EqualsButton;
import univ.boussad.pg.game.objects.shape.button.GreaterThanButton;
import univ.boussad.pg.game.objects.shape.button.LessThanButton;
import univ.boussad.pg.game.objects.shape.button.PassButton;

public enum Bet {
    PASS {
        @Override
        public boolean compareCards(Card playerTopMostCard, Card bettingOnCard) {
            return true;
        }

        @Override
        public Shape createButtonShape(float x, float y, float scale, Coloration coloration) {
            return new PassButton(new float[]{x,y},scale,coloration);
        }
    },
    GREATER_THAN {
        @Override
        public boolean compareCards(Card playerTopMostCard, Card bettingOnCard) {
            return playerTopMostCard.defaultGameScale > bettingOnCard.defaultGameScale;
        }

        @Override
        public Shape createButtonShape(float x, float y, float scale, Coloration coloration) {
            return new GreaterThanButton(new float[]{x,y},scale,coloration);
        }
    },
    LESS_THAN {
        @Override
        public boolean compareCards(Card playerTopMostCard, Card bettingOnCard) {
            return playerTopMostCard.defaultGameScale < bettingOnCard.defaultGameScale;
        }

        @Override
        public Shape createButtonShape(float x, float y, float scale, Coloration coloration) {
            return new LessThanButton(new float[]{x,y},scale,coloration);
        }
    },
    EQUALS {
        @Override
        public boolean compareCards(Card playerTopMostCard, Card bettingOnCard) {
            return playerTopMostCard.defaultGameScale == bettingOnCard.defaultGameScale;
        }

        @Override
        public Shape createButtonShape(float x, float y, float scale, Coloration coloration) {
            return new EqualsButton(new float[]{x,y},scale,coloration);
        }
    };

    public abstract Shape createButtonShape(float x, float y, float scale, Coloration coloration);

    public abstract boolean compareCards(Card playerTopMostCard, Card bettingOnCard);
}
