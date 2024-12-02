package univ.boussad.pg.game.deck;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import univ.boussad.pg.game.objects.Shape;
import univ.boussad.pg.game.objects.color.Coloration;
import univ.boussad.pg.game.objects.shape.Diamond;
import univ.boussad.pg.game.objects.shape.Hexagon;
import univ.boussad.pg.game.objects.shape.Hourglass;
import univ.boussad.pg.game.objects.shape.Pentagon;
import univ.boussad.pg.game.objects.shape.Square;
import univ.boussad.pg.game.objects.shape.Trapezoid;
import univ.boussad.pg.game.objects.shape.Triangle;

public enum Card {

    FOURMI(.2f) {
        @Override
        public Shape createShape(float x, float y, float scale, @NotNull Coloration coloration) {
            return new Triangle(new float[]{x,y},scale, coloration);
        }
    },
    ESCARGOT(.3f) {
        @Override
        public Shape createShape(float x, float y, float scale, @NotNull Coloration coloration) {
            return new Trapezoid(new float[]{x,y},scale, coloration);
        }
    },
    GRENOUILLE(.4f) {
        @Override
        public Shape createShape(float x, float y, float scale, @NotNull Coloration coloration) {
            return new Square(new float[]{x,y},scale, coloration);
        }
    },
    HERISSON(.5f) {
        @Override
        public Shape createShape(float x, float y, float scale, @NotNull Coloration coloration) {
            return new Diamond(new float[]{x,y},scale, coloration);
        }
    },
    RENARD(.6f) {
        @Override
        public Shape createShape(float x, float y, float scale, @NotNull Coloration coloration) {
            return new Hexagon(new float[]{x,y},scale, coloration);
        }
    },
    BICHE(.7f) {
        @Override
        public Shape createShape(float x, float y, float scale, @NotNull Coloration coloration) {
            return new Pentagon(new float[]{x,y},scale, coloration);
        }
    },
    OURS(.8f) {
        @Override
        public Shape createShape(float x, float y, float scale, @NotNull Coloration coloration) {
            return new Hourglass(new float[]{x,y},scale, coloration);
        }
    },;

    public static void shuffle(List<Card> cards) {
        Collections.shuffle(cards, new Random());
    }

    public static List<Card> newDeck() {
        var cards = new ArrayList<Card>();
        for (var card : values()) {
            for (int i = 0; i < (card == FOURMI || card == OURS ? 8 : 9); i++) {
                cards.add(card);
            }
        }

        return cards;
    }

    public static List<Card>[] split(List<Card> originalList) {
        int middle = originalList.size() / 2;
        List<Card> list1 = new ArrayList<>(), list2 = new ArrayList<>();

        for (int i = 0; i < originalList.size() - 1; i++) {
            if (i + 1 >= middle) {
                list2.add(originalList.get(i));
                continue;
            }

            list1.add(originalList.get(i));
        }

        //noinspection unchecked
        return new List[]{list1, list2};
    }

    public final float defaultGameScale; // The smaller the scale, the weaker the card.
    Card(float scale) {
        this.defaultGameScale = scale;
    }

    public abstract Shape createShape(float x, float y, float scale, Coloration coloration);
}
