package com.example.palindrome;

public final class PalindromeWithTwoPointers {
    private PalindromeWithTwoPointers() { /* utility */ }

    /**
     * Implement a method that uses the two-pointer technique
     *
     * @param s input string (must not be null)
     * @return true if palindrome
     * @throws IllegalArgumentException if s is null
     */


    public static boolean isPalindrome(String s) {
        if(s==null){throw new IllegalArgumentException("Missing String");}
        boolean Palindrome = true;
        String str = s;
        str = normalize(s);
        if (str.isEmpty()) { return true; }
        else {
            for (int i = 0; i < (str.length() / 2); i++) {
                if (str.charAt(i) != str.charAt(str.length() - 1 - i)) {
                    Palindrome = false;
                }
            }
        }
        return Palindrome;
    }

    public static void main(String[] args) {
        PalindromeWithTwoPointers palindrome = new PalindromeWithTwoPointers();
        System.out.println(palindrome.isPalindrome("abc"));
        PalindromeWithTwoPointers palindrome1 = new PalindromeWithTwoPointers();
        System.out.println(palindrome1.isPalindrome("madam"));
    }


    // package-private for testing if needed
    static String normalize(String s) {
        String toReturn = null;
        if(s!=null) {
            StringBuilder sb = new StringBuilder(s.length());
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (Character.isLetterOrDigit(c)) {
                    sb.append(Character.toLowerCase(c));
                }
            }
            toReturn = sb.toString();
        }
        return toReturn;
    }
}

