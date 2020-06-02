package grammar.model;

import grammar.view.GrammarAppController;
import java.util.ArrayList;

/**
 *
 * @author mateo
 */
//Class NonTerminal, it makes and controles the Non-Terminals.
public class NonTerminal {

    boolean avoidable, start;
    String nonTerminal;
    public static ArrayList<String> first = new ArrayList<>();
    public ArrayList<String> firstIndicted = new ArrayList<>();
    ArrayList<String> follow = new ArrayList<>();
    public ArrayList<String> followIndicted = new ArrayList<>();
    public ArrayList<String> startGS = new ArrayList<>();
    public ArrayList<String> startGQ = new ArrayList<>();

    //Constructor.
    public NonTerminal(String nonTerminal) {
        this.nonTerminal = nonTerminal;
        this.start = false;
        this.avoidable = false;
    }

    //Mathod Follow, it creates the set follow of the Non-Terminal.
    public ArrayList<String> follow(ArrayList<String> indicted) {
        if (isStart() && !isFollow("┤")) {
            follow.add("┤");
        }
        indicted.add(nonTerminal);
        for (Production x : GrammarAppController.grammar) {
            for (Object a : x.getRightSide()) {
                if (a.getClass().getSimpleName().equalsIgnoreCase("NonTerminal")) {
                    NonTerminal n = (NonTerminal) a;
                    if (n.getNonTerminal().equalsIgnoreCase(nonTerminal)) {
                        if (x.getRightSide().indexOf(a) + 1 == x.getRightSide().size()) {
                            if (!isIndicted(x.getLeftSide().getNonTerminal(), indicted)) {
                                concatFollow(x.getLeftSide().follow(indicted));
                            }
                        } else {
                            for (int i = x.getRightSide().indexOf(a) + 1; i < x.getRightSide().size(); i++) {
                                if (x.getRightSide().get(i).getClass().getSimpleName().equalsIgnoreCase("String")) {
                                    String c = (String) x.getRightSide().get(i);
                                    if (!isFollow(c.charAt(0) + "")) {
                                        follow.add(c.charAt(0) + "");
                                    }
                                    break;
                                } else if (x.getRightSide().get(i).getClass().getSimpleName().equalsIgnoreCase("NonTerminal")) {
                                    NonTerminal m = (NonTerminal) x.getRightSide().get(i);
                                    concatFollow(m.first(m.firstIndicted));
                                    if (!m.isAvoidable()) {
                                        break;
                                    }
                                    if (i + 1 == x.getRightSide().size()) {
                                        if (!isIndicted(x.getLeftSide().getNonTerminal(), followIndicted)) {
                                            concatFollow(x.getLeftSide().follow(indicted));
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        return follow;
    }

    //Mathod first, it creates the set of first of the Non-Terminal.
    public ArrayList<String> first(ArrayList<String> indicted) {
        Boolean sw = false;
        for (String x : indicted) {
            if (x.equals(nonTerminal)) {
                sw = true;
            }
        }
        if (!sw) {
            indicted.add(nonTerminal);
            first.clear();
            for (Production x : GrammarAppController.grammar) {
                if (x.getLeftSide().getNonTerminal().equalsIgnoreCase(nonTerminal)) {
                    for (Object a : x.getRightSide()) {
                        if (a.getClass().getSimpleName().equalsIgnoreCase("String")) {
                            String n = (String) a;
                            if (!n.equalsIgnoreCase("λ") && !isFirst(n.charAt(0) + "")) {
                                first.add(n.charAt(0) + "");
                            }
                            break;
                        } else if (a.getClass().getSimpleName().equalsIgnoreCase("NonTerminal")) {
                            NonTerminal n = (NonTerminal) a;
                            if (!isIndicted(n.getNonTerminal(), indicted)) {
                                concatFirst(n.first(indicted));
                            }
                            if (!n.isAvoidable()) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        return first;
    }

    //Method concatFollow, it adds a terminal to the Follow set.
    public void concatFollow(ArrayList<String> NFollow) {
        if (NFollow != null) {
            for (String x : NFollow) {
                if (!isFollow(x)) {
                    follow.add(x);
                }
            }
        }
    }

    //Method isFollow, it looks for a terminal in the follow set.
    public boolean isFollow(String x) {
        for (String s : follow) {
            if (s.equalsIgnoreCase(x)) {
                return true;
            }
        }
        return false;
    }

    //Method isIndicted, it looks for a terminal in a list
    public boolean isIndicted(String e, ArrayList<String> list) {
        for (String x : list) {
            if (x.equalsIgnoreCase(e)) {
                return true;
            }
        }
        return false;
    }

    //Method isFollow, it looks for a terminal in the first set.
    public boolean isFirst(String f) {
        for (String s : first) {
            if (s.equalsIgnoreCase(f)) {
                return true;
            }
        }
        return false;
    }

    //Method concatFirst, it adds a terminal to the First set.
    public void concatFirst(ArrayList<String> nTFirst) {
        for (String f : nTFirst) {
            if (!isFirst(f)) {
                first.add(f);
            }
        }
    }

    //Method isStartS, it looks if a terminal is in the startGS set.
    public boolean isSatartS(String s) {
        if (startGS.contains(s)) {
            return true;
        }
        startGS.add(s);
        return false;
    }

    //Method isStartQ, it looks if a terminal is in the startGQ set.
    public boolean isStartQ(String q) {
        if (startGQ.contains(q)) {
            return true;
        }
        startGQ.add(q);
        return false;
    }

    //Method isStart, it returns the state of a Non-Terminal that is the grammar's start or not.
    public boolean isStart() {
        return start;
    }

    //Method isAvoidable, it returns the state of a Non-terminal avoidable or not avoidable.
    public boolean isAvoidable() {
        return avoidable;
    }

    //Method getNonTerminal, it resturns the Non-Terminal like a Stirng object.
    public String getNonTerminal() {
        return nonTerminal;
    }

    //Method setNonTerminal, it sets the Non-Terminal like an object.
    public void setNonTerminal(String nonTerminal) {
        this.nonTerminal = nonTerminal;
    }

    //Method setStart, it sets a Non-terminal if it is the start sign of the grammar.
    public void setStart(boolean start) {
        this.start = start;
    }

    //Method setAvoidablem, it sets the a Non-terminal if it is avoidable or not.
    public void setAvoidable(boolean avoidable) {
        this.avoidable = avoidable;
    }

    //MEthod cleanNT, it deletes the data saved.
    public void cleanNT() {
        first = new ArrayList<>();
        firstIndicted = new ArrayList<>();
        follow = new ArrayList<>();
        followIndicted = new ArrayList<>();
        startGS = new ArrayList<>();
        startGQ = new ArrayList<>();
    }

}
