package grammar.model;

import static grammar.model.Production.alfa;
import static grammar.view.GrammarAppController.N;
import static grammar.view.GrammarAppController.N2;
import static grammar.view.GrammarAppController.grammar;
import static grammar.view.GrammarAppController.terminal;
import java.util.ArrayList;

/**
 *
 * @author mateo
 */
//Class PushdownAutomaton, it controles and makes the pushdown automaton.
public class PushdownAutomaton {

    public static int eS, pD, gram;
    public static int lS = 0;
    public static ArrayList<String> pushdownSing = new ArrayList<>();
    public static ArrayList<String> enterSing = new ArrayList<>();
    public static ArrayList<String> faults = new ArrayList<>();
    static String down;
    public static String[][] automaton, repl;
    static String transition;
    static int[][] atmtn;

    //Method initialize, it sets the parameters for the autometon.
    public static void initialize() {
        lS = eS = pD = gram = 0;
        pushdownSing = enterSing = faults = new ArrayList<>();
        down = transition = null;
        atmtn = null;
        automaton = repl = null;
    }

    //Method enterSings, it sets the sets of the pushdown and the enter signs.
    public static void enterSings() {
        boolean sw = true;
        eS = pD = 0;
        lS++;

        for (String x : N) {
            if (sw) {
                down = x;
                lS++;
                sw = false;
            }
            pushdownSing.add(x);
            pD++;
        }
        for (String x : alfa) {
            pushdownSing.add(x);
            pD++;
        }
        for (String x : terminal) {
            enterSing.add(x);
            eS++;
        }
        enterSing.add("┤");
        eS++;
        pushdownSing.add("▼");
        pD++;
    }

    //Method whatGramarIs, it returns a string with the type of the grammar.
    public static String whatGramarIs() {
        if (isGS()) {
            gram = 1;
            return "es S.";
        } else {
            if (isGQ()) {
                if (isLL1()) {
                    gram = 4;
                    return "es Q y LL(1).";
                } else {
                    gram = 2;
                    return "es Q.";
                }
            } else {
                if (isLL1()) {
                    gram = 3;
                    return "es LL(1).";
                }
            }
        }
        gram = 0;
        return "no es S, ni Q, ni LL(1).";
    }

    //Constructor.
    public static void getAutomaton() {
        int i, j;
        String t, n, l, s;
        Object o;
        automaton = new String[pD][eS];
        atmtn = new int[pD][eS];
        repl = new String[pD][eS];

        i = 0;
        while (i < pD) {
            j = 0;
            while (j < eS) {
                automaton[i][j] = "Rechace.";
                atmtn[i][j] = -1;
                j++;
            }
            i++;
        }
        automaton[pD - 1][eS - 1] = "Acepte.";
        atmtn[pD - 1][eS - 1] = 12;

        int m = 1;
        for (Production p : grammar) {
            int count;
            String replace;
            i = 0;
            for (Object x : p.rightSide) {
                l = p.leftSide;
                i++;
                ArrayList<Object> rgtSide = p.rightSide;
                //4.1
                if (x.getClass().getSimpleName().equalsIgnoreCase("String")) {
                    t = (String) x;
                    if (!t.equalsIgnoreCase("λ")) {
                        count = 1;
                        replace = "";
                        while (rgtSide.size() > count) {
                            o = rgtSide.get(count);
                            if (o.getClass().getSimpleName().equalsIgnoreCase("String")) {
                                replace = o + replace;
                            } else {
                                NonTerminal nT = (NonTerminal) o;
                                n = nT.getNonTerminal();
                                replace = n + replace;
                            }
                            count++;
                        }
                        //4.1.1
                        if (count > 1) {
                            automaton[inPushdownSing(l)][inEnterSing(t)] = "Replace(" + replace + "), Avance";
                            atmtn[inPushdownSing(l)][inEnterSing(t)] = 1;
                            repl[inPushdownSing(l)][inEnterSing(t)] = replace;
                        } else {
                            //4.1.2
                            automaton[inPushdownSing(l)][inEnterSing(t)] = "Desapile, Avance.";
                            atmtn[inPushdownSing(l)][inEnterSing(t)] = 3;
                        }
                        break;
                    } //4.3
                    else {

                        for (String sel : p.selectionSet) {
                            automaton[inPushdownSing(l)][inEnterSing(sel)] = "Desapile, Retenga.";
                            atmtn[inPushdownSing(l)][inEnterSing(sel)] = 4;
                        }
                        break;
                    }
                } //4.2
                else {
                    count = 0;
                    replace = "";
                    while (rgtSide.size() > count) {
                        o = rgtSide.get(count);
                        if (o.getClass().getSimpleName().equalsIgnoreCase("String")) {
                            replace = o + replace;
                        } else {
                            NonTerminal nT = (NonTerminal) o;
                            n = nT.getNonTerminal();
                            replace = n + replace;
                        }
                        count++;
                    }
                    for (String sel : p.selectionSet) {
                        automaton[inPushdownSing(l)][inEnterSing(sel)] = "Replace(" + replace + "), Retenga.";
                        atmtn[inPushdownSing(l)][inEnterSing(sel)] = 2;
                        repl[inPushdownSing(l)][inEnterSing(sel)] = replace;
                    }
                    break;
                }
            }

            System.out.print(m + ". ");
            for (String x : p.selectionSet) {
                System.out.print(x + ", ");
            }
            System.out.println("");
            m++;
        }
        for (String a : alfa) {
            automaton[inPushdownSing(a)][inEnterSing(a)] = "Desapile, Avance.";
            atmtn[inPushdownSing(a)][inEnterSing(a)] = 3;
        }
    }

    //Method evaluateString, it evaluates a row in te pushdown automaton.
    public static Boolean evaluateString(String string) {
        int l, i, j, t, lPD, p, e, lRep;
        boolean sw;
        char[] str, rep;
        String[] pDown = new String[100];
        String x, A;

        l = string.length();
        str = string.toCharArray();
        pDown[0] = "▼";
        pDown[1] = down;
        lPD = 2;
        i = 0;
        while (i < l) {
            try {
                x = Character.toString(str[i]);
                A = pDown[lPD - 1];
                p = inPushdownSing(A);
                e = inEnterSing(x);
                switch (atmtn[p][e]) {
                    case (1):
                        lPD--;
                        rep = repl[p][e].toCharArray();
                        lRep = repl[p][e].length();
                        j = 0;
                        sw = false;
                        while (j < lRep) {
                            if (rep[j] == '<') {
                                j++;
                                pDown[lPD] = "<" + Character.toString(rep[j]) + ">";
                                lPD++;
                                j++;
                            } else {
                                pDown[lPD] = Character.toString(rep[j]);
                                lPD++;
                            }
                            j++;
                        }
                        i++;
                        break;
                    case (2):
                        lPD--;
                        rep = repl[p][e].toCharArray();
                        lRep = repl[p][e].length();
                        j = 0;
                        sw = false;
                        while (j < lRep) {
                            if (rep[j] == '<') {
                                j++;
                                pDown[lPD] = "<" + Character.toString(rep[j]) + ">";
                                lPD++;
                                j++;
                            } else {
                                pDown[lPD] = Character.toString(rep[j]);
                                lPD++;
                                j++;
                            }
                        }
                        break;
                    case (3):
                        lPD--;
                        i++;
                        break;
                    case (4):
                        lPD--;
                        break;
                    case (12):
                        return true;
                    case (-1):
                        return false;
                }
            } catch (Exception ex) {
                return false;
            }
        }
        return false;
    }

    //Method isGS, it returns true or false if the grammar is S.
    public static boolean isGS() {
        boolean isGS = true;
        for (Production x : grammar) {
            if (x.getRightSide().get(0).getClass().getSimpleName().equalsIgnoreCase("String")) {
                String a = (String) x.getRightSide().get(0);
                if (!a.equalsIgnoreCase("λ")) {
                    if (x.getLeftSide().isSatartS(a.charAt(0) + "")) {
                        if (!inFaults("La gramática no es S ya que uno de sus N no es disyunto.")) {
                            faults.add("La gramática no es S ya que uno de sus N no es disyunto.");
                            isGS = false;
                        }
                    }
                } else {
                    if (!inFaults("La gramática no es S ya que una de sus producciones contiene el símbolo λ.")) {
                        faults.add("La gramática no es S ya que una de sus producciones contiene el símbolo λ.");
                        isGS = false;
                    }
                }
            } else {
                if (!inFaults("La gramática no es S ya que una de sus producciones comienza con un N.")) {
                    faults.add("La gramática no es S ya que una de sus producciones comienza con un N.");
                    isGS = false;
                }
            }
        }
        return isGS;
    }

    //Method isGQ, it returns true or false if the grammar is Q.
    public static boolean isGQ() {
        boolean isGQ = true;
        for (Production x : grammar) {
            if (x.getRightSide().get(0).getClass().getSimpleName().equalsIgnoreCase("String")) {
                String a = (String) x.getRightSide().get(0);
                if (!a.equalsIgnoreCase("λ")) {
                    if (x.getLeftSide().isStartQ(a.charAt(0) + "")) {
                        if (!inFaults("La gramática no es Q ya que uno de sus N no es disyunto.")) {
                            faults.add("La gramática no es Q ya que uno de sus N no es disyunto.");
                            isGQ = false;
                        }
                    }
                } else {
                    if (!disjunctFirst(x.getLeftSide(), x)) {
                        if (!inFaults("La gramática no es Q ya que uno de sus N no es disyunto.")) {
                            faults.add("La gramática no es Q ya que uno de sus N no es disyunto.");
                            isGQ = false;
                        }
                    }
                }
            } else {
                if (!inFaults("La gramática no es Q ya que una de sus producciones comienza con un N.")) {
                    faults.add("La gramática no es Q ya que una de sus producciones comienza con un N.");
                    isGQ = false;
                }
            }
        }
        if (!isNull()) {
            if (!inFaults("La gramática no es Q ya que ninguna producción contiene λ.")) {
                faults.add("La gramática no es Q ya que ninguna producción contiene λ.");
            }
            return false;
        }
        return isGQ;
    }

    //Method isLL1, it returns true or false if the grammar is LL(1).
    public static boolean isLL1() {
        boolean isGLL1 = true;
        ArrayList<ArrayList<String>> check = new ArrayList<>();
        for (NonTerminal a : N2) {
            for (Production x : grammar) {
                if (x.getLeftSide().getNonTerminal().equalsIgnoreCase(a.getNonTerminal())) {
                    x.createSelection();
                    check.add(x.getSelectionSet());
                }
            }
            if (!isDisjunct(check)) {
                if (!inFaults("La gramática no es LL(1) ya que uno de sus N no es disyunto.")) {
                    faults.add("La gramática no es LL(1) ya que uno de sus N no es disyunto.");
                }
                isGLL1 = false;
            }
            check.clear();
        }
        if (!isNull()) {
            if (!inFaults("La gramática no es LL(1) ya que ninguna producción contiene λ.")) {
                faults.add("La gramática no es LL(1) ya que ninguna producción contiene λ.");
            }
            isGLL1 = false;
        }
        return isGLL1;
    }

    //Method disjunctFirst, it returns ture or false if the set of first is disjunct.
    public static boolean disjunctFirst(NonTerminal s, Production p) {
        for (String x : s.startGS) {
            if (p.getSelectionSet().contains(x)) {
                return false;
            }
        }
        return true;
    }

    //Method isDisjunct, it returns ture or false if the selectionn set is disjunct.
    public static boolean isDisjunct(ArrayList<ArrayList<String>> set) {
        int cont = 0;
        for (ArrayList<String> c : set) {
            int cont1 = 0;
            for (ArrayList<String> a : set) {
                if (cont != cont1) {
                    for (String x : a) {
                        for (String y : c) {
                            if (y.equalsIgnoreCase(x)) {
                                return false;
                            }
                        }
                    }
                }
                cont1++;
            }
            cont++;
        }
        return true;
    }

    //Method isNull, it returns ture or false if the grammar has a lambda sign.
    public static boolean isNull() {
        for (Production x : grammar) {
            if (x.getRightSide().get(0).getClass().getSimpleName().equalsIgnoreCase("String")) {
                String a = (String) x.getRightSide().get(0);
                if (a.equalsIgnoreCase("λ")) {
                    return true;
                }
            }
        }
        return false;
    }

    //Method inFaults, it looks for a fault in the fault's set.
    public static boolean inFaults(String f) {
        for (String x : faults) {
            if (x.equalsIgnoreCase(f)) {
                return true;
            }
        }
        return false;
    }

    //Method inPushdownSing, it looks for a sign in the pushdownSing's set.
    public static int inPushdownSing(String p) {
        int i = 0;
        for (String x : pushdownSing) {
            if (x.equalsIgnoreCase(p)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    //Method inEnterSing, it looks for a sign in the enterSing's set.
    public static int inEnterSing(String e) {
        int i = 0;
        for (String x : enterSing) {
            if (x.equalsIgnoreCase(e)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    //Method getPushdownSing, it returns the list of the pushdown signs.
    public ArrayList<String> getPushdownSing() {
        return pushdownSing;
    }

    //Method getFaults, it returns the list of the faults.
    public ArrayList<String> getFaults() {
        return faults;
    }

    //Method geteS, it returns the quantity of the enter signs.
    public static int geteS() {
        return eS;
    }

    //Method getpD, it returns the quantity of the pushdown signs.
    public static int getpD() {
        return pD;
    }

    //Method getTransition, it returns a transition of the automaton.
    public String getTransition() {
        return transition;
    }

    //Method setTransition, it sets a transition of the  automaton.
    public void setTransition(String transition) {
        this.transition = transition;
    }

    //Method cleanPDA, it deles the data.
    public static void cleanPDA() {
        pushdownSing = new ArrayList<>();
        enterSing = new ArrayList<>();
        faults = new ArrayList<>();
    }

}
