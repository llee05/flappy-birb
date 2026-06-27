package com.example.flappybird.view;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EndScreenViewTest {
    @Test
    void startsHiddenAndShowDisplaysTitleAndReason() {
        EndScreenView view = new EndScreenView(1200, 800, () -> {
        });

        assertFalse(view.getNode().isVisible());
        assertTrue(view.getNode().isMouseTransparent());

        view.show("White wins", "Checkmate");

        assertTrue(view.getNode().isVisible());
        assertFalse(view.getNode().isMouseTransparent());
        assertEquals("White wins", textByValue(view, "White wins").getText());
        assertEquals("Checkmate", textByValue(view, "Checkmate").getText());
    }

    @Test
    void restartButtonRunsRestartAction() {
        AtomicInteger restartCount = new AtomicInteger();
        EndScreenView view = new EndScreenView(1200, 800, restartCount::incrementAndGet);

        restartButton(view).getOnMouseClicked().handle(null);

        assertEquals(1, restartCount.get());
    }

    private Text textByValue(EndScreenView view, String value) {
        return view.getNode().getChildren().stream()
                .flatMap(this::flatten)
                .filter(Text.class::isInstance)
                .map(Text.class::cast)
                .filter(text -> value.equals(text.getText()))
                .findFirst()
                .orElseThrow();
    }

    private StackPane restartButton(EndScreenView view) {
        return view.getNode().getChildren().stream()
                .flatMap(this::flatten)
                .filter(StackPane.class::isInstance)
                .map(StackPane.class::cast)
                .filter(node -> node.getOnMouseClicked() != null)
                .findFirst()
                .orElseThrow();
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
