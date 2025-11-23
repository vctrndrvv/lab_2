public class lab2 {

    private static final char[] ALPH;

    static {
        String eng = "abcdefghijklmnopqrstuvwxyz";
        String rus = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        ALPH = (eng + rus).toCharArray();
    }

    private static int idxOf(char c) {
        c = Character.toLowerCase(c);
        for (int i = 0; i < ALPH.length; i++) {
            if (ALPH[i] == c) return i;
        }
        return -1;
    }

    private static class N {
        N[] ch = new N[ALPH.length];
        boolean isW;
    }

    private final N root = new N();

    public void insert(String w) {
        N node = root;
        for (int i = 0; i < w.length(); i++) {
            int idx = idxOf(w.charAt(i));
            if (idx == -1) return;

            if (node.ch[idx] == null) {
                node.ch[idx] = new N();
            }
            node = node.ch[idx];
        }
        node.isW = true;
    }

    public boolean contains(String w) {
        N node = findNode(w);
        return node != null && node.isW;
    }

    public boolean startsWith(String p) {
        return findNode(p) != null;
    }

    public boolean delete(String w) {
        return deleteRec(root, w.toLowerCase(), 0);
    }

    private boolean deleteRec(N node, String w, int depth) {
        if (node == null) return false;

        if (depth == w.length()) {
            if (!node.isW) return false;
            node.isW = false;
            return noChildren(node);
        }

        int idx = idxOf(w.charAt(depth));
        if (idx == -1) return false;

        N child = node.ch[idx];
        if (child == null) return false;

        boolean shouldDeleteChild = deleteRec(child, w, depth + 1);

        if (shouldDeleteChild) {
            node.ch[idx] = null;
            return !node.isW && noChildren(node);
        }
        return false;
    }

    private boolean noChildren(N node) {
        for (N child : node.ch) {
            if (child != null) return false;
        }
        return true;
    }

    public String[] getByPrefix(String p) {
        N node = findNode(p);
        if (node == null) return new String[0];

        DynArr result = new DynArr();
        StringBuilder sb = new StringBuilder(p.toLowerCase());

        dfs(node, sb, result);

        return result.toArray();
    }

    private N findNode(String s) {
        N node = root;
        for (int i = 0; i < s.length(); i++) {
            int idx = idxOf(s.charAt(i));
            if (idx == -1) return null;

            node = node.ch[idx];
            if (node == null) return null;
        }
        return node;
    }

    private void dfs(N node, StringBuilder sb, DynArr arr) {
        if (node.isW) arr.add(sb.toString());

        for (int i = 0; i < ALPH.length; i++) {
            if (node.ch[i] != null) {
                sb.append(ALPH[i]);
                dfs(node.ch[i], sb, arr);
                sb.deleteCharAt(sb.length() - 1);
            }
        }
    }

    private static class DynArr {
        private String[] data = new String[10];
        private int size = 0;

        void add(String s) {
            if (size == data.length) {
                String[] newArr = new String[data.length * 2];
                System.arraycopy(data, 0, newArr, 0, data.length);
                data = newArr;
            }
            data[size++] = s;
        }

        String[] toArray() {
            String[] res = new String[size];
            System.arraycopy(data, 0, res, 0, size);
            return res;
        }
    }

    public static void main(String[] args) {
        lab2 trie = new lab2();
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        while (true) {
            System.out.println("1. Вставить слово");
            System.out.println("2. Проверить слово");
            System.out.println("3. Удалить слово");
            System.out.println("4. Найти по префиксу");
            System.out.println("5. Выход");
            System.out.print("Введите номер: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Введите слово: ");
                    trie.insert(scanner.nextLine());
                    System.out.println("Слово вставлено.");
                    break;

                case 2:
                    System.out.print("Введите слово: ");
                    System.out.println("Есть? " + trie.contains(scanner.nextLine()));
                    break;

                case 3:
                    System.out.print("Введите слово для удаления: ");
                    boolean deleted = trie.delete(scanner.nextLine());
                    System.out.println(deleted ? "Удалено." : "Слово не найдено.");
                    break;

                case 4:
                    System.out.print("Введите префикс: ");
                    String[] words = trie.getByPrefix(scanner.nextLine());
                    if (words.length == 0) System.out.println("Нет слов с таким префиксом.");
                    else {
                        System.out.println("Найдены:");
                        for (String w : words) System.out.println(w);
                    }
                    break;

                case 5:
                    System.out.println("Выход.");
                    return;

                default:
                    System.out.println("Неверный выбор.");
            }
        }
    }
}
