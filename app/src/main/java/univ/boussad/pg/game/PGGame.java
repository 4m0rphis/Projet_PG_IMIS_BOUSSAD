package univ.boussad.pg.game;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import univ.boussad.pg.game.deck.Card;
import univ.boussad.pg.game.objects.Shape;
import univ.boussad.pg.game.objects.color.Coloration;
import univ.boussad.pg.game.objects.shape.HollowRect;
import univ.boussad.pg.game.objects.shape.Square;
import univ.boussad.pg.game.objects.shape.TouchResult;
import univ.boussad.pg.game.objects.shape.button.ReplayButton;
import univ.boussad.pg.game.player.PGPlayer;
import univ.boussad.pg.game.renderer.DrawingModifier;
import univ.boussad.pg.game.renderer.GameRenderer;

public final class PGGame {
    private static final boolean DEBUG_CHEAT_MODE = true;

    public static Runnable RERENDER_ACTION_TRIGGER;

    private static PGGame instance;
    private final PGPlayer player1, player2;
    private int playerPlaying;
    private Card bettingOnCard, displayCard, bettingOnCardBegin;

    private final List<Card> correctBetsCardList = new ArrayList<>();

    /**
     * While this is true, all inputs are ignored, this field was made for the delay in case of a wrong bet.
     */
    private boolean inCardDisplayTransition = false;

    public PGPlayer getPlayerPlaying() {
        return getPlayer(playerPlaying);
    }

    public PGPlayer getPlayer(int i) {
        return i == 0 ? player1 : player2;
    }

    private PGGame() {
        var deck = Card.newDeck();
        Card.shuffle(deck);

        {
            this.bettingOnCard = deck.remove(0); // Retrieves and removes the head (top-most) card from the deck.
            this.bettingOnCardBegin = bettingOnCard; // Holds the original betting card, in case of a wrong bet.

            this.playerPlaying = 0; // Player's turn.
        }

        var splitDeck = Card.split(deck); // Splits the deck in half.
        this.player1 = new PGPlayer(splitDeck[0]); // Creates and gives player 0 the first half of the shuffled deck.
        this.player2 = new PGPlayer(splitDeck[1]); // Same but gives the 2nd half instead.

    }

    /**
     * @return an instance of the current game.
     */
    public static PGGame instance() {
        return instance == null ? instance = new PGGame() : instance;
    }

    /**
     * Starts a new game with a fresh deck.
     *
     * @return the new game instance.
     */
    @SuppressWarnings("UnusedReturnValue")
    public PGGame startNewGame() {
        return instance = new PGGame();
    }

    /**
     * Tells the {@link GameRenderer} what shapes to render and at what position to render them at using {@link GameRenderer#addShape(Shape, List)}
     *
     * @param renderer the instance of the game renderer to use.
     */
    public void drawInterface(GameRenderer renderer) {

        // The color to use based on which player's turn it is.
        var changingColor = playerPlaying == 0
                ? Color.valueOf(255, 69, 0)
                : Color.valueOf(230, 230, 250);

        // In case the player has no cards left, we render the winning interface.
        if (getPlayerPlaying().cards.size() == 0) {
            var button = new ReplayButton(new float[]{0, 0}, .75f, new Coloration(Color.valueOf(0, 0, 0), 0f));

            renderer.addShape(button, List.of());
            renderer.addShape(new Square(new float[]{0, 0}, 20f, new Coloration(changingColor, 0f)), List.of());

            return; // Do not continue drawing the rest of the interface.
        }

        // BEGIN CARD ORDER DISPLAY
        {
            float displayYLevel = 8.5f;
            int index = 0;
            for (var cardType : new Card[]{Card.FOURMI, Card.ESCARGOT, Card.GRENOUILLE, Card.HERISSON, Card.RENARD, Card.BICHE, Card.OURS}) {
                var shape = cardType.createShape(
                        (-8.5f + (index * 2f)),
                        displayYLevel + (index * 0.1f),
                        cardType.defaultGameScale,
                        new Coloration(Color.valueOf(132, 231, 123), index * 0.1f));

                renderer.addShape(shape, List.of(DrawingModifier.DRAW_WITH_BOX));
                index++;
            }
        }
        // END CARD ORDER DISPLAY

        // BEGIN PLAYING FIELD
        {
            // Renders the card the player is betting on.
            renderer.addShape(bettingOnCard.createShape(3.5f, 0f, bettingOnCard.defaultGameScale, new Coloration(Color.valueOf(255, 165, 0), 0f)), List.of(DrawingModifier.DRAW_WITH_BOX));

            // Renders the losing card in case of a wrong bet.
            float displayCardX = -3.5f, displayCardY = 0;
            var displayCardShape = displayCard == null
                    ? new HollowRect(new float[]{displayCardX, displayCardY}, .899f, 1f, new Coloration(changingColor, 0))
                    : displayCard.createShape(displayCardX, displayCardY, displayCard.defaultGameScale, new Coloration(changingColor, 0));

            renderer.addShape(displayCardShape, List.of(DrawingModifier.DRAW_WITH_BOX_SAME_COLOR));

            float displayYLevel = -2.5f, beginXCoordinate = -5f;
            int index = 0;

            // CONTROLS
            for (var betType : new Bet[]{Bet.LESS_THAN, Bet.EQUALS, Bet.GREATER_THAN}) {
                // If DEBUG_CHEAT_MODE is true, the correct bet is highlighted green.
                var color = DEBUG_CHEAT_MODE && betType.compareCards(getPlayerPlaying().cards.get(0), bettingOnCard) ?
                        Color.valueOf(0, 255, 0)
                        : changingColor;

                var shape = betType.createButtonShape(
                        (beginXCoordinate + index * 5f),
                        displayYLevel,
                        .5f,
                        new Coloration(color, 0f));

                renderer.addShape(shape, List.of());
                index++;
            }

            // SPECIAL "PASS" CONTROL
            {
                if (correctBetsCardList.size() > 0) { // If a card has already been placed (and the bet was correct)
                    var shape = Bet.PASS.createButtonShape(0, -4.5f, .5f, new Coloration(changingColor, 0f));
                    renderer.addShape(shape, List.of());
                }
            }

            // HEALTH BAR-LIKE DISPLAY; the thicker the box is, the more cards the player has.
            {
                // The fewer cards the player has, the fuller the square gets. (Math works)
                var shape = new HollowRect(new float[]{0, 4.25f}, 1f, (getPlayerPlaying().cards.size() / 30f) - .03f, new Coloration(changingColor, 0f), false);
                renderer.addShape(shape, List.of());
            }

            // SPECIAL "RESET" BUTTON
            {
                var shape = new Square(new float[]{8.5f, -8.5f}, .4f, new Coloration(Color.valueOf(75, 0, 130), 0f)) {
                    @Override
                    public TouchResult onShapeTouch(MotionEvent event, float glX, float glY) {
                        PGGame.instance().startNewGame();
                        return TouchResult.RERENDER;
                    }
                };
                renderer.addShape(shape, List.of());
            }
        }
        // END PLAYING FIELD

    }

    /**
     * Invoked when a player has placed a certain bet.
     *
     * @param bet the bet the player has placed.
     */
    public void playerPlacedBet(Bet bet) {
        if (inCardDisplayTransition) return; // If the player has placed a wrong bet and the losing card is still being displayed, accept no input.

        var player = getPlayerPlaying();

        if (bet.equals(Bet.PASS)) {
            bettingOnCardBegin = bettingOnCard;
            displayCard = null;

            correctBetsCardList.clear();
            changeTurn();
            return;
        }

        var playerTopMostCard = player.cards.remove(0);
        correctBetsCardList.add(playerTopMostCard);

        if (!bet.compareCards(playerTopMostCard, bettingOnCard)) {
            bettingOnCard = bettingOnCardBegin;

            displayCard = playerTopMostCard;
            this.inCardDisplayTransition = true;

            final var handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                displayCard = null;
                changeTurn();
                RERENDER_ACTION_TRIGGER.run();
                PGGame.this.inCardDisplayTransition = false;

            }, 2500);

            player.cards.addAll(correctBetsCardList);
            correctBetsCardList.clear();
            return;
        }

        bettingOnCard = playerTopMostCard;
    }

    private void changeTurn() {
        playerPlaying = playerPlaying == 0 ? 1 : 0;
    }
}
