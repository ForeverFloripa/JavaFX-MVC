package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.stage.Stage;
import javafxmvc.model.dao.ClienteDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Cliente;

public class ClientesController implements Initializable {
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
				.addListener((observable, oldValue, newValue) -> selecionarItemTblClientes(newValue));
	}


	public void selecionarItemTblClientes(Cliente cliente){
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
	
	
	@FXML
    public void handleButtonInserir() throws IOException {
        Cliente cliente = new Cliente();
        boolean buttonConfirmarClicked = showFXMLDialogClientes(cliente);
        if (buttonConfirmarClicked) {
            clienteDAO.inserir(cliente);
            carregarTblClientes();
        }
    }

    


	@FXML
    public void handleButtonAlterar() throws IOException {
        Cliente cliente = tblCliente.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            boolean buttonConfirmarClicked = showFXMLDialogClientes(cliente);
            if (buttonConfirmarClicked) {
                clienteDAO.alterar(cliente);
                carregarTblClientes();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um cliente na Tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonRemover() throws IOException {
        Cliente cliente = tblCliente.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            clienteDAO.remover(cliente);
            carregarTblClientes();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um cliente na Tabela!");
            alert.show();
        }
    }
	
    
    @FXML
    private boolean showFXMLDialogClientes(Cliente cliente) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DialogClientesController.class.getResource("/javafxmvc/view/FXMLDialogClientes.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Clientes");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o cliente no Controller.
        DialogClientesController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCliente(cliente);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
	}


	

	
	

}
