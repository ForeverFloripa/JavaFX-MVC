package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafxmvc.model.dao.ClienteDAO;
import javafxmvc.model.dao.ItemDeVendaDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.dao.VendaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Cliente;
import javafxmvc.model.domain.ItemDeVenda;
import javafxmvc.model.domain.Produto;
import javafxmvc.model.domain.Venda;

public class DialogVendasController implements Initializable {
	@FXML
	private ComboBox<Cliente> cbbVClientes;

	@FXML
	private DatePicker dtpVData;

	@FXML
	private CheckBox chkVPago;

	@FXML
	private ComboBox<Produto> cbbVProduto;

	@FXML
	private TextField txtIVQuantdd;

	@FXML
	private Button btnVAdicionar;

	@FXML
	private TableView<ItemDeVenda> tblVItemVenda;

	@FXML
	private TableColumn<ItemDeVenda, Produto> tcIVProduto;

	@FXML
	private TableColumn<ItemDeVenda, Integer> tcIVQtdd;

	@FXML
	private TableColumn<ItemDeVenda, Double> tcIVValor;

	@FXML
	private TextField txtVValor;

	@FXML
	private Button btnConfirmar;

	@FXML
	private Button btnCancelar;

	private List<Cliente> listClientes;
	private List<Produto> listProdutos;
	private ObservableList<Cliente> observableListClientes;
	private ObservableList<Produto> observableListProdutos;
	private ObservableList<ItemDeVenda> observableListItensDeVenda;

	// Atributos para manipulação de Banco de Dados
	private final Database database = DatabaseFactory.getDatabase("postgresql");
	private final Connection connection = database.conectar();
	private final ClienteDAO clienteDAO = new ClienteDAO();
	private final ProdutoDAO produtoDAO = new ProdutoDAO();

	private Stage dialogStage;
	private boolean buttonConfirmarClicked = false;
	private Venda venda;

	public Stage getDialogStage() {
		return dialogStage;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isButtonConfirmarClicked() {
		return buttonConfirmarClicked;
	}

	public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
		this.buttonConfirmarClicked = buttonConfirmarClicked;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		clienteDAO.setConnection(connection);
		produtoDAO.setConnection(connection);
		carregarComboBoxClientes();
		carregarComboBoxProdutos();
		tcIVProduto.setCellValueFactory(new PropertyValueFactory<>("produto"));
		tcIVQtdd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tcIVValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
	}

	public void carregarComboBoxClientes() {
		listClientes = clienteDAO.listar();
		observableListClientes = FXCollections.observableArrayList(listClientes);
		cbbVClientes.setItems(observableListClientes);
	}

	public void carregarComboBoxProdutos() {
		listProdutos = produtoDAO.listar();
		observableListProdutos = FXCollections.observableArrayList(listProdutos);
		cbbVProduto.setItems(observableListProdutos);
	}
	
	
	 @FXML
	    public void handleButtonAdicionar() {
	        Produto produto;
	        ItemDeVenda itemDeVenda = new ItemDeVenda();
	        if (cbbVProduto.getSelectionModel().getSelectedItem() != null) {
	            produto = (Produto) cbbVProduto.getSelectionModel().getSelectedItem();
	            System.out.println("Produto: "+ produto);
	            if (produto.getQuantidade() >= Integer.parseInt(txtIVQuantdd.getText())) {
	                itemDeVenda.setProduto((Produto) cbbVProduto.getSelectionModel().getSelectedItem());
	                itemDeVenda.setQuantidade(Integer.parseInt(txtIVQuantdd.getText()));
	                itemDeVenda.setValor(itemDeVenda.getProduto().getPreco() * itemDeVenda.getQuantidade());
	                venda.getItensDeVenda().add(itemDeVenda);
	                venda.setValor(venda.getValor() + itemDeVenda.getValor());
	                observableListItensDeVenda = FXCollections.observableArrayList(venda.getItensDeVenda());
	                tblVItemVenda.setItems(observableListItensDeVenda);
	                txtVValor.setText(String.format("%.2f", venda.getValor()));
	            } else {
	                Alert alert = new Alert(Alert.AlertType.ERROR);
	                alert.setHeaderText("Problemas na escolha do produto!");
	                alert.setContentText("Não existe a quantidade de produtos disponíveis no estoque!");
	                alert.show();
	            }
	        }
	    }

	    @FXML
	    public void handleButtonConfirmar() {
	        if (validarEntradaDeDados()) {
	            venda.setCliente((Cliente) cbbVClientes.getSelectionModel().getSelectedItem());
	            venda.setPago(chkVPago.isSelected());
	            venda.setData(dtpVData.getValue());
	            buttonConfirmarClicked = true;
	            dialogStage.close();
	        }
	    }

	    @FXML
	    public void handleButtonCancelar() {
	        getDialogStage().close();
	    }

	    //Validar entrada de dados para o cadastro
	    private boolean validarEntradaDeDados() {
	        String errorMessage = "";
	        if (cbbVClientes.getSelectionModel().getSelectedItem() == null) {
	            errorMessage += "Cliente inválido!\n";
	        }
	        if (dtpVData.getValue() == null) {
	            errorMessage += "Data inválida!\n";
	        }
	        if (observableListItensDeVenda == null) {
	            errorMessage += "Itens de Venda inválidos!\n";
	        }
	        if (errorMessage.length() == 0) {
	            return true;
	        } else {
	            // Mostrando a mensagem de erro
	            Alert alert = new Alert(Alert.AlertType.ERROR);
	            alert.setTitle("Erro no cadastro");
	            alert.setHeaderText("Campos inválidos, por favor, corrija...");
	            alert.setContentText(errorMessage);
	            alert.show();
	            return false;
	        }
	    }

	
	

}
