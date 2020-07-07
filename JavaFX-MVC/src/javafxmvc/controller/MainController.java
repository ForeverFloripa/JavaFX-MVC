package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

public class MainController implements Initializable {

	@FXML
	MenuItem mitCadastrosClientes;
	@FXML
	MenuItem mitProcessosVendas;
	@FXML
	MenuItem mitGraficosVendasMes;
	@FXML
	MenuItem mitEstoque;
	@FXML
	AnchorPane anchorpane;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	@FXML
	public void handleMenuItemCadastroClientes() throws IOException {
		AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLClientes.fxml"));
		anchorpane.getChildren().setAll(a);
	}

	@FXML
	public void handleMIVendas() throws IOException {
		AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLVendas.fxml"));
		anchorpane.getChildren().setAll(a);
	}

	@FXML
	public void handleMenuItemGraficosVendasPorMes() throws IOException {
		AnchorPane a = (AnchorPane) FXMLLoader
				.load(getClass().getResource("/javafxmvc/view/FXMLGraficoVendasMes.fxml"));
		anchorpane.getChildren().setAll(a);
	}

	@FXML
	public void handleMenuItemRelatoriosQuantidadeProdutos() throws IOException {
		AnchorPane a = (AnchorPane) FXMLLoader
				.load(getClass().getResource("/javafxmvc/view/FXMLRelatorio.fxml"));
		anchorpane.getChildren().setAll(a);
	}

}
