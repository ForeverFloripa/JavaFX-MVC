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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Categoria;
import javafxmvc.model.domain.Produto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class RelatorioController implements Initializable {

	@FXML
	private TableView<Produto> tblProdutos;

	@FXML
	private TableColumn<Produto, Integer> tcCodigo;

	@FXML
	private TableColumn<Produto, String> tcNome;

	@FXML
	private TableColumn<Produto, Double> tcPreco;

	@FXML
	private TableColumn<Produto, Integer> tcQuantidade;

	@FXML
	private TableColumn<Produto, Categoria> tcCategoria;

	@FXML
	private Button btnImprimir;

	private List<Produto> listProdutos;
	private ObservableList<Produto> observableListProdutos;

	// Atributos para manipulação de Banco de Dados
	private final Database database = DatabaseFactory.getDatabase("postgresql");
	private final Connection connection = database.conectar();
	private final ProdutoDAO produtoDAO = new ProdutoDAO();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		produtoDAO.setConnection(connection);

		carregarTableViewProdutos();

	}

	public void carregarTableViewProdutos() {
		tcCodigo.setCellValueFactory(new PropertyValueFactory<>("cdProduto"));
		tcNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tcPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
		tcQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tcCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

		listProdutos = produtoDAO.listar();

		observableListProdutos = FXCollections.observableArrayList(listProdutos);
		tblProdutos.setItems(observableListProdutos);
	}

	public void handleImprimir() throws JRException {
		// HashMap filtro = new HashMap();
		// filtro.put("cdCategoria", 1);

		URL url = getClass().getResource("/javafxmvc/relatorios/JFXMVCProd.jasper");
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);// null: caso não existam
																								// filtros
		JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);// false: não deixa fechar a aplicação
																			// principal
		jasperViewer.setVisible(true);

	}

}
