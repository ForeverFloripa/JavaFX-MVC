package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafxmvc.model.dao.ItemDeVendaDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.dao.VendaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
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
	private GridPane lblVendaValor;

	@FXML
	private Label lblVendaPago;

	@FXML
	private Label lblVendaData;

	@FXML
	private Label lblVendaCodigo;

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
		// TODO Auto-generated method stub
		
	}

}
