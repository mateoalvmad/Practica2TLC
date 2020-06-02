package grammar.model;

import grammar.view.GrammarAppController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 *
 * @author mateo
 */
//Class Productions, it controles the adding of a production.
public class Productions {

    public Pane pane, rightPane, buttonPane, RightSide;
    public Label index, arrow;
    public ComboBox leftSide;
    public ScrollPane scroll;
    public ObservableList<Node> rSide = FXCollections.observableArrayList();
    double xPos = 0;

    //Constructor.
    public Productions(String index) {
        pane = new Pane();
        pane.setPrefHeight(60);
        buttonPane = new Pane();
        buttonPane.setPrefSize(500, 50);
        rightPane = new Pane();
        RightSide = new Pane();
        scroll = new ScrollPane();
        scroll.setPrefWidth(280);
        scroll.setPrefHeight(50);
        this.index = new Label(index);
        arrow = new Label("→");

        Button b1 = new Button("+T");
        Button b2 = new Button("+N");
        Button b3 = new Button("+λ");
        b1.setStyle("-fx-background-color: #7396D1");
        b2.setStyle("-fx-background-color: #505F99");
        b3.setStyle("-fx-background-color: #44435F");
        b1.setLayoutY(0);
        b2.setLayoutY(0);
        b3.setLayoutY(0);
        scroll.setContent(RightSide);
        b1.setOnAction((Action) -> {
            b3.setDisable(true);
            TextField x = new TextField();
            x.setPrefWidth(85);
            x.setPrefHeight(30);
            x.setLayoutX(xPos);
            xPos = xPos + x.getPrefWidth() + 5;
            rSide.add(x);
            RightSide.getChildren().add(x);

        });
        b2.setOnAction((Action) -> {
            b3.setDisable(true);
            ComboBox x = new ComboBox();
            x.setItems(GrammarAppController.N);
            x.setValue(x.getItems().get(0));
            x.setPrefWidth(85);
            x.setPrefHeight(30);
            x.setLayoutX(xPos);
            xPos = xPos + x.getPrefWidth() + 5;
            rSide.add(x);
            RightSide.getChildren().add(x);

        });
        b3.setOnAction((Action) -> {
            b1.setDisable(true);
            b2.setDisable(true);
            b3.setDisable(true);
            Label x = new Label("λ");
            x.setPrefWidth(85);
            x.setPrefHeight(30);
            x.setLayoutX(xPos);
            xPos = xPos + x.getPrefWidth() + 5;
            rSide.add(x);
            RightSide.getChildren().add(x);

        });

        buttonPane.getChildren().add(b1);
        buttonPane.getChildren().add(b2);
        buttonPane.getChildren().add(b3);
        b1.setPrefSize(40, 10);
        b2.setPrefSize(50, 10);
        b3.setPrefSize(40, 10);
        b2.setLayoutX(b1.getPrefWidth() + 5);
        b3.setLayoutX(b1.getPrefWidth() + b2.getPrefWidth() + 10);

        rightPane.setPrefWidth(285);
        rightPane.getChildren().add(buttonPane);
        buttonPane.setLayoutX(rightPane.getPrefWidth() + 15);
        buttonPane.setLayoutY(1);

        leftSide = new ComboBox();
        if (index.equals("1.")) {
            leftSide.setDisable(true);
        }
        leftSide.setItems(GrammarAppController.N);
        leftSide.setValue(leftSide.getItems().get(0));

        this.index.setMaxWidth(40);
        this.index.setLayoutY(5);
        this.index.setLayoutX(10);
        leftSide.setMaxWidth(85);
        leftSide.setPrefWidth(85);
        leftSide.setPrefHeight(30);
        leftSide.setLayoutY(10);
        leftSide.setLayoutX(10);
        arrow.setLayoutY(5);
        arrow.setMaxWidth(40);
        rightPane.setMaxWidth(60);
        rightPane.getChildren().add(scroll);
        pane.getChildren().add(this.index);
        pane.getChildren().add(arrow);
        pane.getChildren().add(rightPane);
        pane.getChildren().add(leftSide);
        leftSide.setLayoutX(this.index.getMaxWidth());
        leftSide.setLayoutY(5);
        arrow.setMaxSize(50, 20);
        arrow.setMinSize(50, 20);

        arrow.setLayoutX(leftSide.getLayoutX() + leftSide.getMaxWidth() + 15);
        rightPane.setLayoutX(arrow.getLayoutX() + arrow.getMaxWidth() - 15);
        rightPane.setLayoutY(5);
    }

    //Method isRightSideN, it looks for an object in the right side of a production.
    public boolean isRightSide() {
        if (!rSide.isEmpty()) {
            for (Node x : rSide) {
                if (x.getTypeSelector().equalsIgnoreCase("TextField")) {
                    TextField a = (TextField) x;
                    if (a.getText().equalsIgnoreCase("")) {
                        return false;
                    }

                }
            }
        }
        return true;
    }

    //Method getPane, it returns the pane of the productions.
    public Pane getPane() {
        return pane;
    }

    //Method getIndex, it returns the index of a production.
    public Label getIndex() {
        return index;
    }

    //Method getArrow, it returns an arrow sign.
    public Label getArrow() {
        return arrow;
    }

    //Method getLeftSide, it returns a ComboBox with the Non-Terminals of the production.
    public ComboBox getLeftSide() {
        return leftSide;
    }

    //Method getRightPane, it returns the pane with the objects of a production.
    public Pane getRightPane() {
        return rightPane;
    }

}
