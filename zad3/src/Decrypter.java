import java.util.*;

class Decrypter implements DecrypterInterface {

    private Map<Character, Character> decode = new HashMap<Character, Character>();
    private Map<Character, Character> code = new HashMap<Character, Character>();
    private final String correct = "WydziałFizyki,AstronomiiiInformatykiStosowanej";
    private final int[] size = {7, 7, 10, 1, 11, 10};


    private String replace(String message) {
        message = message.replaceAll("\t", " ");
        message = message.replaceAll("\n", " ");
        message = message.replaceAll("\\s+", " ");
        return message;
    }

    private void check(List<String> words) {
        if (words.size() < 6) return;

        StringBuilder mayby = new StringBuilder();
        List<Character> tmp = new ArrayList<>();

        for (int i = 0; i <= words.size() - 6; i++) {
            for (int j = i; j < i + 6; j++) {
                if (words.get(j).length() == size[j - i]) {
                    mayby.append(words.get(j));
                } else {
                    mayby = new StringBuilder();
                    break;
                }
            }

            if (mayby.length() == correct.length()) {

                for (int j = 0; j < correct.length(); j++) {
                    if (tmp.contains(mayby.charAt(j)) == true) {
                        if (decode.get(mayby.charAt(j)) != correct.charAt(j)) {
                            decode.clear();
                            code.clear();
                            tmp.clear();
                            break;
                        }
                    } else {
                        if(code.containsKey(correct.charAt(j)) == true && code.get(correct.charAt(j))!=mayby.charAt(j)){
                            decode.clear();
                            code.clear();
                            tmp.clear();
                            break;
                        }
                        tmp.add(mayby.charAt(j));
                        decode.put(mayby.charAt(j), correct.charAt(j));
                        code.put(correct.charAt(j), mayby.charAt(j));
                    }
                }
            }
            if(decode.size() != code.size()){
                decode.clear();
                code.clear();
                tmp.clear();
                continue;
            }

            if (decode.size() != 0) {
                decode.remove(',');
                code.remove(',');
                return;
            }
        }
    }

    @Override
    public void setInputText(String encryptedDocument) {
        decode.clear();
        code.clear();

        if (encryptedDocument != null) {
            String message;
            message = replace(encryptedDocument);
            List<String> words = Arrays.asList(message.split(" "));
            check(words);
        }
    }

    @Override
    public Map<Character, Character> getCode() {
        return code;
    }

    @Override
    public Map<Character, Character> getDecode() {
        return decode;
    }
}