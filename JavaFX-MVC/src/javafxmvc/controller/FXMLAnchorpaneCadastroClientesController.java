package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.ClienteDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Cliente;

public class FXMLAnchorpaneCadastroClientesController implements Initializable {
	@FXML
	private TableView<Cliente> tblCliente;

	@FXML
	private TableColumn<Cliente, String> tcNome;

	@FXML
	private TableColumn<Cliente, String> tcCPF;

	@FXML
	private Button btnInserir;

	@FXML
	private Button btnRemover;

	@FXML
	private Button btnAlterar;

	@FXML
	private Label lblCliFone;

	@FXML
	private Label lblCliNome;

	@FXML
	private Label lblCliCodigo;

	@FXML
	private Label lblCliCPF;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		clienteDAO.setConnection(connection);
		carregarTblClientes();

		tblCliente.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> selecionarItemTableViewClientes(newValue));
	}


	public void selecionarItemTableViewClientes(Cliente cliente){
        if (cliente != null) {
            lblCliCodigo.setText(String.valueOf(cliente.getCdCliente()));
            lblCliNome.setText(cliente.getNome());
            lblCliCPF.setText(cliente.getCpf());
            lblCliFone.setText(cliente.getTelefone());
        } else {
        	lblCliCodigo.setText("");
        	lblCliNome.setText("");
        	lblCliCPF.setText("");
        	lblCliFone.setText("");
        }
	}

	private List<Cliente> listClientes;
	private ObservableList<Cliente> observableListClientes;

	private final Database database = DatabaseFactory.getDatabase("postgresql");
	private final Connection connection = database.conectar();
	private final ClienteDAO clienteDAO = new ClienteDAO();

	public void carregarTblClientes() {
		tcNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tcCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));

		listClientes = clienteDAO.listar();

		observableListClientes = FXCollections.observableArrayList(listClientes);
		tblCliente.setItems(observableListClientes);

	}

}
