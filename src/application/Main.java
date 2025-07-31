package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;

public class Main extends Application {

    private TableView<Dispositivo> tabela;
    private ObservableList<Dispositivo> listaDispositivos;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Quem está em casa?");

        tabela = new TableView<>();
        tabela.setEditable(true);
        listaDispositivos = FXCollections.observableArrayList();

        TableColumn<Dispositivo, String> nomeColuna = new TableColumn<>("Nome");
        nomeColuna.setEditable(true);
        nomeColuna.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNome()));
        nomeColuna.setCellFactory(column -> {
            TextFieldTableCell<Dispositivo, String> cell = new TextFieldTableCell<>(new DefaultStringConverter()) {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        setStyle("-fx-text-fill: #33bbff; -fx-font-weight: bold;");
                    }
                }
            };
            return cell;
        });
        nomeColuna.setOnEditCommit(event -> {
            Dispositivo d = event.getRowValue();
            d.setNome(event.getNewValue());
        });

        TableColumn<Dispositivo, String> ipColuna = new TableColumn<>("IP");
        ipColuna.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIp()));
        ipColuna.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: #00ff00; -fx-font-weight: bold;");
                }
            }
        });
        ipColuna.setPrefWidth(100);

        TableColumn<Dispositivo, String> macColuna = new TableColumn<>("MAC");
        macColuna.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMac()));
        macColuna.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: #cc99ff;");
                }
            }
        });
        macColuna.setPrefWidth(120);

        TableColumn<Dispositivo, String> descColuna = new TableColumn<>("Descrição");
        descColuna.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescricao()));
        descColuna.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: #cccccc; -fx-font-style: italic;");
                }
            }
        });
        descColuna.setPrefWidth(315);

        TableColumn<Dispositivo, Void> acaoColuna = new TableColumn<>("Identificar");
        acaoColuna.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Identificar");

            {
                btn.setOnAction(event -> {
                    Dispositivo d = getTableView().getItems().get(getIndex());
                    String resultado = RedeScanner.identificar(d.getIp(), d.getMac());
                    d.setDescricao(resultado);
                    tabela.refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean vazio) {
                super.updateItem(item, vazio);
                if (vazio) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        tabela.getColumns().addAll(nomeColuna, ipColuna, macColuna, descColuna, acaoColuna);
        tabela.setItems(listaDispositivos);

        Button btnEscanear = new Button("Escanear");
        btnEscanear.setOnAction(event -> {
            listaDispositivos.setAll(RedeScanner.escanearRede());
        });

        HBox hBox = new HBox(10, btnEscanear);
        hBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(tabela);
        root.setBottom(hBox);

        Scene scene = new Scene(root, 700, 400);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
