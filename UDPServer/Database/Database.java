package Database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;


class User {
    int uid;
    String username, password, group;

    public User(int uid, String username, String password, String group) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.group = group;
    }
}

class Word {
    int idx;
    String wordName, wordType, meaning;

    Word(int idx, String wordName, String wordType, String meaning) {
        this.idx = idx;
        this.wordName = wordName;
        this.wordType = wordType;
        this.meaning = meaning;
    }
}

public class Database {
    private Connection conn = connect();
    private final static Logger logger = Logger.getLogger("virtualDB");
    String SQL_FILE_PATH = "./SQLite";


    public Database() throws SQLException {
    }

    private Connection connect() throws SQLException {

        Connection conn;
        try {
            String url = "jdbc:sqlite:" + SQL_FILE_PATH;
            conn = DriverManager.getConnection(url);
            logger.info("Connection to SQLite has been established.");

        } catch (SQLException e) {
            logger.warning(e.getMessage());
            throw e;
        }
        return conn;
    }

    private void closeConn(Connection conn) throws SQLException {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
            throw e;
        }
        logger.info("[*] connection closed");
    }

    private List<Word> queryWord(String wordName) throws SQLException {
        List<Word> wordList = new ArrayList<Word>();
        Word word = new Word(0, null, null, null);
        String sql = "select * from words where wordName=\"" + wordName + '"';
        try (
                Statement stmt = this.conn.createStatement();
                ResultSet res = stmt.executeQuery(sql)) {
            while (res.next()) {
                word.idx = res.getInt("idx");
                word.wordName = res.getString("wordName");
                word.wordType = res.getString("wordType");
                word.meaning = res.getString("meaning");
                wordList.add(word);
            }


        } catch (SQLException e) {
            logger.warning(e.getMessage());
            throw e;
        }
        logger.info("[-] queried word: " + wordName + ". ");
        return wordList;
    }


    private Boolean addWord(String wordName, String wordType, String meaning) throws SQLException {
        String sql = "insert into words (wordName,wordType,meaning) values(?,?,?)";
        try (
                PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setString(1, wordName);
            pstmt.setString(2, wordType);
            pstmt.setString(3, meaning);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
            throw e;
        }
        logger.info("[-] added word: " + wordName + ". ");
        return true;
    }

    private Boolean editWord(int idx, String wordName, String wordType, String meaning) throws SQLException {
        String sql = "update words set wordName = ? , "
                + "wordType = ? ,  "
                + "meaning = ? "
                + "where idx = ?";

        try (
                PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setString(1, wordName);
            pstmt.setString(2, wordType);
            pstmt.setString(3, meaning);
            pstmt.setInt(4, idx);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
            throw e;
        }
        logger.info("[-] edited word: " + wordName + ". ");
        return true;
    }

    private Boolean removeWord(int idx) throws SQLException {
        String sql = "delete from words where idx = ?";

        try (
                PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setInt(1, idx);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
            throw e;
        }
        logger.info("[-] removed word: " + idx + ". ");
        return true;
    }

    public static void main(String[] args) {
        // write your code here
//        Database db = new Database();
//        db.queryWord("apple");
//        db.addWord("banana", "noun", "another kind of fruit");
//        db.editWord(3, "banana", "noun", "very different from apple");
//        db.removeWord(3);
//        db.closeConn(db.conn);
    }
}
