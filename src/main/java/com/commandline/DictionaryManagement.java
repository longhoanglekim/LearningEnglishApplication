package com.commandline;

import java.io.*;
import java.util.Scanner;

/**
 * Manage dictionary.
 */
public class DictionaryManagement {

    /**
     * Insert words from command line to dictionary.
     */
    public static void insertFromCommandline() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Number of words to insert: ");

        int n = sc.nextInt();
        sc.nextLine(); // Remove enter

        for (int i = 0; i < n; i++) {
            String target = sc.nextLine();
            String explain = sc.nextLine();
            Word newWord = new Word(target, explain);
            Dictionary.words.add(newWord);
        }
    }

    /**
     * Insert words from file to dictionary.
     */
    public static void insertFromFile() {
        try {
            File file = new File("src/main/java/com/commandline/demo.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String data = sc.nextLine();

                // Tách chỉ từ đầu tiên trong dòng
                String[] words = data.split(" ", 2);

                // Kiểm tra xem có ít nhất một từ trong dòng
                if (words.length >= 1) {
                    Word newWord = new Word(words[0], words[1]);
                    Dictionary.words.add(newWord);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred. No such file or dictionary.");
            e.printStackTrace();
            System.exit(1);
        }
    }


    /**
     * Look up a word in dictionary.
     * @param word Word to look up.
     * @return Word's explain if found, "Not found" otherwise.
     */
    public static Word dictionaryLookup(String word) {
        for (int i = 0; i < Dictionary.words.size(); i++) {
            String target = Dictionary.words.get(i).getWord_target();
            if (target.equals(word)) {
                return Dictionary.words.get(i);
            }
        }
        return null;
    }

    /**
     * Add a word to dictionary.
     */
    public static void addWord() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Nhập từ muốn thêm:");
        String target = sc.nextLine();
        System.out.println("Nhập nghĩa:");
        String explain = sc.nextLine();
        Word newWord = new Word(target, explain);
        Dictionary.words.add(newWord);
        File file = new File(DictionaryCommandline.FILE_PATH);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.write(target + " " + explain + "\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a word from dictionary.
     */
    public static void deleteWord() {
        System.out.println("Nhap tu muon xoa:");
        Scanner sc = new Scanner(System.in);
        String target = sc.nextLine();
        for (int i = 0; i < Dictionary.size(); i++) {
            String word = Dictionary.words.get(i).getWord_explain();
            if (word.equals(target)) {
                Dictionary.words.remove(i);
                break;
            }
        }
        try {
            // Đọc tất cả các dòng từ tệp văn bản và lưu chúng vào danh sách
            File file = new File(DictionaryCommandline.FILE_PATH);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String dong;
            StringBuilder content = new StringBuilder();

            while ((dong = bufferedReader.readLine()) != null) {
                // Kiểm tra nếu là dòng cần xóa
                if (!dong.contains(target)) {
                    content.append(dong).append("\n");
                }
            }

            bufferedReader.close();

            // Ghi lại tất cả các dòng còn lại vào tệp văn bản
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(content.toString());
            bufferedWriter.close();

            System.out.println("Đã xóa dòng thành công.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Export current local dictionary to txt file.
     */
    public static void dictionaryExportToFile() {
        File new_file = new File("src/main/java/com/commandline/demo.txt");
        if (!new_file.exists()) {
            try {
                new_file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter writer = new FileWriter(new_file);
            for (Word i : Dictionary.words) {
                writer.write(i.getWord_target() + "\t" + i.getWord_explain());
                writer.write("\n");
            }
            writer.close();
            System.out.println("Xuất file thành công.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
