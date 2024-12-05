/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

*******************************************************************************/
package driver;

import java.util.Stack;
import java.util.ArrayList;

// token class to represent the tokens 
class Token 
{
    private String type;
    private String value;

    public Token(String type, String value) 
    {
        this.type = type;
        this.value = value;
    }

    public String getType() 
    {
        return type;
    }

    public String getValue() 
    {
        return value;
    }
}

// TokenList class to represent a list of tokens
class TokenList extends ArrayList<Token> 
{
    //literally just to inherit from Arraylist
}

// Shunting Yard class
public class ShuntingYard 
{
    //declare vars to use
    public static String Operators = "+-*/^()";
    public static String Numbers = "1234567890";

    // checks  if a string is number
    public static boolean IsNumber(String input) 
    {
        for (char c : input.toCharArray()) 
        {
            if (Numbers.indexOf(c) == -1) 
            {
                return false;
            }
        }
        return true;
    }

    // returns the precedence of operators 
    public static int getPrecedence(String input) 
    {
        char op = input.charAt(0);
        
        switch (op) 
        {
            case '(':
                return 1;
                
            case '+':
                
            case '-':
                return 2;
                
            case '*':
                
            case '/':
                return 3;
                
            case '^':
                return 4;
                
            case ')':
                return 5;
                
            default:
                return 0;
                
                
        }
    }

    // prase the expression into a list of tokens
    public static TokenList ParseFromExp(String exp) 
    {
        TokenList lst = new TokenList();
        
        StringBuilder token = new StringBuilder();

        for (int i = 0; i < exp.length(); i++) 
        {
            char c = exp.charAt(i);

            // handle numbers
            if (Character.isDigit(c)) 
            {
                token.append(c);
            } 
            else 
            {
                if (token.length() > 0) 
                {
                    lst.add(new Token("NUMBER", token.toString()));
                    token.setLength(0);
                }
                // andle operators and parentheses
                if (Operators.indexOf(c) != -1) 
                {
                    lst.add(new Token("OPERATOR", String.valueOf(c)));
                }
            }
        }

        if (token.length() > 0) 
        {
            lst.add(new Token("NUMBER", token.toString()));
        }

        return lst;
    }

    // converts an to reverse polisnh 
    public static TokenList BuildFromTokens(TokenList tokenList) 
    {
        TokenList outputQueue = new TokenList();
        Stack<Token> operatorStack = new Stack<>();

        for (Token token : tokenList) 
        {
            String type = token.getType();
            String value = token.getValue();

            if (type.equals("NUMBER")) 
            {
                outputQueue.add(token); // add numbers to output queue
            } 
            else if (type.equals("OPERATOR")) 
            {
                while (!operatorStack.isEmpty() && getPrecedence(operatorStack.peek().getValue()) >= getPrecedence(value)) 
                {
                    outputQueue.add(operatorStack.pop()); // pop operators with higher precedence
                }
                operatorStack.push(token); // push the curr onto stack
            } 
            else if (value.equals("(")) 
            {
                operatorStack.push(token); 
            } 
            else if (value.equals(")")) 
            {
                while (!operatorStack.peek().getValue().equals("(")) 
                {
                    outputQueue.add(operatorStack.pop()); // popp operators until left parenthesis
                }
                operatorStack.pop(); // pop the left parenthesies 
            }
        }

        // pop rest from stack
        while (!operatorStack.isEmpty()) 
        {
            outputQueue.add(operatorStack.pop());
        }

        return outputQueue;
    }

    // RPN 
    public static int Process(TokenList queue) 
    {
        Stack<Integer> stack = new Stack<>();

        for (Token token : queue) 
        {
            String type = token.getType();
            String value = token.getValue();

            if (type.equals("NUMBER")) 
            {
                stack.push(Integer.parseInt(value)); // push numbers onto the stac
            } 
            else if (type.equals("OPERATOR")) 
            {
                // assign vars
                int b = stack.pop();
                int a = stack.pop();
                int result = 0;

                //switch cases
                switch (value) 
                {
                    case "+":
                        result = a + b;
                        break;
                        
                    case "-":
                        result = a - b;
                        break;
                        
                    case "*":
                        result = a * b;
                        break;
                        
                    case "/":
                        result = a / b;
                        break;
                        
                    case "^":
                        result = (int) Math.pow(a, b);
                        break;
                        
                }
                // push val onto the stack
                stack.push(result); 
            }
        }

        return stack.pop(); // The result is the only value left in the stack
    }

    // int main equivalent 
    public static void main(String[] args) 
    {
        String expression = "3 + 5 * (2 - 8)";
        TokenList tokens = ParseFromExp(expression); 
        TokenList rpn = BuildFromTokens(tokens); 
        int result = Process(rpn); 
        System.out.println("Result: " + result); 
    }
}
