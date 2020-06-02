package grammar.model;

import grammar.view.GrammarAppController;
import java.util.ArrayList;

/**
 *
 * @author mateo
 */
//Class Production, it makes and controles the productions.
public class Production {

    boolean nAvoidable, avoidable;
    String productionIndex, productionString, leftSide;
    NonTerminal lftSide;
    ArrayList<String> selectionSet;
    ArrayList<String> first = new ArrayList<>();
    ArrayList<Object> rightSide = new ArrayList<>();
    public static ArrayList<String> alfa = new ArrayList<>();

    //Constructor.
    public Production(String productionIndex, NonTerminal leftSide) {
        this.productionIndex = productionIndex;
        this.lftSide = leftSide;
        this.leftSide = leftSide.getNonTerminal();
        this.productionString = productionIndex + "  " + this.leftSide + "  →  ";
        nAvoidable = false;
    }

    //Mathod first, it creates the set of first of the productions.
    public ArrayList<String> first() {
        String n;
        for (Object a : rightSide) {
            if (a.getClass().getSimpleName().equalsIgnoreCase("String")) {
                n = (String) a;
                if (!n.equalsIgnoreCase("λ") && !isFirst(n.charAt(0) + "")) {
                    first.add(n.charAt(0) + "");
                }
                break;
            } else if (a.getClass().getSimpleName().equalsIgnoreCase("NonTerminal")) {
                NonTerminal nT = (NonTerminal) a;
                concatFirst(nT.first(nT.firstIndicted));
                if (!nT.isAvoidable()) {
                    break;
                }
            }
        }
        return first;
    }

    //Method getAlfa, it makes the terminal's set wich belong an alfa row.
    public static void getAlfa() {
        String n;
        int i;
        for (Production x : GrammarAppController.grammar) {
            i = 0;
            for (Object a : x.rightSide) {
                i++;
                if (i > 1) {
                    if (a.getClass().getSimpleName().equalsIgnoreCase("String")) {
                        n = (String) a;
                        if (!n.equalsIgnoreCase("λ") && !isAlfa(n.charAt(0) + "")) {
                            alfa.add(n.charAt(0) + "");
                        }
                    }
                }
            }
        }
    }

    //Method isFollow, it looks for a terminal in the alfa set.
    public static boolean isAlfa(String f) {
        for (String s : alfa) {
            if (s.equalsIgnoreCase(f)) {
                return true;
            }
        }
        return false;
    }

    //Method isFollow, it looks for a terminal in the first set.
    public boolean isFirst(String x) {
        for (String s : first) {
            if (s.equalsIgnoreCase(x)) {
                return true;
            }
        }
        return false;
    }

    public void concatFirst(ArrayList<String> primerosNt) {
        for (String x : primerosNt) {
            if (!isFirst(x)) {
                first.add(x);
            }
        }
    }

    //Method concatFirst, it adds a terminal to the First set.
    public boolean Avoidable() {
        for (String x : GrammarAppController.pnAvoidable) {
            if (x.equalsIgnoreCase(productionIndex)) {
                return true;
            }
        }
        return false;
    }

    //Method createSelection, it makes the selection set.
    public void createSelection() {
        selectionSet = first();
        if (Avoidable()) {
            concatSelection(lftSide.follow(lftSide.followIndicted));
        }
    }

    //Method isSelection, it looks for a terminal in the selection set.
    public boolean isSelection(String s) {
        for (String x : selectionSet) {
            if (x.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    //Method concatSelection, it adds a terminal to the Selection set.
    public void concatSelection(ArrayList<String> lista) {
        if (lista != null) {
            for (String x : lista) {
                if (!isSelection(x)) {
                    selectionSet.add(x);
                }
            }
        }
    }

    //Method isRightSideN, it looks for an object in the right side of a production.
    public boolean isRightSideN() {
        for (Object x : rightSide) {
            if (!x.getClass().getSimpleName().equalsIgnoreCase("NonTerminal")) {
                return false;
            }
        }
        return true;
    }

    //Method nonTerminalIn, it looks for a Non-Terminal in avoisdable's set.
    public boolean nonTerminalIn(NonTerminal n) {
        for (NonTerminal x : GrammarAppController.NAvoidable) {
            if (x.getNonTerminal().equalsIgnoreCase(n.getNonTerminal())) {
                return true;
            }
        }
        return false;
    }

    //Method enterAvoidable, it looks for the state of a production if it is avoidable or not avoidable.
    public boolean enterAvoidable() {
        for (Object n : getRightSide()) {
            NonTerminal a = (NonTerminal) n;
            if (!nonTerminalIn(a)) {
                return false;
            }
        }
        return true;
    }

    //Method isAvoidable, it returns the state of a Non-terminal avoidable or not avoidable.
    public boolean isNAvoidable() {
        return nAvoidable;
    }

    //Method setAvoidablem, it sets the a Non-terminal if it is avoidable or not.
    public void setNAvoidable(boolean nAvoidable) {
        this.nAvoidable = nAvoidable;
    }

    //Method setAvoidablem, it sets the a production if it is avoidable or not.
    public void setAvoidable(boolean avoidable) {
        this.avoidable = avoidable;
    }

    //Method isAvoidable, it returns the state of a production avoidable or not avoidable.
    public boolean isAvoidable() {
        return avoidable;
    }

    //Method getProductionString, it returns the production like a string.
    public String getProductionString() {
        return productionString;
    }

    //Method getLeftSide, it returns the production's left side like a Non-Terminal object.
    public NonTerminal getLeftSide() {
        return lftSide;
    }

    //Method getRightSide, it returns the productio's right side like a set of  objects.
    public ArrayList<Object> getRightSide() {
        return rightSide;
    }

    //Method setProductionString, it adds a row ti the production row.
    public void setProductionString(String element) {
        this.productionString = this.productionString + element;
    }

    //Method setLeftIzquierdo, it sets the production's left side.
    public void setLeftIzquierdo(NonTerminal lftSide) {
        this.lftSide = lftSide;
    }

    //Method setRightSide, it sets the production's right side.
    public void setRightSide(Object rightSide) {
        this.rightSide.add(rightSide);
    }

    //Method getProductionIndex, it returns the production's index.
    public String getProductionIndex() {
        return productionIndex;
    }

    //Method setProductionIndex, it sets the production's index.
    public void setProductionIndex(String productionIndex) {
        this.productionIndex = productionIndex;
    }

    //Method getSelectionSet, it returns theselection set.
    public ArrayList<String> getSelectionSet() {
        return selectionSet;
    }

    //Method cleanPN, it deletes the data saved.
    public  void cleanPN() {
        first = new ArrayList<>();
        rightSide = new ArrayList<>();
        alfa = new ArrayList<>();
    }
}
