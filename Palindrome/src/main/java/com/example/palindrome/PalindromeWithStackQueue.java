package com.example.palindrome;

public class PalindromeWithStackQueue {

    /**
     * Implement a method that uses a Stack and a Queue
     * to determine whether the input string is a palindrome.
     * Palindromes read the same forwards and backwards,
     * ignoring case and non-alphanumeric characters.
     * Use Stack and Queue provided by Java's standard library.
     */


    public static boolean isPalindrome(String str) {
        boolean Palindrome = true;

        if(str==null){throw new IllegalArgumentException("Missing string");}
        StackInterface<Character> stack = new ArrayStack<>(str.length());
        QueueInterface<Character> queue = new ArrayQueue<>(str.length());

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isLetter(ch)) {
                ch = Character.toLowerCase(ch);
                stack.push(ch);
                queue.enqueue(ch);
            }
        }

        while (!stack.isEmpty()&&!queue.isEmpty()) {
            char ch1 = stack.peek();
            char ch2 = queue.peek();
            stack.pop();
            queue.dequeue();
            if (ch1 != ch2) {
                Palindrome = false;
                break;
            }
        }
        return Palindrome;
    }
        public static void main(String[] args) {
            PalindromeWithStackQueue palindrome = new PalindromeWithStackQueue();
            System.out.println(palindrome.isPalindrome("abc"));
            PalindromeWithStackQueue palindrome1 = new PalindromeWithStackQueue();
            System.out.println(palindrome1.isPalindrome("madam"));
        }

}
