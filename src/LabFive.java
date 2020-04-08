import java.util.Scanner;
import java.util.Stack;

public class LabFive {
    private Stack<Integer> stack;  // Stores integers in Stack for computing operations
    private Stack<String> optStack; // Stores operations in Stack
    private Stack<String> expStack; // Stores expressions used to convert post to infix or pre to infix
    public int value;

    // Constructor
    public LabFive() {
        stack = new Stack<Integer>();
        optStack = new Stack<String>();
        expStack = new Stack<String>();
        value = 0;

    }


    // Method to evaluate Postfix expression
    public void evaluatePostFix(String s) {
        try {
            //String s = "536+*";
            stack.clear();
            value = 0;

            String str[] = s.split(" ");

            for (int i = 0; i < str.length; i++) {
                evaluateE(str[i], 1);

            }
            System.out.println("Here is the completed operation: " + value);

        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Invalid expression.");
        }

    }

    // Method to evaluate Prefix expression
    public void evaluatePreFix(String s) {
        try {
            //String s = "536+*";
            stack.clear();
            value = 0;

            String str[] = s.split(" ");

            for (int i = str.length-1; i >= 0; i--) {
                evaluateE(str[i],2);

            }
            System.out.println("Here is the completed operation: " + value);

        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Invalid expression.");
        }

    }

    //Method - computes value for given operation and pushes it back to stack.
    // If number is given, it pushes to stack
    // opt -1 is prefix 2 - post fix
    public void evaluateE(String s, int opt) throws Exception{

    try {
        int x =0;
        int y =0;

        switch (s) {
            case "*":
                value = stack.pop() * stack.pop();
                stack.push(value);
                break;
            case "+":
                value = stack.pop() + stack.pop();
                stack.push(value);
                break;
            case "/":
                x = stack.pop();
                y = stack.pop();
                if (opt == 1){
                    value = y / x; //Postfix
                }else{
                    value = x / y; // Prefix
                }

                stack.push(value);
                break;
            case "-":
                x = stack.pop();
                y = stack.pop();
                if (opt == 1 ) {
                    value = y - x;//post fix
                }else{
                    value = x -y; // prefix
                }
                stack.push(value);
                break;

            default:

                stack.push(Integer.parseInt(s));
                break;
            }
        }
        catch (Exception e) {
            //e.printStackTrace();
            //System.out.println("Invalid expression.");
            throw e;
        }

    }

    // Method evaluates infix to value
    public void evaluateInFixParant(String s) {
        try {
            //String s = "536+*";
            stack.clear();
            optStack.clear();
            value = 0;

            String str[] = s.split(" ");
            for (int i = 0; i < str.length; i++){
                switch (str[i]){
                    case "+":
                    case "-":
                    case "*":
                    case "/":
                        optStack.push(str[i]);
                        break;


                    case "(":
                        break;
                    case ")":
                        // evaluate top 2 numbers from stack for top 1 operation from optStack
                        calcTop();
                        break;

                    default :
                        // push number stack
                        stack.push(Integer.parseInt(str[i]));
                        break;

                }

            }
            while (optStack.size() > 0){
                calcTop();
            }
            value = stack.pop();
            System.out.println("Here is the completed operation: " + value);

        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Invalid expression.");
        }

    }

    // Method pops top 2 elements from stack and performs operation based on first item on opstack
    public void calcTop() throws Exception{
        try {
            String s = optStack.pop();
            switch (s) {
                case "*":
                    value = stack.pop() * stack.pop();
                    stack.push(value);
                    break;
                case "+":
                    value = stack.pop() + stack.pop();
                    stack.push(value);
                    break;
                case "/":
                    int x1 = stack.pop();
                    int y1 = stack.pop();
                    value = y1 / x1;
                    stack.push(value);
                    break;
                case "-":
                    int x = stack.pop();
                    int y = stack.pop();
                    value = y - x;
                    stack.push(value);
                    break;

                default:

                    stack.push(Integer.parseInt(s));
                    break;
            }
        }
        catch (Exception e) {
            //e.printStackTrace();
            //System.out.println("Invalid expression.");
            throw e;
        }

    }
    //Method checks precedence of string passed
    // ^ 1, */ 2, +- 3 used for evaluating infix
    public int getPrescedence(String p){
        switch (p){
            case "^":
                return 1;
            case "*":
            case "/":
                return 2;

            case "+":
            case "-":
                return 3;

            default:
                return -1;
        }
    }

    // Returns the expression from infix format to postfix format
    public String inFixToPostFix(String exp){
        String[] str = exp.split(" ");
        boolean skipPrecedence = false;
        String newExp = "";
        optStack.clear();
        stack.clear();

        for (int i = 0; i < str.length; i++){
            switch (str[i]){
                case "*":
                case "^":
                case "/":
                case "+":
                case "-":
                    // Check if opstack has anything
                    if (!optStack.isEmpty() && !skipPrecedence){
                        // if it does, peek the value and get its precedence and the current operators precedence
                        String lastOp = optStack.peek();
                        if (getPrescedence(lastOp) < getPrescedence(str[i])){
                            newExp += " " + optStack.pop();
                        }
                        // if the stack has lower precedence then you need to add it
                    }
                    optStack.push(str[i]);

                    break;
                case "(":
                    skipPrecedence = true;
                    break;
                case ")":
                    skipPrecedence = false;
                    break;
                default:
                    //add to new string
                    if (newExp.isEmpty()){
                        newExp += str[i];

                    }
                    else {
                        newExp += " " + str[i];

                    }
                    break;
            }
        }
        while (!optStack.isEmpty()){
            newExp  += " " + optStack.pop();
        }
        return newExp;
    }

    // Returns the expression from postfix format to infix format
    public String postFixToInfix(String exp){
        stack.clear();
        optStack.clear();
        expStack.clear();
        String newExp = "";
        String x = "";
        String y = "";


        String[] str = exp.split(" ");
        for(int i = 0; i < str.length; i++){
            switch (str[i]){
                case "*":
                case "/":
                case "+":
                case "-":
                case "^":
                    x = expStack.pop();
                    y = expStack.pop();

                    newExp = "( " + y + " " + str[i] + " " + x + " )";
                    expStack.push(newExp);

                    break;
                case "(":
                case ")":
                    break;

                default:
                    expStack.push(str[i]);
                    break;


            }
        }
        // Return the last item in the stack thats been computed
        return expStack.pop();
    }

    // Returns the expression from prefix format to infix format
    public String preFixToInFix(String exp){
        stack.clear();
        optStack.clear();
        expStack.clear();
        String newExp = "";
        String x = "";
        String y = "";


        String[] str = exp.split(" ");
        for(int i = str.length-1; i >= 0; i--){
            switch (str[i]){
                case "*":
                case "/":
                case "+":
                case "-":
                case "^":
                    x = expStack.pop();
                    y = expStack.pop();

                    newExp = "( " + x + " " + str[i] + " " + y + " )";
                    expStack.push(newExp);

                    break;
                case "(":
                case ")":
                    break;

                default:
                    expStack.push(str[i]);
                    break;


            }
        }
        // Return the last item in the stack thats been computed
        return expStack.pop();

    }

    // Method Show the menu of options and get user input to process
    public void showMenu(){
        try {
            System.out.println("Menu");
            System.out.println("1. Prefix");
            System.out.println("2. Postfix");
            System.out.println("3. Infix");
            System.out.println("4. Infix to PostFix");
            System.out.println("Choose option 1-6:");
            Scanner sc = new Scanner(System.in);
            int opt = sc.nextInt();

            System.out.println("Input an expression add space between every element & operation eg: 2 * ( 4 / 2 ):");

            Scanner sc1 = new Scanner(System.in);
            String exp = sc1.nextLine();

            System.out.println("Here is expression " + exp + " and here is the option " + opt);
            switch (opt){
                case 1:
                    System.out.println("Prefix to Infix -> " + preFixToInFix(exp));
                    evaluatePreFix(exp);
                    break;
                case 2:
                    System.out.println("Postfix to Infix -> " + postFixToInfix(exp));
                    evaluatePostFix(exp);
                    break;
                case 3:
                    evaluateInFixParant(exp);
                    break;
                case 4:
                    String postfixexp = inFixToPostFix(exp);
                    System.out.println("InFix Express -> " + exp + "  converted to Post Fix Expression ->" + postfixexp);
                    evaluatePostFix(postfixexp);
                    break;
                default:
                    System.out.println("Invalid option selected.");
                    return;
            }
            System.out.println("1. Quit");
            System.out.println("2. Start over");
            int newOpt = sc.nextInt();

            if (newOpt == 1) {
                System.exit(0);
            }
            if (newOpt == 2) {
                return;
            }
        }catch (Exception e ){
            System.out.println("Exception ocurred try again");
            return;
        }

    }


    public static void main(String[] args){
        LabFive fix = new LabFive();

        // Run infinately
        while(true){
            fix.showMenu();
        }
    }
}






