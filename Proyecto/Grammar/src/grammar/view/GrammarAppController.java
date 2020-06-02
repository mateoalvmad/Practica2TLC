package grammar.view;

import grammar.model.Productions;
import grammar.Grammar;
import grammar.model.NonTerminal;
import grammar.model.Production;
import grammar.model.PushdownAutomaton;
import static grammar.model.PushdownAutomaton.automaton;
import static grammar.model.PushdownAutomaton.cleanPDA;
import static grammar.model.PushdownAutomaton.enterSing;
import static grammar.model.PushdownAutomaton.evaluateString;
import static grammar.model.PushdownAutomaton.faults;
import static grammar.model.PushdownAutomaton.getAutomaton;
import static grammar.model.PushdownAutomaton.gram;
import static grammar.model.PushdownAutomaton.pushdownSing;
import static grammar.model.PushdownAutomaton.whatGramarIs;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;

/**
 * [MateoAlvarezMadrigal][JulianaCadavidRamirez][practica2][TdeL]
 *
 * @author mateo
 */
//Class GrammarAppController, it controles the visible part of the application.
public class GrammarAppController implements Initializable {
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%Utils%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   

    Grammar mainApp;

    //Method initialize, it sets the another panes off.
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        NTPane.setVisible(false);
        prtnPane.setVisible(false);
        show.setVisible(false);
        help.setVisible(false);
    }

    //Method setMainApp, it sets the mainApp.
    public void setMainApp(Grammar mainApp) {
        this.mainApp = mainApp;
    }

    //Method showMessage, it show a message in the screen of the user.
    public void showMessage(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
        alert.close();
    }

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%Home Page%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @FXML
    private AnchorPane homePane;

    @FXML
    private AnchorPane help;

    //Method handleStart, it shows the panel where the user can introduce the Non-Terminals.
    @FXML
    private void handleStart(ActionEvent event) {
        homePane.setVisible(false);
        NTPane.setVisible(true);
        initializeNT();
    }

    //Method handleInfo, it shows the panel where the user can get info about the application.
    @FXML
    private void handleInfo(ActionEvent event) {
        homePane.setVisible(false);
        help.setVisible(true);
    }

    //Method openBook, it shows the book "Introduccion A Los Compiladores".
    @FXML
    private void openBook(ActionEvent event) {
        String fileLocal = new String("src/grammar/manuals/IntroduccionALosCompiladores.pdf");
        try {
            File path = new File(fileLocal);
            Desktop.getDesktop().open(path);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            showMessage("Error", "No se pudo encontrar el archivo");
            e.printStackTrace();
        }
    }
    //Method openBook, it shows the user manual.
    @FXML
    private void openUserManual(ActionEvent event) {
        String fileLocal = new String("src/grammar/manuals/userManual.pdf");
        try {
            File path = new File(fileLocal);
            Desktop.getDesktop().open(path);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            showMessage("Error", "No se pudo encontrar el archivo");
            e.printStackTrace();
        }
    }

    //Method openBook, it shows the technical manual.
    @FXML
    private void openTechnicalManual(ActionEvent event) {
        String fileLocal = new String("src/grammar/manuals/javadoc/index.html");
        try {
            File path = new File(fileLocal);
            Desktop.getDesktop().open(path);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            showMessage("Error", "No se pudo encontrar el archivo");
            e.printStackTrace();
        }
    }
    
    //Method backHome, it goes back to the home panel.
    @FXML
    private void backHome(ActionEvent event) {
        homePane.setVisible(true);
        help.setVisible(false);
    }

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%Add Non-Terminal Panel%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @FXML
    private AnchorPane NTPane;
    @FXML
    private ScrollPane NPaneScroll;
    @FXML
    private Pane NPane;
    @FXML
    private Pane fatherPane;
    @FXML
    private Button add;

    public static ArrayList<TextField> NBox;
    public static ObservableList<String> N;
    public static ObservableList<NonTerminal> N2;
    private static double yPos = 1;
    private static double xPos;

    //Method initializeProduction, it sets the parameters for the pane wich adds the NON-Terminals.
    public void initializeNT() {
        NBox = new ArrayList<>();
        N = FXCollections.observableArrayList();
        N2 = FXCollections.observableArrayList();
        add = new Button("+");
        N.clear();
        N2.clear();
        NBox.clear();
        xPos = 0;
        yPos = 0;
        NPane.getChildren().add(add);
        add.setMinSize(45, 35);
        add.setMaxSize(45, 35);
        add.setWrapText(true);
        add.setAlignment(Pos.CENTER);
        add.setLayoutY(35);
        add.setStyle("-fx-background-color: #1B009D ; -fx-text-fill: white; -fx-font-size: 18px;");
        NPaneScroll = new ScrollPane();
        NPaneScroll.setLayoutX(60);
        NPaneScroll.setLayoutY(120);
        NPaneScroll.setPrefSize(500, 200);
        NPaneScroll.setContent(NPane);
        NPaneScroll.setBackground(Background.EMPTY);
        NPaneScroll.applyCss();
        fatherPane.getChildren().add(NPaneScroll);
        add.setOnAction((ActionEvent t) -> {
            NBoxConstructor(NPane);
        });
    }

    //Method handleNext, it shows the panel where the user can add the productions.
    @FXML
    public void handleNext() {
        if (!NBox.isEmpty()) {
            for (TextField x : NBox) {
                if (!isAdditive("<" + x.getText() + ">") || x.getText().equalsIgnoreCase("")) {
                    if (!isAdditive("<" + x.getText() + ">")) {
                        showMessage("Error", "No Terminales repetidos.");
                        N.clear();
                        return;
                    } else {
                        showMessage("Error", "Debes ingresar los Terminales en el cuadro de texto.");
                        N.clear();
                        return;
                    }
                } else {
                    String nonTerminal = "<" + x.getText().toUpperCase() + ">";
                    NonTerminal nonTerminal2 = new NonTerminal(nonTerminal);
                    N.add(nonTerminal);
                    N2.add(nonTerminal2);
                }
            }
            NTPane.setVisible(false);
            prtnPane.setVisible(true);
            initializeProductions();
        } else {
            showMessage("Error", "Debes ingresar al menos un No Terminal.");
        }
    }

    //Method handleBack,it goes back to the home panel.
    @FXML
    public void handleBack() {
        cont = 0;
        NPane.getChildren().clear();
        add.setLayoutX(0);
        prtnPane.setVisible(false);
        NTPane.setVisible(false);
        homePane.setVisible(true);
    }

    //Method NBoxConstructor, it adds a box where the user can add Non-Terminals.
    public void NBoxConstructor(Pane NPane) {
        TextField x;
        x = new TextField();
        x.setMaxWidth(50);
        if (NBox.size() % 6 == 0) {
            yPos = yPos + 35;
            xPos = 0;
        }
        x.setLayoutX(xPos);
        x.setLayoutY(yPos);
        NBox.add(x);
        NPane.getChildren().add(x);
        xPos = xPos + x.getMaxWidth() + 18;
        add.setLayoutX(x.getLayoutX() + x.getMaxWidth() + 18);
        add.setLayoutY(x.getLayoutY());
    }

    //Method isAdditive, it checks if a Non-Terminal is arleady set.
    public boolean isAdditive(String nonTerminal) {
        return N.stream().noneMatch((x) -> (x.equalsIgnoreCase(nonTerminal)));
    }

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%Add productions Panel%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @FXML
    private AnchorPane prtnPane;
    @FXML
    static ArrayList<Productions> productions = new ArrayList<>();
    @FXML
    public Pane pane;
    Pane pane2;
    ScrollPane scroll;

    int cont = 0;
    public static ObservableList<Production> grammar = FXCollections.observableArrayList();
    public static ArrayList<NonTerminal> NAvoidable = new ArrayList<>();
    public static ArrayList<String> pnAvoidable = new ArrayList<>();
    public static ArrayList<String> terminal = new ArrayList<>();

    //Method handleEnd, it checks if the productions are okey and shows the panel where the user can visualize the automaton and avaluates a row.
    public void handleEnd() {
        for (Productions x : productions) {
            if (x.rSide.isEmpty()) {
                showMessage("Error", "Debes ingresar el lado derecho de las producciones que agregues.");
                return;
            }
            if (x.isRightSide() == false) {
                showMessage("Error", "Debes llenar las cajas de texto que agregues.");
                return;
            }
        }
        for (Productions x : productions) {
            NonTerminal n = searchN(x.leftSide.getValue().toString());
            if (productions.get(0) == x) {
                n.setStart(true);
            }
            Production p = new Production(x.getIndex().getText(), n);
            for (Node e : x.rSide) {
                if (e.getTypeSelector().equalsIgnoreCase("TextField")) {
                    TextField a = (TextField) e;
                    p.setRightSide(a.getText());
                    p.setProductionString(a.getText());
                    if (!inTerminal(a.getText())) {
                        terminal.add(a.getText());
                    }
                } else if (e.getTypeSelector().equalsIgnoreCase("ComboBox")) {
                    ComboBox a = (ComboBox) e;
                    p.setRightSide(searchN(a.getValue().toString()));
                    p.setProductionString(searchN(a.getValue().toString()).getNonTerminal());
                } else {
                    Label a = (Label) e;
                    p.setRightSide(a.getText());
                    p.setProductionString(a.getText());
                }
            }
            grammar.add(p);
        }
        NAndPnAvoidable();
        for (Production x : grammar) {
            x.createSelection();
        }
        Production.getAlfa();
        show.setVisible(true);
        prtnPane.setVisible(false);
        initializeGPA();
    }

    //Method searchN, it search a Non-Terminal object by its name.
    public static NonTerminal searchN(String nonTerminal) {
        for (NonTerminal x : N2) {
            if (x.getNonTerminal().equalsIgnoreCase(nonTerminal)) {
                return x;
            }
        }
        return null;
    }

    //Method initializeProductions, it sets the parameters for the pane where the user can adds the produxtions.
    public void initializeProductions() {
        cont++;
        productions.clear();
        pane.getChildren().clear();
        Productions ptn = new Productions(cont + ".");
        productions.add(ptn);

        pane2 = new Pane();
        pane2.setMinSize(600, 500);
        pane2.getChildren().add(ptn.getPane());

        Button agg = new Button("+");
        Button remove = new Button("-");
        remove.setVisible(false);
        remove.setStyle("-fx-background-color: #CF5060; -fx-text-fill: black");
        agg.setLayoutY(ptn.pane.getPrefHeight() - 10);
        agg.setLayoutX(5);
        agg.setStyle("-fx-background-color: #52A08D; -fx-text-fill: black;");
        agg.setPrefSize(30, 30);
        remove.setPrefSize(30, 30);

        agg.setOnAction((Action) -> {
            remove.setVisible(true);
            cont++;
            Productions x = new Productions(cont + ".");
            productions.add(x);
            pane2.getChildren().add(x.getPane());
            x.getPane().setLayoutY(agg.getLayoutY());
            agg.setLayoutY(agg.getLayoutY() + x.pane.getPrefHeight() - 10);
            remove.setLayoutX(agg.getLayoutX() + agg.getWidth() + 2);
            remove.setLayoutY(agg.getLayoutY());

        });
        remove.setOnAction((Action) -> {
            cont--;
            agg.setLayoutY(agg.getLayoutY() - productions.get(cont).pane.getPrefHeight() + 10);
            remove.setLayoutY(agg.getLayoutY());
            pane2.getChildren().remove(productions.get(cont).getPane());
            productions.remove(productions.get(cont));
            if (cont == 1) {
                remove.setVisible(false);
            }
        });

        pane2.getChildren().add(agg);
        pane2.getChildren().add(remove);

        scroll = new ScrollPane();
        scroll.setPrefSize(657, 443);
        scroll.setContent(pane2);
        pane.getChildren().add(scroll);
    }

    //Method NAndPnAvoidable, it adds the productions wich are avoidable.
    static public void NAndPnAvoidable() {
        for (Production x : grammar) {
            if (x.getRightSide().get(0) == "λ") {
                searchN(x.getLeftSide().getNonTerminal()).setAvoidable(true);
                NAvoidable.add(x.getLeftSide());
                pnAvoidable.add(x.getProductionIndex());
            }
        }
        boolean end = false;
        while (end == false) {
            end = true;
            for (Production x : grammar) {
                if (x.isRightSideN()) {
                    if (x.enterAvoidable() && !x.isNAvoidable()) {
                        x.setNAvoidable(true);
                        pnAvoidable.add(x.getProductionIndex());
                        if (!inAvoidable(x.getLeftSide().getNonTerminal())) {
                            searchN(x.getLeftSide().getNonTerminal()).setAvoidable(true);
                            NAvoidable.add(x.getLeftSide());
                        }
                        end = false;
                    }
                }
            }
        }

    }

    //Method inAvoidable, it looks for a Non-Terminal in the avoidable's set.
    static public boolean inAvoidable(String noTerminal) {
        for (NonTerminal x : NAvoidable) {
            if (x.getNonTerminal().equalsIgnoreCase(noTerminal)) {
                return true;
            }
        }
        return false;
    }

    //Method inTerminal, it looks for a Terminal in the terminal's set.
    static public boolean inTerminal(String t) {
        for (String x : terminal) {
            if (x.equalsIgnoreCase(t)) {
                return true;
            }
        }
        return false;
    }

//%%%%%%%%%%%%%%%%%%%%%Show Grammar and  Pushdoen automaton%%%%%%%%%%%%%%%%%%%%%    
    @FXML
    AnchorPane show;
    @FXML
    AnchorPane paneG;
    @FXML
    private TableView<Production> table;
    @FXML
    private TableColumn<Production, String> production;
    @FXML
    public TableView<List<StringProperty>> PDATable;
    @FXML
    private ArrayList<TableColumn<List<StringProperty>, String>> trn = new ArrayList<>();
    @FXML
    private Label GKind;
    @FXML
    private TextField string;
    @FXML
    private Button addN;
    @FXML
    private Button bE;
    @FXML
    private AnchorPane PDAPane;
    boolean sw = false;

    //Method initializeGPA, it sets the parameters where the automaton and the production woll be showed.
    public void initializeGPA() {
        int k, j;
        String t;
        paneG.getChildren().clear();
        table = new TableView<>();
        production = new TableColumn<>("Producciones");
        table.setMinSize(438, 318);
        table.getColumns().add(production);
        production.setMinWidth(438);
        production.setCellValueFactory(new PropertyValueFactory<>("productionString"));
        production.setCellFactory(TextFieldTableCell.forTableColumn());
        table.setItems(grammar);
        paneG.getChildren().add(table);

        GKind.setText(whatGramarIs());
        PushdownAutomaton.enterSings();
        getAutomaton();

        PDAPane.getChildren().clear();
        PDATable = new TableView<>();
        PDATable.setMaxSize(440, 281);
        PDATable.setMinSize(440, 281);
        if (gram != 0) {
            k = 0;
            do {
                final int q = k;
                if (k == 0) {
                    t = "";
                } else {
                    t = enterSing.get(k - 1);
                }
                TableColumn<List<StringProperty>, String> cln = new TableColumn<>(t);
                cln.setCellValueFactory(data -> data.getValue().get(q));
                trn.add(cln);
                PDATable.getColumns().add(cln);
                k++;
            } while (enterSing.size() >= k);

            ObservableList<List<StringProperty>> data = FXCollections.observableArrayList();

            k = 0;
            for (String x : pushdownSing) {
                List<StringProperty> r = new ArrayList<>();
                r.add(0, new SimpleStringProperty(x));
                j = 1;
                while (trn.size() > j) {
                    r.add(j, new SimpleStringProperty(automaton[k][j - 1]));
                    j++;
                }
                data.add(r);
                k++;
            }
            PDATable.setItems(data);
            PDAPane.getChildren().add(PDATable);
        } else {
            TableColumn<List<StringProperty>, String> cln = new TableColumn<>("Razones:");
            cln.setCellValueFactory(data -> data.getValue().get(0));
            PDATable.getColumns().add(cln);
            ObservableList<List<StringProperty>> data = FXCollections.observableArrayList();
            for (String x : faults) {
                List<StringProperty> r = new ArrayList<>();
                r.add(0, new SimpleStringProperty(x));
                data.add(r);
            }
            PDATable.setItems(data);
            PDAPane.getChildren().add(PDATable);
            string.setDisable(true);
            addN.setDisable(true);
            bE.setDisable(true);
        }
    }

    //Method newGrammar, it deletes all the data to add a new grammar.
    @FXML
    public void newGrammar() {
        prtnPane.setVisible(false);
        show.setVisible(false);
        NTPane.setVisible(false);
        homePane.setVisible(true);
        //Restart nonT
        for (NonTerminal n : N2) {
            n.cleanNT();
        }
        for (Production p : grammar) {
            p.cleanPN();
        }
        yPos = 1;
        NPane.getChildren().clear();
        add.setLayoutX(0);
        //Restart production
        cont = 0;
        productions = new ArrayList<>();
        grammar = FXCollections.observableArrayList();
        NAvoidable = new ArrayList<>();
        pnAvoidable = new ArrayList<>();
        terminal = new ArrayList<>();
        //Restart show
        PDATable.setItems(null);
        trn = new ArrayList<>();
        string.setText("");
        sw = false;
        cleanPDA();
    }

    //Method examinateString, it evaluates a row in the automaton.
    @FXML
    public void examinateString() {
        String s;
        if (!sw) {
            showMessage("Hilera erronea", "No puedes evaluar la hilera sin agregar el símbolo de fin de secuencia, usa el botón \"Añadir\" ┤");
        } else {
            s = string.getText();
            if (s.equals("") || s.equals(null)) {
                showMessage("Hilera vacía.", "Debes ingresar una hilera si quieres que sea evaluada por el automata.");
            } else {
                if (evaluateString(s)) {
                    showMessage("Hilera", "La hilera fue reconocida exitosamente por la gramática.");
                } else {
                    showMessage("Hilera", "La hilera no fue reconocida por la gramática, fue rechazada.");
                }
            }
        }
    }

    //Method addEnd, it adds the final row sign.
    @FXML
    public void addEnd() {
        String s;
        if (!sw) {
            sw = true;
            s = string.getText();
            s = s + "┤";
            string.setText(s);
            string.setDisable(true);
            addN.setText("Editar");
        } else {
            sw = false;
            s = string.getText();
            s = s.substring(0, s.length() - 1);
            string.setText(s);
            string.setDisable(false);
            addN.setText("Añadir ┤");
        }
    }

    //Method close, it closes the application.
    @FXML
    public void close() {
        Grammar.closeStage();
    }
}
