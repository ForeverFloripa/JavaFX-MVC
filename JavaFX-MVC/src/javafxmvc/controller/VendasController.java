package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafxmvc.model.dao.ItemDeVendaDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.dao.VendaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.ItemDeVenda;
import javafxmvc.model.domain.Produto;
import javafxmvc.model.domain.Venda;

public class VendasController implements Initializable{

	@FXML
	private TableView<Venda> tblVendas;

	@FXML
	private TableColumn<Venda, Integer> tcVendaCodgo;

	@FXML
	private TableColumn<Venda, LocalDate> tcVendaData;

	@FXML
	private TableColumn<Venda, Venda> tcVendaCliente;

	@FXML
	private GridPane gpVendas;

	@FXML
	private Label lblVendaPago;

	@FXML
	private Label lblVendaData;

	@FXML
	private Label lblVendaCodigo;
	@FXML
	private Label lblVendaValor;

	@FXML
	private Label lblVendaCliente;

	@FXML
	private Button btnVInserir;

	@FXML
	private Button btnVAlterar;

	@FXML
	private Button btnVRemover;
	
	
	
	
	private List<Venda> listVendas;
    private ObservableList<Venda> observableListVendas;

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final VendaDAO vendaDAO = new VendaDAO();
    private final ItemDeVendaDAO itemDeVendaDAO = new ItemDeVendaDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		vendaDAO.setConnection(connection);
        carregarTblVendas();
        
        selecionarItemTblVendas(null);

        // Listen acionado diante de quaisquer alterações na seleção de itens do TableView
        tblVendas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTblVendas(newValue));
		
		
	}
	
	public void selecionarItemTblVendas(Venda venda) {
        if (venda != null) {
            lblVendaCodigo.setText(String.valueOf(venda.getCdVenda()));
            lblVendaData.setText(String.valueOf(venda.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            lblVendaValor.setText(String.format("%.2f", venda.getValor()));
            lblVendaPago.setText(String.valueOf(venda.getPago()));
            lblVendaCliente.setText(venda.getCliente().toString());
        } else {
        	lblVendaCodigo.setText("");
        	lblVendaData.setText("");
        	lblVendaValor.setText("");
        	lblVendaPago.setText("");
        	lblVendaCliente.setText("");
        }
    }

    public void carregarTblVendas() {
        tcVendaCodgo.setCellValueFactory(new PropertyValueFactory<>("cdVenda"));
        tcVendaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tcVendaCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));

        listVendas = vendaDAO.listar();

        observableListVendas = FXCollections.observableArrayList(listVendas);
        tblVendas.setItems(observableListVendas);
    }
    
    @FXML
    public void handleButtonInserir() throws IOException, SQLException {
        Venda venda = new Venda();
        List<ItemDeVenda> listItensDeVenda = new ArrayList<>();
        venda.setItensDeVenda(listItensDeVenda);
        boolean buttonConfirmarClicked = showFXMLDialogVendas(venda);
        if (buttonConfirmarClicked) {
            try {
                connection.setAutoCommit(false);
                vendaDAO.setConnection(connection);
                vendaDAO.inserir(venda);
                itemDeVendaDAO.setConnection(connection);
                produtoDAO.setConnection(connection);
                for (ItemDeVenda listItemDeVenda : venda.getItensDeVenda()) {
                    Produto produto = listItemDeVenda.getProduto();
                    listItemDeVenda.setVenda(vendaDAO.buscarUltimaVenda());
                    itemDeVendaDAO.inserir(listItemDeVenda);
                    produto.setQuantidade(produto.getQuantidade() - listItemDeVenda.getQuantidade());
                    produtoDAO.alterar(produto);
                }
                connection.commit();
                carregarTblVendas();
            } catch (SQLException ex) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(VendasController.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(VendasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    @FXML
    public void handleButtonRemover() throws IOException, SQLException {
        Venda venda = tblVendas.getSelectionModel().getSelectedItem();
        if (venda != null) {
            connection.setAutoCommit(false);
            vendaDAO.setConnection(connection);
            itemDeVendaDAO.setConnection(connection);
            produtoDAO.setConnection(connection);
            for (ItemDeVenda listItemDeVenda : venda.getItensDeVenda()) {
                Produto produto = listItemDeVenda.getProduto();
                produto.setQuantidade(produto.getQuantidade() + listItemDeVenda.getQuantidade());
                produtoDAO.alterar(produto);
                itemDeVendaDAO.remover(listItemDeVenda);
            }
            vendaDAO.remover(venda);
            connection.commit();
            carregarTblVendas();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma venda na Tabela!");
            alert.show();
        }
    }
    
    
    public boolean showFXMLDialogVendas(Venda venda) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DialogVendasController.class.getResource("/javafxmvc/view/FXMLDialogVendas.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Registro de Vendas");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        // Setando a Venda no Controller.
        DialogVendasController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVenda(venda);
        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();
        return controller.isButtonConfirmarClicked();
    }

}
