package com.example.lab8;


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

@SuppressWarnings("ALL")
public class TableWithDataController implements Initializable {
    public static final int MS_TIMEOUT = 100;
    public static final int TIMEOUT = 5000;
    public static MySQLDataBase db;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<BreakingBadPhrase> TableData;

    @FXML
    private TableColumn<BreakingBadPhrase, String> author;

    @FXML
    private TableColumn<BreakingBadPhrase, String> quote;

    @FXML
    private TableColumn<BreakingBadPhrase, Integer> quoteId;

    @FXML
    private TableColumn<BreakingBadPhrase, String> series;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    private static String getStringFromResultSet(ArrayList result) {
        StringBuilder str = new StringBuilder();

        for (Object row : result) {
            for (String column : (String[]) row) {
                str.append(column).append("\t\t");
            }
            str.append("\n");
        }
        return str.toString();
    }

    public void loadDataBase(ActionEvent actionEvent) throws SQLException {
        baseLoad();
        JSONGetter jsonGetter = new JSONGetter();
        JSONGetter.url = "https://breakingbadapi.com/api/quotes";
        jsonGetter.run();
        int msForWaiting = 0;
        do {
            msForWaiting += MS_TIMEOUT;
            try {
                Thread.sleep(MS_TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (msForWaiting <= TIMEOUT && jsonGetter.jsonIn == "");
        if (msForWaiting >= TIMEOUT) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось загрузить данные");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    Platform.exit();
                }
            });
        }

        Phrases phrases = new Phrases();
        phrases.ReadAndParseToList(jsonGetter);

        try {
            db.delete("DELETE FROM phrases");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "INSERT INTO phrases(quoteId, quote, author, series) VALUES (?,?,?,?)";

        for (int i = 0; i < phrases.getPhrases().size(); i++) {
            try {
                db.insert(query, i, phrases);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        quoteId.setCellValueFactory(new PropertyValueFactory<BreakingBadPhrase, Integer>("quoteId"));
        quote.setCellValueFactory(new PropertyValueFactory<BreakingBadPhrase, String>("quote"));
        author.setCellValueFactory(new PropertyValueFactory<BreakingBadPhrase, String>("author"));
        series.setCellValueFactory(new PropertyValueFactory<BreakingBadPhrase, String>("series"));

        TableData.setItems(phrases.getPhrases());
    }

    public void baseLoad() throws SQLException {
        db = new MySQLDataBase("breakingbadphrases");
        System.out.println(db.Connect() ? "Connected successfully" : "Connection failed");
        useDB();
    }

    public static void useDB() throws SQLException {
        db.executeQuery("use " + db.getDBname() + ";");
    }

    public void onTextInputFinish(ActionEvent actionEvent) throws SQLException {
        baseLoad();
        searchByAuthor();
    }

    public void searchByAuthor() throws SQLException {
        TableData.getItems().clear();
        Statement st = db.connection.createStatement();
        ResultSet rs;
        rs = st.executeQuery("select * from phrases where author like" + "'%" + searchField.getText() + "%'" + " ;");

        ObservableList<BreakingBadPhrase> data = FXCollections.observableArrayList();

        quoteId.setCellValueFactory(new PropertyValueFactory<BreakingBadPhrase, Integer>("quoteId"));
        quote.setCellValueFactory(new PropertyValueFactory<BreakingBadPhrase, String>("quote"));
        author.setCellValueFactory(new PropertyValueFactory<BreakingBadPhrase, String>("author"));
        series.setCellValueFactory(new PropertyValueFactory<BreakingBadPhrase, String>("series"));

        while (rs.next())
        {
            data.add(new BreakingBadPhrase(rs.getInt("quoteId"), rs.getString("quote"), rs.getString("author"), rs.getString("series")));
        }

        TableData.setItems(data);
        rs.close();
        st.close();
    }
}