package com.example.flappybird.view;

import com.github.bhlangonijr.chesslib.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PromotionChoiceViewTest {
    @Test
    void startsHiddenAndShowDisplaysChoosingSide() {
        PromotionChoiceView view = new PromotionChoiceView(1200, 800);

        assertFalse(view.getNode().isVisible());
        assertTrue(view.getNode().isMouseTransparent());

        view.show(Side.BLACK);

        assertTrue(view.getNode().isVisible());
        assertFalse(view.getNode().isMouseTransparent());
        assertTrue(hasText(view, "BLACK TO CHOOSE"));
        assertTrue(hasText(view, "Q  Queen    R  Rook"));
        assertTrue(hasText(view, "B  Bishop   N  Knight"));
    }

    @Test
    void hideConcealsOverlayAgain() {
        PromotionChoiceView view = new PromotionChoiceView(1200, 800);

        view.show(Side.WHITE);
        view.hide();

        assertFalse(view.getNode().isVisible());
        assertTrue(view.getNode().isMouseTransparent());
    }

    private boolean hasText(PromotionChoiceView view, String value) {
        return view.getNode().getChildren().stream()
                .flatMap(this::flatten)
                .filter(Text.class::isInstance)
                .map(Text.class::cast)
                .map(Text::getText)
                .anyMatch(value::equals);
    }

    private java.util.stream.Stream<Node> flatten(Node node) {
        if (node instanceof Group group) {
            return java.util.stream.Stream.concat(
                    java.util.stream.Stream.of(node),
                    group.getChildren().stream().flatMap(this::flatten)
            );
        }

        if (node instanceof StackPane stackPane) {
            return java.util.stream.Stream.concat(
                    java.util.stream.Stream.of(node),
                    stackPane.getChildren().stream().flatMap(this::flatten)
            );
        }

        return java.util.stream.Stream.of(node);
    }
}
